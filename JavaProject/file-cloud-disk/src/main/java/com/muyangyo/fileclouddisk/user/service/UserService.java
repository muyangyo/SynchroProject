package com.muyangyo.fileclouddisk.user.service;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.muyangyo.fileclouddisk.common.config.Setting;
import com.muyangyo.fileclouddisk.common.exception.IllegalLoginWithoutRSA;
import com.muyangyo.fileclouddisk.common.model.dto.LoginDTO;
import com.muyangyo.fileclouddisk.common.model.meta.User;
import com.muyangyo.fileclouddisk.common.model.other.Result;
import com.muyangyo.fileclouddisk.common.utils.EasyTimedCache;
import com.muyangyo.fileclouddisk.common.utils.MD5Utils;
import com.muyangyo.fileclouddisk.common.utils.NetworkUtils;
import com.muyangyo.fileclouddisk.common.utils.TokenUtils;
import com.muyangyo.fileclouddisk.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.PrivateKey;
import java.util.Date;

/**
 * UserService 服务层，处理用户相关的业务逻辑。
 */
@Service
@Slf4j
public class UserService {

    @Resource
    private UserMapper userMapper;

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
        User user = userMapper.selectByUsername(username);
        if (user != null && MD5Utils.verify(password, user.getPassword())) {
            // 更新登录时间
            user.setLastLoginTime(new Date());
            userMapper.updateByUserId(user);

            //生成token并设置cookie
            TokenUtils.TokenLoad tokenLoad = new TokenUtils.TokenLoad();
            tokenLoad.setUserId(user.getUserId());
            tokenLoad.setRole("user");
            tokenLoad.setUsername(user.getUsername());
            tokenLoad.setIp(NetworkUtils.getClientIp(request));
            tokenLoad.setLoginTime(String.valueOf(new Date()));

            String token = TokenUtils.createToken(TokenUtils.TokenLoad.createTokenLoad(tokenLoad));
            Cookie jwtCookie = new Cookie(Setting.TOKEN_HEADER_NAME, token);
            jwtCookie.setHttpOnly(true); // 防止JavaScript访问此Cookie
            jwtCookie.setPath("/"); // 设置Cookie的路径 表示根路径，意味着这个 Cookie 会在整个域名下的所有路径中被发送
            response.addCookie(jwtCookie);


            Cookie cookie = new Cookie(Setting.TOKEN_NAME_FOR_FE, System.currentTimeMillis() + "");
            cookie.setPath("/");
            cookie.setHttpOnly(false);
            cookie.setMaxAge(setting.getTokenLifeTime());
            response.addCookie(cookie);// 给前端验证token是否存在的

            return Result.success(true);
        } else {
            return Result.fail("用户名或密码错误");
        }

    }


    /**
     * 创建用户
     *
     * @param username 用户名
     * @param password 密码
     * @param key      邀请码
     * @return 创建结果
     */
    public Result createUser(String username, String password, String key) { //TODO 这个方法以后只能由管理员调用，需要权限控制
        if (!key.equals("123456")) {
            return Result.fail("邀请码错误");
        }

        String permissions = "rwd";// TODO 这里应该调用权限服务，获取用户权限
        // 1. n: 没有权限 2. r:读权限 3. w:写权限 4. d:删除权限

        User user = userMapper.selectByUsername(username); // 判断用户名是否存在
        if (user == null) {
            String encipher = MD5Utils.encipher(password);
            Integer i = userMapper.numberOfUser() + 1;
            user = new User(i.toString(), username, encipher, new Date(), new Date(), 0, permissions);
            userMapper.insertByDynamicCondition(user);
            return Result.success(true);
        } else {
            return Result.fail("用户名已存在");
        }
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

    /**
     * 解密Login信息
     *
     * @param loginDTO 登录信息
     * @param ip       登录IP
     */
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
}