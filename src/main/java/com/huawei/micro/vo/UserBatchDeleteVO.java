package com.huawei.micro.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 用户批量删除请求 VO。
 *
 * @author Eric
 * @since 1.0.0
 */
@Data
public class UserBatchDeleteVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 待删除用户 ID 列表。 */
    @NotEmpty(message = "用户ID列表不能为空")
    private List<Long> ids;
}
