package com.huawei.micro.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志新增请求 VO。
 *
 * @author Eric
 * @since 1.0.0
 */
@ApiModel("操作日志新增请求")
@Data
public class OperateLogCreateVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 操作人（用户名）。 */
    @ApiModelProperty(value = "操作人", required = true, example = "testadmin")
    @NotBlank(message = "操作人不能为空")
    private String operator;

    /** 操作时间（为空则取当前时间）。 */
    @ApiModelProperty(value = "操作时间", example = "2026-06-15T10:00:00")
    private LocalDateTime operateTime;

    /** 接口路径。 */
    @ApiModelProperty(value = "接口路径", required = true, example = "/api/users")
    @NotBlank(message = "接口路径不能为空")
    private String requestPath;

    /** 请求方法。 */
    @ApiModelProperty(value = "请求方法", required = true, example = "POST")
    @NotBlank(message = "请求方法不能为空")
    private String requestMethod;

    /** 请求参数（JSON 字符串）。 */
    @ApiModelProperty(value = "请求参数", example = "{\"username\":\"test\"}")
    private String requestParams;

    /** 响应结果（JSON 字符串）。 */
    @ApiModelProperty(value = "响应结果", example = "{\"code\":200}")
    private String responseResult;
}
