package com.muyangyo.filesyncclouddisk.user.controller;

import com.muyangyo.filesyncclouddisk.common.model.dto.Login;
import com.muyangyo.filesyncclouddisk.common.model.other.Result;
import com.muyangyo.filesyncclouddisk.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/13
 * Time: 18:47
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody Login login) {
        if (StringUtils.hasLength(login.getUsername()) && StringUtils.hasLength(login.getPassword())) {
            if (login.getUsername().length() > 30) {
                return Result.fail("登录失败!用户名不能超过30个字符!", 401);
            }
            if (login.getPassword().length() > 32) {
                return Result.fail("登录失败!密码不能超过32个字符!", 401);
            }
            return userService.checkUser(login.getUsername(), login.getPassword());
        }
        return Result.fail("登录失败!用户名或密码为空!", 401);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result register(@RequestBody Login login) {
        if (StringUtils.hasLength(login.getUsername()) && StringUtils.hasLength(login.getPassword()) && StringUtils.hasLength(login.getKey())) {
            if (login.getUsername().length() > 30) {
                return Result.fail("创建新用户失败!用户名不能超过30个字符!", 401);
            }
            if (login.getPassword().length() > 32) {
                return Result.fail("创建新用户失败!密码不能超过32个字符!", 401);
            }
            return userService.createUser(login.getUsername(), login.getPassword(), login.getKey());
        }
        return Result.fail("创建新用户失败!密钥有误或用户名或密码为空!", 401);
    }

    @RequestMapping("/logout")
    public String logout() {
        return "logout";
    }
}
