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
 * 角色实体，对应 sys_role 表。
 *
 * @author Eric
 * @since 1.0.0
 */
@Data
@TableName("sys_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 ID。 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 角色名称。 */
    @TableField("role_name")
    private String roleName;

    /** 角色编码。 */
    @TableField("role_code")
    private String roleCode;

    /** 角色描述。 */
    @TableField("description")
    private String description;

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
