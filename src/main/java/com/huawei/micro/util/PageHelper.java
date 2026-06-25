package com.huawei.micro.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huawei.micro.config.PageProperties;
import com.huawei.micro.vo.PageResultVO;

import java.util.List;

/**
 * 分页参数规范化与分页结果构建工具类。
 *
 * @author Eric
 * @since 1.0.0
 */
public final class PageHelper {

    private PageHelper() {
    }

    /**
     * 规范化页码。
     *
     * @param pageNum        页码
     * @param pageProperties 分页配置
     * @return 有效页码
     */
    public static int normalizePageNum(Integer pageNum, PageProperties pageProperties) {
        if (pageNum == null || pageNum < 1) {
            return pageProperties.getDefaultPageNum();
        }
        return pageNum;
    }

    /**
     * 规范化每页条数。
     *
     * @param pageSize       每页条数
     * @param pageProperties 分页配置
     * @return 有效每页条数
     */
    public static int normalizePageSize(Integer pageSize, PageProperties pageProperties) {
        if (pageSize == null || pageSize < 1) {
            return pageProperties.getDefaultPageSize();
        }
        return Math.min(pageSize, pageProperties.getMaxPageSize());
    }

    /**
     * 构建分页结果 VO。
     *
     * @param page    MyBatis-Plus 分页对象
     * @param records 当前页记录
     * @param <T>     记录类型
     * @return 分页结果
     */
    public static <T> PageResultVO<T> buildPageResult(Page<?> page, List<T> records) {
        PageResultVO<T> pageResult = new PageResultVO<>();
        pageResult.setRecords(records);
        pageResult.setTotal(page.getTotal());
        pageResult.setPageNum(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setPages(page.getPages());
        return pageResult;
    }
}
