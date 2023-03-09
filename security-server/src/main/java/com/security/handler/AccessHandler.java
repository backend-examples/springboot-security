package com.security.handler;

import com.common.enums.CodeEnum;
import com.common.utils.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理用户访问权限不足的异常抛出
 */
public class AccessHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("text/json;charset=utf-8");  // 设置返回数据格式为 json

        ObjectMapper objectMapper = new ObjectMapper();
        ResponseResult<Object> responseResult = new ResponseResult<>();

        responseResult.setCode(CodeEnum.USER_AUTH_DENY.getResultCode());
        responseResult.setMessage(CodeEnum.USER_AUTH_DENY.getResultMsg());

        objectMapper.writeValue(response.getWriter(), responseResult);
    }
}
