package com.muyangyo.fileclouddisk.user.controller;


import com.muyangyo.fileclouddisk.common.model.dto.Login;
import com.muyangyo.fileclouddisk.common.model.other.Result;
import com.muyangyo.fileclouddisk.common.utils.NetworkUtils;
import com.muyangyo.fileclouddisk.user.service.UserService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @Resource
    UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody Login login, HttpServletRequest request, HttpServletResponse response) {
        userService.decryptLogin(login, NetworkUtils.getClientIp(request));// 解密登录信息 TODO: 记得释放

        if (StringUtils.hasLength(login.getUsername()) && StringUtils.hasLength(login.getPassword())) {
            if (login.getUsername().length() > 30) {
                return Result.fail("登录失败!用户名不能超过30个字符!", 401);
            }
            if (login.getPassword().length() > 32) {
                return Result.fail("登录失败!密码不能超过32个字符!", 401);
            }
            return userService.checkUser(login.getUsername(), login.getPassword(), request, response);
        }
        return Result.fail("登录失败!用户名或密码为空!", 401);
    }

//    @RequestMapping(value = "/register", method = RequestMethod.POST)
//    public Result register(@RequestBody Login login, HttpServletRequest request) {
////        userService.decryptLogin(login, NetworkUtils.getClientIp(request));// 解密登录信息  TODO: 记得释放
//
//        if (StringUtils.hasLength(login.getUsername()) && StringUtils.hasLength(login.getPassword()) && StringUtils.hasLength(login.getKey())) {
//            if (login.getUsername().length() > 30) {
//                return Result.fail("创建新用户失败!用户名不能超过30个字符!", 401);
//            }
//            if (login.getPassword().length() > 32) {
//                return Result.fail("创建新用户失败!密码不能超过32个字符!", 401);
//            }
//            return userService.createUser(login.getUsername(), login.getPassword(), login.getKey());
//        }
//        return Result.fail("创建新用户失败!密钥有误或用户名或密码为空!", 401);
//    }


    @RequestMapping(value = "/getPublicKey", method = RequestMethod.GET)
    public Result getPublicKey(HttpServletRequest request) {
        return userService.getPublicKey(NetworkUtils.getClientIp(request));
    }

    @RequestMapping("/logout")
    public String logout() {
        return "logout";
    }
}
