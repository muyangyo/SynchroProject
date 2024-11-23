import {defineStore} from 'pinia';
import {ref} from 'vue';
import JSEncrypt from 'jsencrypt';

export const useKeyStore = defineStore('keyStore', () => {
    const publicKey = ref("");

    const setPublicKey = (key) => {
        if (!key || typeof key !== 'string') {
            throw new Error("公钥应为有效的非空字符串");
        }
        publicKey.value = key;
    };

    const encryptData = (data) => {
        if (!publicKey.value) {
            throw new Error("请先设置公钥!");
        }

        const encryptor = new JSEncrypt();
        encryptor.setPublicKey(publicKey.value);
        return encryptor.encrypt(data);
    };

    return {
        setPublicKey,
        encryptData,
    };
});
