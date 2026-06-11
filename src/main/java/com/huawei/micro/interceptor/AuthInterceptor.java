package com.huawei.micro.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huawei.micro.common.Result;
import com.huawei.micro.common.ResultCode;
import com.huawei.micro.util.TokenStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 登录态拦截器，校验请求 token。
 *
 * @author Eric
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_HEADER = "token";
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenStore tokenStore;
    private final ObjectMapper objectMapper;

    /**
     * 请求前置校验登录态。
     *
     * @param request  HTTP 请求
     * @param response HTTP 响应
     * @param handler  处理器
     * @return 是否放行
     * @throws IOException 响应写入异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        String token = resolveToken(request);
        if (!StringUtils.hasText(token) || tokenStore.getUserId(token) == null) {
            writeUnauthorized(response);
            return false;
        }
        return true;
    }

    /**
     * 从请求头解析 token。
     *
     * @param request HTTP 请求
     * @return token，未携带时返回 null
     */
    private String resolveToken(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authorization) && authorization.startsWith(BEARER_PREFIX)) {
            return authorization.substring(BEARER_PREFIX.length()).trim();
        }
        return request.getHeader(TOKEN_HEADER);
    }

    /**
     * 写入未登录响应。
     *
     * @param response HTTP 响应
     * @throws IOException 响应写入异常
     */
    private void writeUnauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Result<Void> result = Result.fail(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
