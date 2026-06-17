package com.huawei.micro.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 基础数据新增请求 VO。
 *
 * @author Eric
 * @since 1.0.0
 */
@ApiModel("基础数据新增请求")
@Data
public class BaseDataCreateVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 数据类型编码。 */
    @ApiModelProperty(value = "数据类型编码", required = true, example = "user_status")
    @NotBlank(message = "数据类型编码不能为空")
    private String typeCode;

    /** 数据编码。 */
    @ApiModelProperty(value = "数据编码", required = true, example = "1")
    @NotBlank(message = "数据编码不能为空")
    private String dataCode;

    /** 数据名称。 */
    @ApiModelProperty(value = "数据名称", required = true, example = "启用")
    @NotBlank(message = "数据名称不能为空")
    private String dataName;

    /** 排序号。 */
    @ApiModelProperty(value = "排序号", example = "1")
    private Integer sort;

    /** 状态：0-禁用，1-启用。 */
    @ApiModelProperty(value = "状态", example = "1")
    private Integer status;
}
