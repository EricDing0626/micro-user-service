package com.huawei.micro.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录响应 VO
 */
@Data
public class LoginResponseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String token;
    private Long userId;
    private String username;
}
