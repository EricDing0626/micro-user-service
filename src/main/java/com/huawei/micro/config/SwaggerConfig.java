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

/**
 * Swagger 在线文档配置。
 *
 * @author Eric
 * @since 1.0.0
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    private static final String AUTH_HEADER = "Authorization";

    /**
     * 用户管理模块 API 文档。
     *
     * @return Swagger Docket
     */
    @Bean
    public Docket userApiDocket() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("用户管理模块")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.huawei.micro.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("用户管理微服务 API 文档")
                .description("micro-user-service 接口在线调试文档，登录后在 Authorize 中填写 Bearer token")
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
                        .operationSelector(operationContext -> {
                            String pattern = operationContext.requestMappingPattern();
                            return pattern != null && !pattern.contains("/api/auth/login");
                        })
                        .build());
    }
}
