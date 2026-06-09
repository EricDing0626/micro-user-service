package com.huawei.micro.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 分页参数配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.page")
public class PageProperties {

    private int defaultPageNum = 1;
    private int defaultPageSize = 10;
    private int maxPageSize = 100;
}
