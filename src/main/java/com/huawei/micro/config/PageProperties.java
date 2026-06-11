package com.huawei.micro.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 分页参数配置。
 *
 * @author Eric
 * @since 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.page")
public class PageProperties {

    /**
     * 默认页码。
     */
    private int defaultPageNum = 1;

    /**
     * 默认每页条数。
     */
    private int defaultPageSize = 10;

    /**
     * 最大每页条数。
     */
    private int maxPageSize = 100;
}
