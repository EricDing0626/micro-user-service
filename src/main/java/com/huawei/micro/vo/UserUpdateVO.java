package com.huawei.micro.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 用户修改请求 VO。
 *
 * @author Eric
 * @since 1.0.0
 */
@Data
public class UserUpdateVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户 ID。 */
    @NotNull(message = "用户ID不能为空")
    private Long id;

    /** 用户名。 */
    private String username;

    /** 密码。 */
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String password;

    /** 邮箱。 */
    private String email;

    /** 手机号。 */
    private String phone;

    /** 状态：0-禁用，1-启用。 */
    private Integer status;

    /** 角色 ID 列表。 */
    private List<Long> roleIds;
}
