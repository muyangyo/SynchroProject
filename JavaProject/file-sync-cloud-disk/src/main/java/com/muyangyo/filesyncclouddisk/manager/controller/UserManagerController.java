package com.muyangyo.filesyncclouddisk.manager.controller;

import com.muyangyo.filesyncclouddisk.common.aspect.annotations.AdminRequired;
import com.muyangyo.filesyncclouddisk.common.config.Setting;
import com.muyangyo.filesyncclouddisk.common.model.dto.UserDTO;
import com.muyangyo.filesyncclouddisk.common.model.other.Result;
import com.muyangyo.filesyncclouddisk.common.utils.NetworkUtils;
import com.muyangyo.filesyncclouddisk.manager.service.UserManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/12/17
 * Time: 19:01
 */
@RestController
@Slf4j
@RequestMapping("/api/userManager")
@AdminRequired
public class UserManagerController {
    @Resource
    private UserManagerService userManagerService;
    @Resource
    private Setting setting;

    @GetMapping("/getUserList")
    public Result getUserList(HttpServletRequest request) {
        return userManagerService.getUserList(request);
    }

    @PostMapping("/addUser")
    public Result addUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        if (setting.isLoginAndRegisterUseEncrypt()) {
            userManagerService.decryptLogin(userDTO, NetworkUtils.getClientIp(request));// 解密登录信息
        }

        if (StringUtils.hasLength(userDTO.getUsername()) && StringUtils.hasLength(userDTO.getPassword())) {
            if (userDTO.getUsername().length() > 30) {
                return Result.fail("创建新用户失败!用户名不能超过30个字符!", 401);
            }
            if (userDTO.getPassword().length() > 32) {
                return Result.fail("创建新用户失败!密码不能超过32个字符!", 401);
            }
            return userManagerService.createUser(userDTO.getUsername(), userDTO.getPassword(), userDTO.getPermissions(), request);
        }
        return Result.fail("创建新用户失败!用户名或密码为空!", 401);
    }

    @DeleteMapping("/deleteUser")
    public Result deleteUser(@RequestParam("username") String username, HttpServletRequest request) {
        return userManagerService.deleteUser(username, request);
    }

    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        if (setting.isLoginAndRegisterUseEncrypt()) {
            userManagerService.decryptLogin(userDTO, NetworkUtils.getClientIp(request));// 解密登录信息
        }

        if (StringUtils.hasLength(userDTO.getUsername())) {

            if (StringUtils.hasLength(userDTO.getPassword()) && userDTO.getPassword().length() > 32) {
                return Result.fail("更新用户失败!密码不能超过32个字符!", 401);
            }
            return userManagerService.updateUser(userDTO.getUsername(), userDTO.getPassword(), userDTO.getPermissions(), request);
        }
        return Result.fail("更新用户失败!用户名为空!", 401);
    }


    @RequestMapping(value = "/getPublicKey", method = RequestMethod.GET)
    public Result getPublicKey(HttpServletRequest request) {
        if (setting.isLoginAndRegisterUseEncrypt())
            return userManagerService.getPublicKey(NetworkUtils.getClientIp(request));
        else
            return Result.success("NONE");
    }

}
