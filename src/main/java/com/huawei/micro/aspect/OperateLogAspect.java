package com.huawei.micro.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huawei.micro.entity.User;
import com.huawei.micro.mapper.UserMapper;
import com.huawei.micro.service.OperateLogService;
import com.huawei.micro.util.TokenStore;
import com.huawei.micro.vo.LoginVO;
import com.huawei.micro.vo.OperateLogCreateVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 操作日志切面，拦截 {@code /api/*} 接口并自动写入操作日志。
 *
 * @author Eric
 * @since 1.0.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperateLogAspect {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_HEADER = "token";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String UNKNOWN_OPERATOR = "anonymous";
    private static final int MAX_JSON_LENGTH = 4000;

    private final OperateLogService operateLogService;
    private final TokenStore tokenStore;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;

    /**
     * 切点：拦截 controller 包下 REST 接口，排除操作日志管理接口避免重复记录。
     */
    @Pointcut("execution(* com.huawei.micro.controller..*.*(..)) "
            + "&& !within(com.huawei.micro.controller.OperateLogController)")
    public void apiControllerPointcut() {
    }

    /**
     * 环绕通知：采集操作人、请求信息、响应结果并写入操作日志。
     *
     * @param joinPoint 连接点
     * @return 原方法返回值
     * @throws Throwable 原方法异常
     */
    @Around("apiControllerPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime operateTime = LocalDateTime.now();
        HttpServletRequest request = getCurrentRequest();
        String requestPath = request != null ? request.getRequestURI() : "";
        String requestMethod = request != null ? request.getMethod() : "";
        String operator = resolveOperator(request, joinPoint.getArgs());
        String requestParams = buildRequestParams(joinPoint.getArgs());

        Object result = null;
        Throwable thrown = null;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable ex) {
            thrown = ex;
            throw ex;
        } finally {
            String responseResult = thrown != null ? toJson("error: " + thrown.getMessage()) : toJson(result);
            saveOperateLog(operator, operateTime, requestPath, requestMethod, requestParams, responseResult);
        }
    }

    private void saveOperateLog(String operator, LocalDateTime operateTime, String requestPath,
                                String requestMethod, String requestParams, String responseResult) {
        try {
            OperateLogCreateVO createVO = new OperateLogCreateVO();
            createVO.setOperator(operator);
            createVO.setOperateTime(operateTime);
            createVO.setRequestPath(requestPath);
            createVO.setRequestMethod(requestMethod);
            createVO.setRequestParams(requestParams);
            createVO.setResponseResult(responseResult);
            operateLogService.createOperateLog(createVO);
        } catch (Exception ex) {
            log.warn("自动记录操作日志失败: path={}, method={}", requestPath, requestMethod, ex);
        }
    }

    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return attributes.getRequest();
    }

    private String resolveOperator(HttpServletRequest request, Object[] args) {
        if (request != null) {
            String token = resolveToken(request);
            if (StringUtils.hasText(token)) {
                Long userId = tokenStore.getUserId(token);
                if (userId != null) {
                    User user = userMapper.selectById(userId);
                    if (user != null && StringUtils.hasText(user.getUsername())) {
                        return user.getUsername();
                    }
                }
            }
        }
        if (args != null) {
            for (Object arg : args) {
                if (arg instanceof LoginVO) {
                    LoginVO loginVO = (LoginVO) arg;
                    if (StringUtils.hasText(loginVO.getUsername())) {
                        return loginVO.getUsername();
                    }
                }
            }
        }
        return UNKNOWN_OPERATOR;
    }

    private String resolveToken(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authorization) && authorization.startsWith(BEARER_PREFIX)) {
            return authorization.substring(BEARER_PREFIX.length()).trim();
        }
        return request.getHeader(TOKEN_HEADER);
    }

    private String buildRequestParams(Object[] args) {
        if (args == null || args.length == 0) {
            return null;
        }
        List<Object> params = new ArrayList<>();
        for (Object arg : args) {
            if (arg == null || isSkippableArg(arg)) {
                continue;
            }
            params.add(arg);
        }
        if (params.isEmpty()) {
            return null;
        }
        if (params.size() == 1) {
            return toJson(params.get(0));
        }
        return toJson(params);
    }

    private boolean isSkippableArg(Object arg) {
        return arg instanceof HttpServletRequest
                || arg instanceof HttpServletResponse;
    }

    private String toJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return truncate(objectMapper.writeValueAsString(value));
        } catch (JsonProcessingException ex) {
            return truncate(String.valueOf(value));
        }
    }

    private String truncate(String text) {
        if (text == null || text.length() <= MAX_JSON_LENGTH) {
            return text;
        }
        return text.substring(0, MAX_JSON_LENGTH);
    }
}
