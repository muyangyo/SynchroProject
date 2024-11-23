package com.muyangyo.fileclouddisk.common.aspect;


import com.muyangyo.fileclouddisk.common.config.Setting;
import com.muyangyo.fileclouddisk.common.utils.EasyTimedCache;
import com.muyangyo.fileclouddisk.common.utils.NetworkUtils;
import com.muyangyo.fileclouddisk.common.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
    @Resource
    private Setting setting;

    List<String> EXCLUDE_URL = new LinkedList<>(Arrays.asList("/api/user/login", "/api/user/register", "/api/user/getPublicKey"));

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String clientIp = NetworkUtils.getClientIp(request);// 获取客户端IP地址
        //如果是登录或注册接口，则记录登录或注册次数
        if (EXCLUDE_URL.contains(request.getRequestURI())) {
            return limitLoginAndRegister(request, clientIp);
        }

        //持有token
        Cookie[] requestCookies = request.getCookies();
        for (Cookie tmp : requestCookies) {
            if (tmp.getName().equals("Authorization")) {
                return TokenUtils.checkToken(tmp.getValue(), clientIp);
            }
        }

        //如果都没有则返回401状态码
        response.setStatus(401);
        log.warn("用户访问 [{}] 时没有登录，请求被拒绝", request.getRequestURL());
        return false;
    }

    /**
     * 限制登录和注册次数
     *
     * @param request
     * @return 是否允许访问
     */
    private boolean limitLoginAndRegister(HttpServletRequest request, String clientIp) {
        log.info("IP [{}] 正在尝试登入 || 注册 || 获取公钥", clientIp);
        EasyTimedCache<String, Integer> loginAndRegisterTimeCache = setting.getLoginAndRegisterTimeCache();
        Integer integer = loginAndRegisterTimeCache.get(clientIp);
        if (integer != null && integer > 0) {
            if (integer >= setting.getMaxNumberOfAttempts()) {
                // 尝试次数已满，拒绝访问
                log.warn("IP [{}] 尝试 登入 || 注册 || 获取公钥 次数过多，请求被拒绝", clientIp);
                return false;
            } else {
                // 尝试次数未满，继续尝试
                loginAndRegisterTimeCache.put(clientIp, ++integer);
                return true;
            }
        } else {
            // 第一次登录或注册
            loginAndRegisterTimeCache.put(clientIp, 1);
            return true;
        }
    }

}
