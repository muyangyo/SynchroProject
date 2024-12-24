package com.muyangyo.fileclouddisk.user.controller;


import com.muyangyo.fileclouddisk.common.config.Setting;
import com.muyangyo.fileclouddisk.common.model.dto.LoginDTO;
import com.muyangyo.fileclouddisk.common.model.other.Result;
import com.muyangyo.fileclouddisk.common.utils.NetworkUtils;
import com.muyangyo.fileclouddisk.user.service.UserService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/api/user")
public class UserController {

    @Resource
    UserService userService;

    @Resource
    Setting setting;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        if (setting.isLoginAndRegisterUseEncrypt()) {
            userService.decryptLogin(loginDTO, NetworkUtils.getClientIp(request));// 解密登录信息
        }

        if (StringUtils.hasLength(loginDTO.getUsername()) && StringUtils.hasLength(loginDTO.getPassword())) {
            if (loginDTO.getUsername().length() > 30) {
                return Result.fail("登录失败!用户名不能超过30个字符!", 401);
            }
            if (loginDTO.getPassword().length() > 32) {
                return Result.fail("登录失败!密码不能超过32个字符!", 401);
            }
            return userService.checkUser(loginDTO.getUsername(), loginDTO.getPassword(), request, response);
        }
        return Result.fail("登录失败!用户名或密码为空!", 401);
    }

    @GetMapping("/getPermissions")
    public Result getPermissions(HttpServletRequest request) {
        return userService.getPermissions(request);
    }

    @RequestMapping(value = "/getPublicKey", method = RequestMethod.GET)
    public Result getPublicKey(HttpServletRequest request) {
        if (setting.isLoginAndRegisterUseEncrypt())
            return userService.getPublicKey(NetworkUtils.getClientIp(request));
        else
            return Result.success("NONE");
    }

    @RequestMapping("/logout")
    public Result logout(HttpServletRequest request, HttpServletResponse response) {
        return userService.logout(request, response);
    }
}
