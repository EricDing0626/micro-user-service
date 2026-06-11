package com.huawei.micro.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录响应 VO。
 *
 * @author Eric
 * @since 1.0.0
 */
@Data
public class LoginResponseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 登录 token。 */
    private String token;

    /** 用户 ID。 */
    private Long userId;

    /** 用户名。 */
    private String username;
}
