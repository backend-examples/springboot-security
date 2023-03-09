package com.web.controller;

import com.common.enums.CodeEnum;
import com.common.utils.ResponseResult;
import com.domain.pojo.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin(origins = "*")
public class CommonApiRESTController {
    @GetMapping("/user/common")
    public ResponseResult<Object> userCommonRequest(@AuthenticationPrincipal User user) {
        System.out.println("User: "+user.getUsername()+" Authorities: "+user.getAuthorities());
        return new ResponseResult<>(CodeEnum.SUCCESS);
    }

    @GetMapping("/admin/common")
    public ResponseResult<Object> adminCommonRequest(@AuthenticationPrincipal User user) {
        System.out.println("User: "+user.getUsername()+" Authorities: "+user.getAuthorities());
        return new ResponseResult<>(CodeEnum.SUCCESS);
    }
}