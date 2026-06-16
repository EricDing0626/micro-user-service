package com.huawei.micro.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 操作日志切面，拦截 {@code /api/*} 接口并在后续写入操作日志。
 *
 * @author Eric
 * @since 1.0.0
 */
@Aspect
@Component
public class OperateLogAspect {

    /**
     * 切点：拦截 controller 包下所有 REST 接口（项目内均为 /api/* 路径）。
     */
    @Pointcut("execution(* com.huawei.micro.controller..*.*(..))")
    public void apiControllerPointcut() {
    }

    /**
     * 环绕通知占位，Commit 5 将补充操作人、请求参数与响应结果的采集与入库逻辑。
     *
     * @param joinPoint 连接点
     * @return 原方法返回值
     * @throws Throwable 原方法异常
     */
    @Around("apiControllerPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
}
