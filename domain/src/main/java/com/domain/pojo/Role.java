package com.domain.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class Role {

    private Integer id;

    private String roleName;

    public Role(Integer id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }
}
