package com.muyangyo.syncfileclouddisk.utils;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

/**
 * @ClassName SM2Utils
 * @Description SM2算法工具类，用于生成密钥对、加密和解密操作
 */
public class SM2Utils {
    // 获取SM2曲线参数
    private static final X9ECParameters x9ECParameters = GMNamedCurves.getByName("sm2p256v1");
    private static final ECParameterSpec ecDomainParameters = new ECParameterSpec(x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN());

    /**
     * 创建密钥对
     *
     * @return 返回生成的密钥对
     */
    public static KeyPair createKeyPair() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        // 指定SM2曲线参数
        final ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");

        // 创建密钥对生成器
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());
        kpg.initialize(sm2Spec, new SecureRandom());

        return kpg.generateKeyPair();
    }


    /**
     * 获取公钥的十六进制字符串
     *
     * @param keyPair 包含公钥和私钥的密钥对
     * @return 公钥的十六进制字符串
     */
    public static String getPublicKeyString(KeyPair keyPair) {
        // 从密钥对中获取公钥
        PublicKey publicKey = keyPair.getPublic();
        String publicKeyString = "";

        // 如果公钥是BCECPublicKey的实例，则进行编码
        if (publicKey instanceof BCECPublicKey) {
            // 将公钥的点Q转换为十六进制字符串
            publicKeyString = Hex.toHexString(((BCECPublicKey) publicKey).getQ().getEncoded(false));
        }

        // 返回公钥的十六进制字符串
        return publicKeyString;
    }


    /**
     * 获取私钥的十六进制字符串
     *
     * @param keyPair 密钥对
     * @return 返回私钥的十六进制字符串
     */
    public static String getPrivateKeyString(KeyPair keyPair) {
        // 获取密钥对中的私钥
        PrivateKey privateKey = keyPair.getPrivate();
        String privateKeyString = "";

        // 检查私钥是否为BCEC私钥
        if (privateKey instanceof BCECPrivateKey) {
            // 将私钥的D值转换为字节数组并转换为十六进制字符串
            privateKeyString = Hex.toHexString(((BCECPrivateKey) privateKey).getD().toByteArray());
        }
        return privateKeyString; // 返回私钥的字符串表示
    }


    /**
     * 使用公钥的十六进制字符串加密数据
     *
     * @param publicKeyHex 公钥的十六进制字符串
     * @param data         需要加密的数据
     * @return 返回加密后的字符串
     */
    public static String encrypt(String publicKeyHex, String data) {
        return encrypt(getECPublicKeyByPublicKeyHex(publicKeyHex), data, 1);
    }

    /**
     * 使用私钥的十六进制字符串解密数据
     *
     * @param privateKeyHex 私钥的十六进制字符串
     * @param cipherData    密文
     * @return 返回解密后的原始数据
     */
    public static String decrypt(String privateKeyHex, String cipherData) {
        return decrypt(getBCECPrivateKeyByPrivateKeyHex(privateKeyHex), cipherData, 1);
    }

    /**
     * 使用BCECPublicKey对象加密数据
     *
     * @param publicKey BCECPublicKey对象
     * @param data      需要加密的数据
     * @param modeType  加密模式类型
     * @return 返回加密后的字符串
     */
    private static String encrypt(BCECPublicKey publicKey, String data, int modeType) {
        // 设置加密模式
        SM2Engine.Mode mode = (modeType != 1) ? SM2Engine.Mode.C1C2C3 : SM2Engine.Mode.C1C3C2;

        // 获取椭圆曲线参数
        ECParameterSpec ecParameterSpec = publicKey.getParameters();
        ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(),
                ecParameterSpec.getG(), ecParameterSpec.getN());

        // 创建公钥参数对象
        ECPublicKeyParameters ecPublicKeyParameters = new ECPublicKeyParameters(publicKey.getQ(), ecDomainParameters);
        SM2Engine sm2Engine = new SM2Engine(mode);

        // 初始化加密引擎
        sm2Engine.init(true, new ParametersWithRandom(ecPublicKeyParameters, new SecureRandom()));
        byte[] arrayOfBytes = null;

        try {
            byte[] in = data.getBytes(StandardCharsets.UTF_8);
            arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
        } catch (Exception e) {
            System.out.println("SM2加密时出现异常: " + e.getMessage());
            e.printStackTrace();
        }
        return Hex.toHexString(arrayOfBytes); // 返回加密后的十六进制字符串
    }


    /**
     * 使用BCECPrivateKey对象解密数据
     *
     * @param privateKey BCECPrivateKey对象
     * @param cipherData 密文
     * @param modeType   解密模式类型
     * @return 返回解密后的原始数据
     */
    private static String decrypt(BCECPrivateKey privateKey, String cipherData, int modeType) {
        // 设置解密模式
        SM2Engine.Mode mode = (modeType != 1) ? SM2Engine.Mode.C1C2C3 : SM2Engine.Mode.C1C3C2;

        byte[] cipherDataByte = Hex.decode(cipherData);
        ECParameterSpec ecParameterSpec = privateKey.getParameters();
        ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(),
                ecParameterSpec.getG(), ecParameterSpec.getN());

        // 创建私钥参数对象
        ECPrivateKeyParameters ecPrivateKeyParameters = new ECPrivateKeyParameters(privateKey.getD(),
                ecDomainParameters);

        SM2Engine sm2Engine = new SM2Engine(mode);
        sm2Engine.init(false, ecPrivateKeyParameters); // 初始化解密引擎
        String result = null;

        try {
            byte[] arrayOfBytes = sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length);
            result = new String(arrayOfBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("SM2解密时出现异常: " + e.getMessage());
        }
        return result; // 返回解密后的数据
    }


    /**
     * 根据十六进制字符串获取BCECPublicKey公钥对象
     *
     * @param pubKeyHex 公钥的十六进制字符串
     * @return 返回BCECPublicKey对象
     */
    private static BCECPublicKey getECPublicKeyByPublicKeyHex(String pubKeyHex) {

        // 确保公钥字符串长度符合要求
        if (pubKeyHex.length() > 128) {
            pubKeyHex = pubKeyHex.substring(pubKeyHex.length() - 128);
        }
        String stringX = pubKeyHex.substring(0, 64); // 获取X坐标
        String stringY = pubKeyHex.substring(stringX.length()); // 获取Y坐标
        BigInteger x = new BigInteger(stringX, 16);
        BigInteger y = new BigInteger(stringY, 16);

        // 创建ECPublicKeySpec对象
        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(x9ECParameters.getCurve().createPoint(x, y), ecDomainParameters);
        return new BCECPublicKey("EC", ecPublicKeySpec, BouncyCastleProvider.CONFIGURATION);
    }

    /**
     * 根据十六进制字符串获取BCECPrivateKey私钥对象
     *
     * @param privateKeyHex 私钥的十六进制字符串
     * @return 返回BCECPrivateKey对象
     */
    private static BCECPrivateKey getBCECPrivateKeyByPrivateKeyHex(String privateKeyHex) {
        BigInteger d = new BigInteger(privateKeyHex, 16);
        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(d, ecDomainParameters);
        return new BCECPrivateKey("EC", ecPrivateKeySpec, BouncyCastleProvider.CONFIGURATION);
    }
}