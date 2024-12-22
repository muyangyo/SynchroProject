package com.muyangyo.fileclouddisk.common.aspect;

import com.muyangyo.fileclouddisk.common.aspect.annotations.UserOperationLimit;
import com.muyangyo.fileclouddisk.common.model.enums.Roles;
import com.muyangyo.fileclouddisk.common.model.meta.User;
import com.muyangyo.fileclouddisk.common.model.other.Result;
import com.muyangyo.fileclouddisk.common.utils.TokenUtils;
import com.muyangyo.fileclouddisk.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Aspect
@Order(2)
@Component
@Slf4j
public class UserOperationLimitAspect {

    @Resource
    private UserMapper userMapper;

    @Around("@annotation(com.muyangyo.fileclouddisk.common.aspect.annotations.UserOperationLimit) || @within(com.muyangyo.fileclouddisk.common.aspect.annotations.UserOperationLimit)")
    public Object checkUserOperation(ProceedingJoinPoint joinPoint) {
        try {
            // 获取当前请求的 HttpServletRequest
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            // 从注解中获取所需权限
            String requiredPermission = getRequiredPermission(joinPoint);

            // 从请求头中获取 token 的载荷
            String token = TokenUtils.getTokenFromCookie(request);
            TokenUtils.TokenLoad tokenLoad = TokenUtils.TokenLoad.fromMap(TokenUtils.getTokenLoad(token));

            // 判断当前用户是否是用户
            if (!tokenLoad.getRole().equals(String.valueOf(Roles.USER))) {
                log.warn("当前用户未注册，无权进行此操作");
                return Result.fail("无权限进行此操作");
            }

            // 获取当前用户
            User user = userMapper.selectByUserId(tokenLoad.getUserId());
            if (user == null) {
                log.warn("当前用户不存在");
                return Result.fail("无权限进行此操作");
            }

            // 检查用户是否拥有所需权限
            if (hasPermission(user, requiredPermission)) {
                // 如果有权限，继续执行目标方法
                return joinPoint.proceed();
            } else {
                // 如果没有权限，返回统一的错误信息
                log.warn("{} 的 {} 方法需要 [{}] 权限,当前用户 [{}] 没有权限进行此操作",
                        joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), requiredPermission, user.getUsername());
                return Result.fail("无权限进行此操作");
            }
        } catch (Exception e) {
            log.error("用户操作权限检查异常", e);
            return Result.fail("系统异常，请联系管理员!");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private boolean hasPermission(User user, String requiredPermission) {
        if (requiredPermission.isEmpty()) {
            return true; // 没有指定权限，默认有权限
        }
        Set<Character> userHasPermissions = user.getPermissionsSet();
        for (Character required : requiredPermission.toCharArray()) {
            if (!userHasPermissions.contains(required)) {
                return false; // 没有权限
            }
        }
        return true; // 有权限
    }

    private String getRequiredPermission(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        UserOperationLimit methodAnnotation = methodSignature.getMethod().getAnnotation(UserOperationLimit.class);
        if (methodAnnotation != null) {
            return methodAnnotation.value(); // 获取注解的值
        }
        // 如果方法上没有注解，检查类上的注解
        Class<?> clazz = methodSignature.getMethod().getDeclaringClass();
        UserOperationLimit classAnnotation = clazz.getAnnotation(UserOperationLimit.class);
        if (classAnnotation != null) {
            return classAnnotation.value();
        }
        // 没有指定权限，默认无权限
        return "";
    }
}