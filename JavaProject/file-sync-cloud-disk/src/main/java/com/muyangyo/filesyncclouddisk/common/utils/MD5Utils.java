package com.muyangyo.filesyncclouddisk.common.utils;

import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/2/25
 * Time: 12:45
 */
public class MD5Utils {
    /**
     * 加密密码
     *
     * @param password 用户输入原始的密码
     * @return 加密后的密文
     */
    public static String encipher(String password) {
        //生成随机盐值
        String salt = UUID.randomUUID().toString().replace("-", "");//生成长度为 32 的字符串
        //追加生成密文 (salt+password)
        String baseKey = DigestUtils.md5DigestAsHex((salt + password).getBytes());//生成长度为 32 的字符串
        //存入密文
        return salt + baseKey;// 总共 64 位字符串
    }

    /**
     * 验证密码是否正确
     *
     * @param password    用户输入的密码
     * @param sqlPassword 数据库中的密码
     * @return 验证结果
     */
    public static boolean verify(String password, String sqlPassword) {
        //取出盐值
        String salt = sqlPassword.substring(0, 32);

        //追加生成待验证密文
        String baseKey = DigestUtils.md5DigestAsHex((salt + password).getBytes());
        String key = salt + baseKey;

        return key.equals(sqlPassword);
    }
}
