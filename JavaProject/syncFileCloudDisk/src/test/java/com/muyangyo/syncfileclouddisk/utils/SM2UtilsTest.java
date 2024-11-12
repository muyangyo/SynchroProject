package com.muyangyo.syncfileclouddisk.utils;

import org.junit.jupiter.api.Test;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/12
 * Time: 15:57
 */
class SM2UtilsTest {

    @Test
    void check() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        KeyPair keyPair = SM2Utils.createKeyPair();
        String publicKey = SM2Utils.getPublicKeyString(keyPair);
        String privateKey = SM2Utils.getPrivateKeyString(keyPair);

        assertTrue(publicKey != null && privateKey != null);
        System.out.println("公钥: " + publicKey + ", 私钥: " + privateKey);

        String data = "hello world 你好 123 @#$%^&*()_+";
        String encryptString = SM2Utils.encrypt(publicKey, data);
        String decrypt = SM2Utils.decrypt(privateKey, encryptString);
        assertEquals(data, decrypt);
    }

    @Test
    void check2() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        KeyPair keyPair = SM2Utils.createKeyPair();
        String publicKey = SM2Utils.getPublicKeyString(keyPair);
        String privateKey = SM2Utils.getPrivateKeyString(keyPair);

        System.out.println("公钥: " + publicKey + ", 私钥: " + privateKey);
    }

    @Test
    void check3() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        String decrypt = SM2Utils.decrypt("2728c22d98241e9fa264a351832a737795a69f226b670d4e85cac88546d0cdd6",
                "67131b2aedd3ab41d797426f02a8abff152b41e1c578c97eb9cb266dfe60a601051d04b23a23726dd9f603d7659a82d85f0f39e54812f3cbd27b3ec6a8b030ade79c1277c7b6ceccfa9d040c43c03dde85875b780ff861315de07094c9d74130120781");
        System.out.println(decrypt);
    }

}
