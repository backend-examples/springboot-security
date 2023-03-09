package com.security.handler;

import com.common.enums.CodeEnum;
import com.common.utils.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 需要实现权限认证成功处理器
 */
public class AuthSuccessHandler implements AuthenticationSuccessHandler  {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("text/json;charset=utf-8");  // 设置返回数据格式为 json

        ObjectMapper objectMapper = new ObjectMapper();
        ResponseResult<Object> responseResult = new ResponseResult<>(CodeEnum.SUCCESS);

        objectMapper.writeValue(response.getWriter(), responseResult);
    }
}
