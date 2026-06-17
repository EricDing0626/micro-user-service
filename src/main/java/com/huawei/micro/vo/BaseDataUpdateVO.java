package com.huawei.micro.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 基础数据修改请求 VO。
 *
 * @author Eric
 * @since 1.0.0
 */
@ApiModel("基础数据修改请求")
@Data
public class BaseDataUpdateVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 ID。 */
    @ApiModelProperty(value = "基础数据ID", required = true)
    @NotNull(message = "基础数据ID不能为空")
    private Long id;

    /** 数据类型编码。 */
    @ApiModelProperty(value = "数据类型编码", example = "user_status")
    private String typeCode;

    /** 数据编码。 */
    @ApiModelProperty(value = "数据编码", example = "1")
    private String dataCode;

    /** 数据名称。 */
    @ApiModelProperty(value = "数据名称", example = "启用")
    private String dataName;

    /** 排序号。 */
    @ApiModelProperty(value = "排序号", example = "1")
    private Integer sort;

    /** 状态：0-禁用，1-启用。 */
    @ApiModelProperty(value = "状态", example = "1")
    private Integer status;
}
