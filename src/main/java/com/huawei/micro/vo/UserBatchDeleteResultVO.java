package com.huawei.micro.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户批量删除结果 VO
 */
@Data
public class UserBatchDeleteResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int successCount;
    private int failedCount;
    private List<Long> successIds = new ArrayList<>();
    private List<Long> failedIds = new ArrayList<>();
}
