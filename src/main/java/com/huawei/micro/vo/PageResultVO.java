package com.huawei.micro.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果 VO。
 *
 * @param <T> 记录数据类型
 * @author Eric
 * @since 1.0.0
 */
@Data
public class PageResultVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 当前页数据列表。 */
    private List<T> records;

    /** 总记录数。 */
    private long total;

    /** 当前页码。 */
    private long pageNum;

    /** 每页条数。 */
    private long pageSize;

    /** 总页数。 */
    private long pages;
}
