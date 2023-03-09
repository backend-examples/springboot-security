package com.security.config;


import com.security.handler.*;
import impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsServiceImpl userDetailsServiceImpl () {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public AuthSuccessHandler authSuccessHandler () {
        return new AuthSuccessHandler();
    }

    @Bean
    public AuthFailureHandler authFailureHandler () {
        return new AuthFailureHandler();
    }

    @Bean
    public AuthEntryPointHandler authEntryPointHandler () {
        return new AuthEntryPointHandler();
    }

    @Bean
    public AccessHandler accessHandler () {
        return new AccessHandler();
    }

    @Bean
    public AccountLogoutSuccessHandler accountLogoutSuccessHandler () {
        return new AccountLogoutSuccessHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //所需要用到的静态资源，允许访问
        web.ignoring().antMatchers( "/swagger-ui.html",
                "/swagger-ui/*",
                "/swagger-resources/**",
                "/v2/api-docs",
                "/v3/api-docs",
                "/webjars/**")
                // 放行/user/register，不需要登录就可以访问
                .antMatchers("/user/register");
    }

    /**
     * 配置认证方式等
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                // 将用户详情服务配置到 Spring Security 上
                .userDetailsService(userDetailsServiceImpl())
                // 在全局中使用加密
                .passwordEncoder(passwordEncoder());
    }

    /**
     * http 相关的配置，包括登入登出、异常处理、会话管理等
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 设置 csrf token，防止 post 请求拦截
        http
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        http
                .formLogin()
                // 设置登录接口路径，登录方式为 post 请求，字段为用户名 username 及密码 password
                .loginProcessingUrl("/user/login")
                // 注入登录成功处理器
                .successHandler(authSuccessHandler())
                // 注入登录失败处理器
                .failureHandler(authFailureHandler())
                .permitAll()
                .and()
                .authorizeRequests()
                // 使用 /api/user/** 而不是 /api/user
                .antMatchers("/api/user/**").hasRole("USER")
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/**").authenticated()
                .and()
                .logout()
                // 注入登出处理器
                .logoutSuccessHandler(accountLogoutSuccessHandler())
                // 登出时删除 JSESSIONID
                .deleteCookies("JSESSIONID")
                .and()
                // 异常处理
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPointHandler())
                .accessDeniedHandler(accessHandler());
    }
}
