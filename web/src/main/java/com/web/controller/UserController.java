package com.web.controller;

import com.common.enums.CodeEnum;
import com.common.utils.ResponseResult;
import com.domain.vo.RegistrationForm;
import impl.RegisterImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private RegisterImpl registerImpl;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public ResponseResult<Object> register(RegistrationForm form) {
        ResponseResult<Object> responseResult = new ResponseResult<>();
        try {
            registerImpl.registerUser(form);
            responseResult.setCode(CodeEnum.SUCCESS.getResultCode());
        } catch (Exception e) {
            responseResult.setCode(CodeEnum.INTERNAL_SERVER_ERROR.getResultCode());
            responseResult.setMessage(CodeEnum.INTERNAL_SERVER_ERROR.getResultMsg());
        }

        return responseResult;
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public ResponseResult login (@RequestParam String username, @RequestParam String password) {
        System.out.println("username: " + username);
        System.out.println("password: " + password);

        return ResponseResult.success();
    }
}