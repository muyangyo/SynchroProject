package com.muyangyo.project_management_system.global.service;

import com.muyangyo.project_management_system.global.components.Timer;
import com.muyangyo.project_management_system.global.mapper.UserMapper;
import com.muyangyo.project_management_system.global.model.User;
import com.muyangyo.project_management_system.global.model.response_model.LoadAfterLogin;
import com.muyangyo.project_management_system.global.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/1
 * Time: 11:24
 */
@Service
@Slf4j
public class LoginServer {
    @Autowired
    UserMapper userMapper;
    @Autowired
    Timer timer;

    public boolean check(User user) {
        User ret = userMapper.getByUsername(user);

        // 避免空指针问题
        if (ret == null) {
            log.info("没有该用户名数据");
            return false;
        }


        log.info("查询到了该用户{},验证中...", ret);
        return (ret.getUsername().equals(user.getUsername())) && ret.getPassword().equals(user.getPassword());
    }

    public Boolean setLoginStatus(HttpServletResponse response, HttpServletRequest httpServletRequest, User user, Boolean isRememberMe) {
        try {
            user = userMapper.getByUsername(user);//获取完整对象
            LoadAfterLogin loadAfterLogin = new LoadAfterLogin(user.getUsername(), user.getRole().toString(), timer.getFormatTime());
            Map<String, String> load = new HashMap<>();
            Field[] fields = LoadAfterLogin.class.getDeclaredFields();
            for (Field tmp : fields) {
                load.put(tmp.getName().toString(), tmp.get(loadAfterLogin).toString());
            }

            if (isRememberMe) {
                log.info("用户 {} 使用 Token 的方式登录", user.getUsername());
                //如果勾选了记住我,则使用token进行登录
                String token = TokenUtil.getToken(load);
                Cookie jwtCookie = new Cookie("Authorization", token);
                jwtCookie.setHttpOnly(true); // 防止JavaScript访问此Cookie
                jwtCookie.setPath("/"); // 设置Cookie的路径
                response.addCookie(jwtCookie);
            } else {
                //如果没有勾选记住我,则使用Session登录
                log.info("用户 {} 使用 Session 的方式登录", user.getUsername());
                HttpSession session = httpServletRequest.getSession(true);
                for (Map.Entry<String, String> temp : load.entrySet()) {
                    session.setAttribute(temp.getKey(), temp.getValue());
                }
            }
            response.sendRedirect("/index.html");
            return true;
        } catch (Exception e) {
            log.error("{} 用户登入时出现错误!", user.getUsername());
            e.printStackTrace();
            return false;
        }
    }
}
