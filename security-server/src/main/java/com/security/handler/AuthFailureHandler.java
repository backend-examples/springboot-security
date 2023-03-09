package com.security.handler;

import com.common.enums.CodeEnum;
import com.common.utils.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限认证失败处理器
 */
public class AuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ResponseResult<Object> responseResult = null;

        if (exception instanceof AccountExpiredException) {
            //账号过期
            responseResult = new ResponseResult<>(CodeEnum.USER_ACCOUNT_EXPIRED);
        } else if (exception instanceof BadCredentialsException) {
            //密码错误
            responseResult = new ResponseResult<>(CodeEnum.USER_PASSWORD_ERROR);
        } else if (exception instanceof CredentialsExpiredException) {
            //密码过期
            responseResult = new ResponseResult<>(CodeEnum.USER_PASSWORD_EXPIRED);
        } else if (exception instanceof DisabledException) {
            //账号不可用
            responseResult = new ResponseResult<>(CodeEnum.USER_ACCOUNT_DISABLE);
        } else if (exception instanceof LockedException) {
            //账号锁定
            responseResult = new ResponseResult<>(CodeEnum.USER_ACCOUNT_LOCKED);
        } else if (exception instanceof InternalAuthenticationServiceException) {
            //用户不存在
            responseResult = new ResponseResult<>(CodeEnum.USER_ACCOUNT_NOT_EXIST);
        } else {
            //其他错误
            responseResult = new ResponseResult<>(CodeEnum.INTERNAL_SERVER_ERROR);
        }

        response.setContentType("text/json;charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), responseResult);
    }
}
