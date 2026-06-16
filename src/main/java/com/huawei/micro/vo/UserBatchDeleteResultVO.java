package com.huawei.micro.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户批量删除结果 VO。
 *
 * @author developer
 * @since 1.0.0
 */
@Data
public class UserBatchDeleteResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 删除成功数量。 */
    private int successCount;

    /** 删除失败数量。 */
    private int failedCount;

    /** 删除成功的用户 ID 列表。 */
    private List<Long> successIds = new ArrayList<>();

    /** 删除失败的用户 ID 列表。 */
    private List<Long> failedIds = new ArrayList<>();
}
