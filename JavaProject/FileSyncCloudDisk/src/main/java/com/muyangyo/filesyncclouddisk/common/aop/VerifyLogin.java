package com.muyangyo.filesyncclouddisk.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/2
 * Time: 15:26
 */
@Component
@Slf4j
public class VerifyLogin implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //如果对方持有session
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userName") != null) {
            return true;
        }

        //如果都没有则返回401状态码
        response.setStatus(401);
        log.warn("用户访问 {} 时没有登录，请求被拒绝", request.getRequestURL());
        return false;
    }
}
