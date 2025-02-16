package com.muyangyo.filesyncclouddisk.common.aspect;

import com.muyangyo.filesyncclouddisk.common.model.enums.Roles;
import com.muyangyo.filesyncclouddisk.common.model.other.Result;
import com.muyangyo.filesyncclouddisk.common.utils.TokenUtils;
import com.muyangyo.filesyncclouddisk.manager.mapper.AdminMapper;
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

@Aspect
@Order(2)
@Component
@Slf4j
public class AdminOperationAspect {
    @Resource
    private AdminMapper adminMapper;

    @Around("@annotation(com.muyangyo.filesyncclouddisk.common.aspect.annotations.AdminRequired) || @within(com.muyangyo.filesyncclouddisk.common.aspect.annotations.AdminRequired)")
    public Object checkAdminRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取当前请求的 HttpServletRequest
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // 从请求头中获取 token 的载荷
        String token = TokenUtils.getTokenFromCookie(request);
        TokenUtils.TokenLoad tokenLoad = TokenUtils.TokenLoad.fromMap(TokenUtils.getTokenLoad(token));
        // 判断当前用户是否是管理员
        if (!tokenLoad.getRole().equals(String.valueOf(Roles.ADMIN))) {
            // 如果不是管理员，返回统一的错误信息
            log.warn("{} 的 {} 方法需要管理员权限,请使用管理员账号进行操作", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            return Result.fail("无管理员权限，请使用管理员账号进行操作");
        }
        // 看看有没有这个用户
        String userId = tokenLoad.getUserId();
        if (adminMapper.selectByUserId(userId) == null) {
            // 如果没有这个用户，返回统一的错误信息
            log.warn("{} 的 {} 方法需要管理员权限,但是该用户 {} 不存在", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), userId);
            return Result.fail("无管理员权限，请使用管理员账号进行操作");
        }

        // 如果是管理员，继续执行目标方法
        return joinPoint.proceed();
    }
}