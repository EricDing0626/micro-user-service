package com.huawei.micro.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户角色关联实体，对应 sys_user_role 表。
 *
 * @author developer
 * @since 1.0.0
 */
@Data
@TableName("sys_user_role")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 ID。 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 用户 ID。 */
    @TableField("user_id")
    private Long userId;

    /** 角色 ID。 */
    @TableField("role_id")
    private Long roleId;

    /** 创建时间。 */
    @TableField("create_time")
    private LocalDateTime createTime;
}
