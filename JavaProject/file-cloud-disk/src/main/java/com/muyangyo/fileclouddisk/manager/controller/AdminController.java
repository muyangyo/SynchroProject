package com.muyangyo.fileclouddisk.manager.controller;

import com.muyangyo.fileclouddisk.common.aspect.annotations.LocalOperation;
import com.muyangyo.fileclouddisk.common.config.Setting;
import com.muyangyo.fileclouddisk.common.model.dto.LoginDTO;
import com.muyangyo.fileclouddisk.common.model.other.Result;
import com.muyangyo.fileclouddisk.common.utils.NetworkUtils;
import com.muyangyo.fileclouddisk.manager.service.AdminService;
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
 * Date: 2024/12/14
 * Time: 15:34
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @Resource
    private Setting setting;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @LocalOperation
    public Result login(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        if (setting.isLoginAndRegisterUseEncrypt()) {
            adminService.decryptLogin(loginDTO, NetworkUtils.getClientIp(request));// 解密登录信息
        }
        if (setting.isLocalOperationOnly()) {
            if (!NetworkUtils.isLocalhost(request)) {
                return Result.fail("登录失败!请使用局域网登录!", 401);
            }
        }

        // 有密钥则创建新用户
        if (StringUtils.hasLength(loginDTO.getKey())) {
            if (loginDTO.getUsername().length() > 30) {
                return Result.fail("注册失败!用户名不能超过30个字符!", 401);
            }
            if (loginDTO.getPassword().length() > 32) {
                return Result.fail("注册失败!密码不能超过32个字符!", 401);
            }
            return adminService.createUser(loginDTO, request, response);
        }

        // 无密钥则登录
        if (StringUtils.hasLength(loginDTO.getUsername()) && StringUtils.hasLength(loginDTO.getPassword())) {
            if (loginDTO.getUsername().length() > 30) {
                return Result.fail("登录失败!用户名不能超过30个字符!", 401);
            }
            if (loginDTO.getPassword().length() > 32) {
                return Result.fail("登录失败!密码不能超过32个字符!", 401);
            }
            return adminService.checkUser(loginDTO.getUsername(), loginDTO.getPassword(), request, response);
        }
        return Result.fail("登录失败!用户名或密码为空!", 401);

    }

    @RequestMapping(value = "/getPublicKey", method = RequestMethod.GET)
    @LocalOperation
    public Result getPublicKey(HttpServletRequest request) {
        if (setting.isLoginAndRegisterUseEncrypt())
            return adminService.getPublicKey(NetworkUtils.getClientIp(request));
        else
            return Result.success("NONE");
    }

    @RequestMapping(value = "/remoteOperationIsOpen", method = RequestMethod.GET)
    public Result remoteOperationIsOpen(HttpServletRequest request) {
        return Result.success(!setting.isLocalOperationOnly());
    }

    @RequestMapping("/logout")
    public Result logout(HttpServletRequest request, HttpServletResponse response) {
        return adminService.logout(request, response);
    }
}
