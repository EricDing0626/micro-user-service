package com.huawei.micro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 用户管理微服务启动类。
 *
 * @author Eric
 * @since 1.0.0
 */
@SpringBootApplication
@MapperScan("com.huawei.micro.mapper")
@EnableTransactionManagement
@EnableCaching
public class MicroUserApplication {

    /**
     * 应用入口。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(MicroUserApplication.class, args);
    }
}
