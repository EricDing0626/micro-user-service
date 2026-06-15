package com.huawei.micro.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志实体，对应 sys_operate_log 表。
 *
 * @author Eric
 * @since 1.0.0
 */
@Data
@TableName("sys_operate_log")
public class OperateLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 ID。 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 操作人（用户名）。 */
    @TableField("operator")
    private String operator;

    /** 操作时间。 */
    @TableField("operate_time")
    private LocalDateTime operateTime;

    /** 接口路径。 */
    @TableField("request_path")
    private String requestPath;

    /** 请求方法（GET/POST/PUT/DELETE 等）。 */
    @TableField("request_method")
    private String requestMethod;

    /** 请求参数（JSON 字符串）。 */
    @TableField("request_params")
    private String requestParams;

    /** 响应结果（JSON 字符串）。 */
    @TableField("response_result")
    private String responseResult;

    /** 创建时间。 */
    @TableField("create_time")
    private LocalDateTime createTime;
}
