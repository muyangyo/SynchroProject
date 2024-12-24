package com.muyangyo.fileclouddisk.common.aspect;

import com.muyangyo.fileclouddisk.common.config.Setting;
import com.muyangyo.fileclouddisk.common.model.other.Result;
import com.muyangyo.fileclouddisk.common.utils.NetworkUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/12/17
 * Time: 16:12
 */
@Aspect
@Order(1)
@Component
@Slf4j
public class LocalOperationAspect {
    @Resource
    private Setting setting;

    private static final String FORBIDDEN_RESPONSE = "非法操作，请在本地进行操作";

    @Around("@annotation(com.muyangyo.fileclouddisk.common.aspect.annotations.LocalOperation) || @within(com.muyangyo.fileclouddisk.common.aspect.annotations.LocalOperation)")
    public Object checkLocalRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        // 检查是否限制本地操作
        if (!setting.isLocalOperationOnly()) {
            return joinPoint.proceed(); // 如果不限制，直接执行目标方法
        }

        log.trace("由于开启了本地操作限制，{} 的 {} 本方法只能在本地进行", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

        // 获取当前请求的 HttpServletRequest
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // 检查是否是回环地址
        if (NetworkUtils.isLocalhost(request)) {
            // 如果是回环地址，继续执行目标方法
            return joinPoint.proceed();
        } else {
            // 如果不是回环地址，返回统一的错误信息
            return Result.fail(FORBIDDEN_RESPONSE);
        }
    }
}
