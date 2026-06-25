package com.huawei.micro.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * HTTP 请求 token 解析工具类。
 *
 * @author Eric
 * @since 1.0.0
 */
public final class TokenResolver {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_HEADER = "token";
    private static final String BEARER_PREFIX = "Bearer ";

    private TokenResolver() {
    }

    /**
     * 从请求头解析 token。
     *
     * @param request HTTP 请求
     * @return token，未携带时返回 null
     */
    public static String resolveToken(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authorization) && authorization.startsWith(BEARER_PREFIX)) {
            return authorization.substring(BEARER_PREFIX.length()).trim();
        }
        return request.getHeader(TOKEN_HEADER);
    }
}
