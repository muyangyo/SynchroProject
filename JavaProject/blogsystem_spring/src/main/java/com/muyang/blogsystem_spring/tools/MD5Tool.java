package com.muyang.blogsystem_spring.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/24
 * Time: 16:13
 */
@Slf4j
public class MD5Tool {
    public static String encipher(String password) {
        //生成随机盐值
        String salt = UUID.randomUUID().toString().replace("-", "");

        //追加,生成 原始密文
        String baseKey = DigestUtils.md5DigestAsHex((salt + password).getBytes());

        //合并(这里我们就直接追加就是,懒得搞那么复杂的加密)
        String key = baseKey + salt;
        return key;
    }

    public static boolean verify(String toVerifiedPassword, String sqlPassword) {
        //得到盐值
        String salt = sqlPassword.substring(32, 64);//由于上面我们是直接追加的,所以我们取出末尾的即可

        //计算预计密文
        String baseKey = DigestUtils.md5DigestAsHex((salt + toVerifiedPassword).getBytes());//必须采用盐值在前
        String preKey = baseKey + salt;

        return preKey.equals(sqlPassword);
    }
}
