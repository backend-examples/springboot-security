package com.domain.vo;

import com.domain.pojo.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@ApiModel("注册表单")
public class RegistrationForm {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("角色")
    private Integer role;

    public User toUser(PasswordEncoder passwordEncoder, String roleName) {
        List<String> role = roleName == null ? Arrays.asList("ROLE_USER") : Arrays.asList(roleName);
        return new User(username, passwordEncoder.encode(password), role);
    }
}