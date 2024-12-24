import JSEncrypt from 'jsencrypt';

// RSA加密工具类
class RSAEncryptUtil {
    constructor() {
        this.publicKey = ""; // 公钥
        this.openEncrypt = true; // 是否开启加密
    }

    // 设置公钥
    setPublicKey(key) {
        if (key === "NONE") {
            console.warn("未开启加密功能,请注意网络环境");
            this.openEncrypt = false;
            return;
        }

        if (!key || typeof key !== 'string') {
            throw new Error("公钥应为有效的非空字符串");
        }
        this.publicKey = key;
    }

    // 加密数据
    encryptData(data) {
        if (this.openEncrypt === true) { // 开启加密
            if (!this.publicKey) {
                throw new Error("请先设置公钥!");
            }

            const encryptor = new JSEncrypt();
            encryptor.setPublicKey(this.publicKey);
            return encryptor.encrypt(data);
        } else {
            return data;
        }
    }
}

// 导出单例实例
export const rsaEncryptUtil = new RSAEncryptUtil();