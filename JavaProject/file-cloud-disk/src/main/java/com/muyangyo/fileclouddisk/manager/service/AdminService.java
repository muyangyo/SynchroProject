package com.muyangyo.fileclouddisk.manager.service;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.muyangyo.fileclouddisk.common.config.Setting;
import com.muyangyo.fileclouddisk.common.exception.IllegalLoginWithoutRSA;
import com.muyangyo.fileclouddisk.common.model.dto.LoginDTO;
import com.muyangyo.fileclouddisk.common.model.enums.Roles;
import com.muyangyo.fileclouddisk.common.model.meta.Admin;
import com.muyangyo.fileclouddisk.common.model.other.Result;
import com.muyangyo.fileclouddisk.common.utils.EasyTimedCache;
import com.muyangyo.fileclouddisk.common.utils.MD5Utils;
import com.muyangyo.fileclouddisk.common.utils.NetworkUtils;
import com.muyangyo.fileclouddisk.common.utils.TokenUtils;
import com.muyangyo.fileclouddisk.manager.mapper.AdminMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.PrivateKey;
import java.util.Date;

@Service
@Slf4j
public class AdminService {

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private Setting setting;

    /**
     * 验证用户名和密码是否匹配
     *
     * @param username 用户名
     * @param password 密码
     * @param request
     * @return 验证结果，true表示匹配，false表示不匹配
     */
    public Result checkUser(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        Admin admin = adminMapper.selectByUsername(username);
        if (admin != null && MD5Utils.verify(password, admin.getPassword())) {
            // 更新登录时间
            admin.setLastLoginTime(new Date());
            adminMapper.updateByUserId(admin);

            generateAndSetCookies(admin, request, response, "admin");

            return Result.success(true);
        } else {
            return Result.fail("用户名或密码错误");
        }

    }

    public void decryptLogin(LoginDTO loginDTO, String ip) {
        EasyTimedCache<String, PrivateKey> rasCache = setting.getRasCache();
        PrivateKey privateKey = rasCache.get(ip); // 根据IP获取私钥
        // 三次解密
        RSA rsa = SecureUtil.rsa();
        rsa.setPrivateKey(privateKey);
        try {
            loginDTO.setUsername(rsa.decryptStr(loginDTO.getUsername(), KeyType.PrivateKey));
            loginDTO.setPassword(rsa.decryptStr(loginDTO.getPassword(), KeyType.PrivateKey));
            if (StringUtils.hasLength(loginDTO.getKey())) {
                loginDTO.setKey(rsa.decryptStr(loginDTO.getKey(), KeyType.PrivateKey));
            }
        } catch (Exception e) {
            throw new IllegalLoginWithoutRSA("登录信息解密失败，请检查RSA私钥是否正确！");
        }
        log.info("登录信息解密成功!");
    }

    public Result createUser(LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        if (!loginDTO.getKey().equals(setting.getInvitationCode())) {
            return Result.fail("邀请码错误");
        }

        Admin admin = adminMapper.selectByUsername(loginDTO.getUsername());
        if (admin != null) {
            return Result.fail("用户名已存在");
        }
        String encipheredPassword = MD5Utils.encipher(loginDTO.getPassword());
        int i = adminMapper.numberOfAdmin() + 1;
        admin = new Admin(Integer.toString(i), loginDTO.getUsername(), encipheredPassword, new Date(), new Date());
        adminMapper.insertByDynamicCondition(admin);
        generateAndSetCookies(admin, request, response, String.valueOf(Roles.ADMIN));
        return Result.success(true);
    }

    /**
     * 生成token并设置cookie
     *
     * @param admin    用户信息
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param role     用户角色
     */
    private void generateAndSetCookies(Admin admin, HttpServletRequest request, HttpServletResponse response, String role) {
        // 生成token并设置cookie
        TokenUtils.TokenLoad tokenLoad = new TokenUtils.TokenLoad();
        tokenLoad.setUserId(admin.getUserId());
        tokenLoad.setRole(role);
        tokenLoad.setUsername(admin.getUsername());
        tokenLoad.setIp(NetworkUtils.getClientIp(request));
        tokenLoad.setLoginTime(String.valueOf(new Date()));

        String token = TokenUtils.createToken(TokenUtils.TokenLoad.createTokenLoad(tokenLoad));
        Cookie jwtCookie = new Cookie(Setting.TOKEN_HEADER_NAME, token);
        jwtCookie.setHttpOnly(true); // 防止JavaScript访问此Cookie
        jwtCookie.setPath("/"); // 设置Cookie的路径 表示根路径
        response.addCookie(jwtCookie);

        // 给前端验证token是否存在的cookie
        Cookie feCookie = new Cookie(Setting.TOKEN_NAME_FOR_FE, System.currentTimeMillis() + "");
        feCookie.setPath("/");
        feCookie.setHttpOnly(false);
        feCookie.setMaxAge(setting.getTokenLifeTime());
        response.addCookie(feCookie);
    }

    /**
     * 生成公钥和私钥，并缓存私钥
     *
     * @param ip 登录IP
     * @return 公钥
     */
    public Result getPublicKey(String ip) {
        RSA rsa = SecureUtil.rsa();
        EasyTimedCache<String, PrivateKey> rasCache = setting.getRasCache();
        rasCache.put(ip, rsa.getPrivateKey()); // 缓存私钥

        String publicKey = rsa.getPublicKeyBase64(); // 发送公钥给客户端
        return Result.success(publicKey);
    }
}