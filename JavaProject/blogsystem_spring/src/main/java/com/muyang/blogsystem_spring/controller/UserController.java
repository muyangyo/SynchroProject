package com.muyang.blogsystem_spring.controller;

import com.muyang.blogsystem_spring.model.Result;
import com.muyang.blogsystem_spring.model.User;
import com.muyang.blogsystem_spring.model.UserInfo;
import com.muyang.blogsystem_spring.model.requestmodel.ForLogin;
import com.muyang.blogsystem_spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/20
 * Time: 11:24
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping("/getUserInfo")
    public UserInfo getUserInfo(HttpServletRequest httpServletRequest) throws Exception {
        String token = httpServletRequest.getHeader("token");
        if (token == null) {
            return null;
        }
        return userService.getUserInfo(token);
    }

    @RequestMapping("/getEditorUserInfo")
    public UserInfo getEditorUserInfo(HttpServletRequest httpServletRequest, Integer blogId) throws Exception {
        String token = httpServletRequest.getHeader("token");
        if (token == null) {
            return null;
        }
        if (blogId == null) {
            return null;
        }
        return userService.getEditorUserInfo(token, blogId);
    }

    @RequestMapping(value = "/login")
    public Result login(ForLogin forLogin) {
        if (forLogin == null) {
            return Result.fail("无效登录信息");
        }
        String userName = forLogin.getUsername();
        String password = forLogin.getPassword();
        if (!StringUtils.hasLength(userName) || !StringUtils.hasLength(password)) {
            return Result.fail("无效登录信息");
        }
        return userService.checkLogin(userName, password);
    }

    @RequestMapping(value = "/register")
    public Result register(User user) {
        if (user == null) {
            return Result.fail("无效注册信息");
        }
        String userName = user.getUserName();
        String password = user.getPassword();
        if (!StringUtils.hasLength(userName) || !StringUtils.hasLength(password)) {
            return Result.fail("无效注册信息");
        }
        if (!StringUtils.hasLength(user.getGithubUrl())) {
            user.setGithubUrl(null);
        }
        return userService.register(user);
    }

}
