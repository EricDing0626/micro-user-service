package com.huawei.micro.vo;

import com.huawei.micro.entity.Role;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户详情响应 VO。
 *
 * @author Eric
 * @since 1.0.0
 */
@Data
public class UserDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户 ID。 */
    private Long id;

    /** 用户名。 */
    private String username;

    /** 邮箱。 */
    private String email;

    /** 手机号。 */
    private String phone;

    /** 状态：0-禁用，1-启用。 */
    private Integer status;

    /** 创建时间。 */
    private LocalDateTime createTime;

    /** 更新时间。 */
    private LocalDateTime updateTime;

    /** 关联角色列表。 */
    private List<Role> roles;
}
