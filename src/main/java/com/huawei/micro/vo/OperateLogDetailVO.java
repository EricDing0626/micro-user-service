package com.huawei.micro.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志详情响应 VO。
 *
 * @author Eric
 * @since 1.0.0
 */
@ApiModel("操作日志详情")
@Data
public class OperateLogDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 ID。 */
    @ApiModelProperty("日志ID")
    private Long id;

    /** 操作人（用户名）。 */
    @ApiModelProperty("操作人")
    private String operator;

    /** 操作时间。 */
    @ApiModelProperty("操作时间")
    private LocalDateTime operateTime;

    /** 接口路径。 */
    @ApiModelProperty("接口路径")
    private String requestPath;

    /** 请求方法。 */
    @ApiModelProperty("请求方法")
    private String requestMethod;

    /** 请求参数（JSON 字符串）。 */
    @ApiModelProperty("请求参数")
    private String requestParams;

    /** 响应结果（JSON 字符串）。 */
    @ApiModelProperty("响应结果")
    private String responseResult;

    /** 创建时间。 */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
