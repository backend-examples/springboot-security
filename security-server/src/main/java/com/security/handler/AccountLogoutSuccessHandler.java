package com.security.handler;

import com.common.enums.CodeEnum;
import com.common.utils.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登出成功处理器
 */
public class AccountLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        response.setContentType("text/json;charset=utf-8");

        ObjectMapper objectMapper = new ObjectMapper();
        ResponseResult<Object> responseResult;

        if (authentication != null) {
            responseResult = new ResponseResult<>(CodeEnum.SUCCESS);
        } else {
            responseResult = new ResponseResult<>(CodeEnum.USER_NOT_LOGIN);
        }

        objectMapper.writeValue(response.getWriter(), responseResult);
    }
}
