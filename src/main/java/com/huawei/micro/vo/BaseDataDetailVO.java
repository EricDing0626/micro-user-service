package com.huawei.micro.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础数据详情响应 VO。
 *
 * @author Eric
 * @since 1.0.0
 */
@ApiModel("基础数据详情")
@Data
public class BaseDataDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 ID。 */
    @ApiModelProperty("基础数据ID")
    private Long id;

    /** 数据类型编码。 */
    @ApiModelProperty("数据类型编码")
    private String typeCode;

    /** 数据编码。 */
    @ApiModelProperty("数据编码")
    private String dataCode;

    /** 数据名称。 */
    @ApiModelProperty("数据名称")
    private String dataName;

    /** 排序号。 */
    @ApiModelProperty("排序号")
    private Integer sort;

    /** 状态：0-禁用，1-启用。 */
    @ApiModelProperty("状态")
    private Integer status;

    /** 创建时间。 */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /** 更新时间。 */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
