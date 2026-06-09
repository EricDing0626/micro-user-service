package com.huawei.micro.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果 VO
 */
@Data
public class PageResultVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<T> records;
    private long total;
    private long pageNum;
    private long pageSize;
    private long pages;
}
