package com.huawei.micro.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体，对应 sys_user 表。
 *
 * @author Eric
 * @since 1.0.0
 */
@Data
@TableName("sys_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 ID。 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 用户名。 */
    @TableField("username")
    private String username;

    /** 密码（MD5 密文）。 */
    @TableField("password")
    private String password;

    /** 邮箱。 */
    @TableField("email")
    private String email;

    /** 手机号。 */
    @TableField("phone")
    private String phone;

    /** 状态：0-禁用，1-启用。 */
    @TableField("status")
    private Integer status;

    /** 逻辑删除标识：0-未删除，1-已删除。 */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    /** 创建时间。 */
    @TableField("create_time")
    private LocalDateTime createTime;

    /** 更新时间。 */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
