package com.muyangyo.fileclouddisk.manager.controller;

import com.muyangyo.fileclouddisk.common.config.Setting;
import com.muyangyo.fileclouddisk.common.model.dto.UserDTO;
import com.muyangyo.fileclouddisk.common.model.other.Result;
import com.muyangyo.fileclouddisk.common.utils.NetworkUtils;
import com.muyangyo.fileclouddisk.manager.service.UserManagerService;
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
@RequestMapping("/userManager")
public class UserManagerController {
    @Resource
    private UserManagerService userManagerService;
    @Resource
    private Setting setting;

    @GetMapping("/getUserList")
    public Result getUserList() {
        return userManagerService.getUserList();
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
            return userManagerService.createUser(userDTO.getUsername(), userDTO.getPassword(), userDTO.getPermissions());
        }
        return Result.fail("创建新用户失败!用户名或密码为空!", 401);
    }

    @DeleteMapping("/deleteUser")
    public Result deleteUser(@RequestParam("username") String username) {
        return userManagerService.deleteUser(username);
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
            return userManagerService.updateUser(userDTO.getUsername(), userDTO.getPassword(), userDTO.getPermissions());
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
