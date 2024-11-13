package com.muyangyo.filesyncclouddisk.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import org.junit.jupiter.api.Test;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/12
 * Time: 15:57
 */
class RsaUtilsTest {

    @Test
    void check() {
        // 生成 RSA 密钥对
        RSA rsa = SecureUtil.rsa();

        // 获取公钥和私钥
        String publicKey = rsa.getPublicKeyBase64();
        String privateKey = rsa.getPrivateKeyBase64();

        System.out.println("公钥: " + publicKey);
        System.out.println("私钥: " + privateKey);

        String text = "我是一段测试aaaa !@#@!#!@#!@";

        // 使用公钥加密(前端)
        String encrypted = rsa.encryptBase64(text, KeyType.PublicKey);
        System.out.println("Base64 加密结果: " + encrypted);

        // 使用私钥解密(后端)
        String decryptStr = rsa.decryptStr(encrypted, KeyType.PrivateKey);
        System.out.println("解密后的明文: " + decryptStr);
    }
}
