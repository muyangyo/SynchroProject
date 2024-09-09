package com.muyangyo.project_management_system.global.aop;

import com.muyangyo.project_management_system.global.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //现在有两个判定的条件
        //1.如果请求持有token
        Cookie[] requestCookies = request.getCookies();
        for (Cookie tmp : requestCookies) {
            if (tmp.getName().equals("Authorization")) {
                if (TokenUtil.checkToken(tmp.getValue())) {
                    return true;
                }
            }
        }

        //2.如果对方持有session
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userName") != null) {
            return true;
        }

        //如果都没有则返回401状态码
        response.setStatus(401);
        log.warn("用户访问 {} 时没有登录,正在指引用户跳转至 login.html", request.getRequestURL());
        response.sendRedirect("/login.html");
        return false;
    }
}
