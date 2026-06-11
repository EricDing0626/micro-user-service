package com.huawei.micro.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户登录请求 VO。
 *
 * @author Eric
 * @since 1.0.0
 */
@ApiModel("登录请求")
@Data
public class LoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户名。 */
    @ApiModelProperty(value = "用户名", required = true, example = "testadmin")
    @NotBlank(message = "用户名不能为空")
    private String username;

    /** 密码。 */
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    @NotBlank(message = "密码不能为空")
    private String password;
}
