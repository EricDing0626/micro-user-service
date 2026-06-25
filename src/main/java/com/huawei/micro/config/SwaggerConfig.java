package com.huawei.micro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * Swagger 在线文档配置，按业务模块分组展示。
 *
 * @author Eric
 * @since 1.0.0
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    private static final String AUTH_HEADER = "Authorization";

    /**
     * 认证模块 API 文档。
     *
     * @return Swagger Docket
     */
    @Bean
    public Docket authApiDocket() {
        return buildDocket("认证管理", PathSelectors.ant("/api/auth/**"), false);
    }

    /**
     * 用户管理模块 API 文档。
     *
     * @return Swagger Docket
     */
    @Bean
    public Docket userApiDocket() {
        return buildDocket("用户管理", PathSelectors.ant("/api/users/**"), true);
    }

    /**
     * 操作日志模块 API 文档。
     *
     * @return Swagger Docket
     */
    @Bean
    public Docket operateLogApiDocket() {
        return buildDocket("操作日志管理", PathSelectors.ant("/api/operate-logs/**"), true);
    }

    /**
     * 基础数据模块 API 文档。
     *
     * @return Swagger Docket
     */
    @Bean
    public Docket baseDataApiDocket() {
        return buildDocket("基础数据管理", PathSelectors.ant("/api/base-data/**"), true);
    }

    private Docket buildDocket(String groupName, Predicate<String> pathSelector, boolean requireAuth) {
        Docket docket = new Docket(DocumentationType.OAS_30)
                .groupName(groupName)
                .apiInfo(apiInfo(groupName))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.huawei.micro.controller"))
                .paths(pathSelector)
                .build();
        if (requireAuth) {
            docket.securitySchemes(securitySchemes()).securityContexts(securityContexts());
        }
        return docket;
    }

    private ApiInfo apiInfo(String groupName) {
        return new ApiInfoBuilder()
                .title("micro-user-service API 文档 - " + groupName)
                .description("用户管理微服务在线调试文档。"
                        + "除登录接口外，请在 Authorize 中填写 Bearer {token}。"
                        + "操作日志支持 AOP 自动写入与 recent 联调查询；"
                        + "基础数据支持按 typeCode 查询与 Spring Cache。")
                .version("1.0.0")
                .contact(new Contact("Eric", "", ""))
                .build();
    }

    private List<SecurityScheme> securitySchemes() {
        return Collections.singletonList(new ApiKey(AUTH_HEADER, AUTH_HEADER, "header"));
    }

    private List<SecurityContext> securityContexts() {
        AuthorizationScope[] scopes = new AuthorizationScope[]{
                new AuthorizationScope("global", "accessEverything")
        };
        return Collections.singletonList(
                SecurityContext.builder()
                        .securityReferences(Collections.singletonList(new SecurityReference(AUTH_HEADER, scopes)))
                        .build());
    }
}
