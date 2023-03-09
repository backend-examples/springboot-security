package com.common.config;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Swagger配置类
 * 在与Spring Boot集成时，放在与Application.java统计的目录下
 * 通过注解@Configuration让Spring来加载该类的配置
 * 在通过注解@EnableSwagger2来启用Swagger2
 * @author:sunxj
 * @date:2020-12-05 21:44:15
 * @description:Swagger配置类
 */
@EnableOpenApi
@Configuration
public class SwaggerConfiguration implements WebMvcConfigurer {

    @Autowired
    private SwaggerProperties swaggerProperties;

    /**
     * 创建API应用
     * apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例，用来控制那些借口暴露给Swagger来展现
     * 本例采用指定扫描的包路径来定义指定要建立API的目录
     * @return
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .pathMapping("/")
                .enable(swaggerProperties.getEnable())//定义是否开启swagger，false为关闭，可以通过变量控制
                .apiInfo(apiInfo())//将api的元信息设置为包含在json ResourceListing响应中。
                .host(swaggerProperties.getTryHost())//接口调试地址
                .select()//选择哪些接口作为swagger的doc发布
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                .apis(basePackage("com.domain;com.web"))
                .paths(PathSelectors.any())
                .build()
                .protocols(newHashSet("https", "http"))// 支持的通讯协议集合
                .securitySchemes(securitySchemes())// 授权信息设置，必要的header token等认证信息
                .securityContexts(securityContexts());// 授权信息全局应用
    }

    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://项目实际地址/swagger-ui.html
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getApplicationName() + "Restful APIs")//网页标题
                .description(swaggerProperties.getApplicationDescription())//网页描述
                .termsOfServiceUrl("http://localhost:8888/")//接口访问地址
                .contact(new Contact("user",null,"*3096015076@qq.com"))//名称，url，邮箱
                .version("Application Version: " + swaggerProperties.getApplicationVersion() + ", Spring Boot Version: " + SpringBootVersion.getVersion())//接口版本号
                .build();
    }
    /**
     * 设置授权信息
     */
    private List<SecurityScheme> securitySchemes() {
        ApiKey apiKey = new ApiKey("BASE_TOKEN", "token", io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER.toString());
        return Collections.singletonList(apiKey);
    }

    /**
     * 授权信息全局应用
     */
    private List<SecurityContext> securityContexts() {
        return Collections.singletonList(
                SecurityContext.builder()
                        .securityReferences(Collections.singletonList(new SecurityReference("BASE_TOKEN", new AuthorizationScope[]{new AuthorizationScope("global", "")})))
                        .build()
        );
    }

    @SafeVarargs
    private final <T> Set<T> newHashSet(T... ts) {
        if (ts.length > 0) {
            return new LinkedHashSet<>(Arrays.asList(ts));
        }
        return null;
    }

    /**
     * 通用拦截器排除swagger设置，所有拦截器都会自动加swagger相关的资源排除信息
     */
    @SuppressWarnings("unchecked")
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        try {
            Field registrationsField = FieldUtils.getField(InterceptorRegistry.class, "registrations",true);
            List<InterceptorRegistration> registrations = (List<InterceptorRegistration>) ReflectionUtils.getField(registrationsField, registry);
            if (registrations != null) {
                for (InterceptorRegistration interceptorRegistration : registrations) {
                    interceptorRegistration
                            .excludePathPatterns("/swagger**/**")
                            .excludePathPatterns("/webjars/**")
                            .excludePathPatterns("/v3/**")
                            .excludePathPatterns("/doc.html");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).map(handlerPackage(basePackage)).orElse(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage)     {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage.split(";")) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.ofNullable(input.declaringClass());
    }
}