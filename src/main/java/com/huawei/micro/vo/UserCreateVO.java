package com.huawei.micro.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 用户新增请求 VO。
 *
 * @author Eric
 * @since 1.0.0
 */
@ApiModel("用户新增请求")
@Data
public class UserCreateVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户名。 */
    @ApiModelProperty(value = "用户名", required = true, example = "zhangsan")
    @NotBlank(message = "用户名不能为空")
    private String username;

    /** 密码。 */
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String password;

    /** 邮箱。 */
    private String email;

    /** 手机号。 */
    private String phone;

    /** 状态：0-禁用，1-启用。 */
    private Integer status;

    /** 角色 ID 列表。 */
    @ApiModelProperty(value = "角色ID列表", example = "[2]")
    private List<Long> roleIds;
}
