package com.security.handler;

import com.common.enums.CodeEnum;
import com.common.utils.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 屏蔽重定向页面
 * 用户在访问被拒绝时返回统一的 json 数据
 */
public class AuthEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ResponseResult<Object> responseResult = new ResponseResult<>(CodeEnum.USER_NOT_LOGIN);
        response.setContentType("text/json;charset=utf-8");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), responseResult);
    }
}
