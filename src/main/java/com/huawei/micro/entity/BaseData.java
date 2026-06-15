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
 * 基础数据实体，对应 sys_base_data 表。
 *
 * @author Eric
 * @since 1.0.0
 */
@Data
@TableName("sys_base_data")
public class BaseData implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 ID。 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 数据类型编码（如 user_status、role_type）。 */
    @TableField("type_code")
    private String typeCode;

    /** 数据编码。 */
    @TableField("data_code")
    private String dataCode;

    /** 数据名称。 */
    @TableField("data_name")
    private String dataName;

    /** 排序号，越小越靠前。 */
    @TableField("sort")
    private Integer sort;

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
