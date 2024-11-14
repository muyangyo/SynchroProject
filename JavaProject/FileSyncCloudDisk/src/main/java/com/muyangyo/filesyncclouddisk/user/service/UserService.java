package com.muyangyo.filesyncclouddisk.user.service;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.muyangyo.filesyncclouddisk.common.config.Setting;
import com.muyangyo.filesyncclouddisk.common.model.dto.Login;
import com.muyangyo.filesyncclouddisk.common.model.enumeration.CommonSizeUnit;
import com.muyangyo.filesyncclouddisk.common.model.meta.User;
import com.muyangyo.filesyncclouddisk.common.model.other.Result;
import com.muyangyo.filesyncclouddisk.common.utils.EasyTimedCache;
import com.muyangyo.filesyncclouddisk.common.utils.MD5Utils;
import com.muyangyo.filesyncclouddisk.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.PrivateKey;
import java.util.Date;

/**
 * UserService 服务层，处理用户相关的业务逻辑。
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Setting setting;

    /**
     * 验证用户名和密码是否匹配
     *
     * @param username 用户名
     * @param password 密码
     * @return 验证结果，true表示匹配，false表示不匹配
     */
    public Result checkUser(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user != null && MD5Utils.verify(password, user.getPassword())) {
            // 更新登录时间
            user.setLastLoginTime(new Date());
            userMapper.updateByPrimaryKeySelective(user);
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
    public Result createUser(String username, String password, String key) {
        if (!key.equals(setting.getInvitationCode())) {
            return Result.fail("邀请码错误");
        }

        User user = userMapper.selectByUsername(username);
        if (user == null) {
            String encipher = MD5Utils.encipher(password);
            int i = userMapper.numberOfUsers() + 1;
            user = new User(Integer.toString(i), username, encipher, new Date(), new Date(), 1, 0L, CommonSizeUnit.GB.getSize());
            userMapper.insertSelective(user);
            return Result.success(true);
        } else {
            return Result.fail("用户名已存在");
        }
    }

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
     * @param login 登录信息
     * @param ip    登录IP
     */
    public void decryptLogin(Login login, String ip) {
        EasyTimedCache<String, PrivateKey> rasCache = setting.getRasCache();
        PrivateKey privateKey = rasCache.get(ip); // 根据IP获取私钥
        // 三次解密
        RSA rsa = SecureUtil.rsa();
        rsa.setPrivateKey(privateKey);
        login.setUsername(rsa.decryptStr(login.getUsername(), KeyType.PrivateKey));
        login.setPassword(rsa.decryptStr(login.getPassword(), KeyType.PrivateKey));
        if (StringUtils.hasLength(login.getKey())) {
            login.setKey(rsa.decryptStr(login.getKey(), KeyType.PrivateKey));
        }
        log.info("登录信息解密成功!");
    }
}