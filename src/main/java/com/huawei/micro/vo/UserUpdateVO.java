package com.huawei.micro.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 用户修改请求 VO
 */
@Data
public class UserUpdateVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "用户ID不能为空")
    private Long id;

    private String username;

    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String password;

    private String email;

    private String phone;

    private Integer status;

    private List<Long> roleIds;
}
