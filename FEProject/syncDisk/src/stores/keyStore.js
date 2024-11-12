import {defineStore} from 'pinia';
import {ref} from 'vue';
import {sm2} from 'sm-crypto';

export const useKeyStore = defineStore('keyStore', () => {
    const publicKey = ref("");

    const setPublicKey = (key) => {
        publicKey.value = key;
    }

    const encryptData = (data) => {
        if (!publicKey.value) {
            throw new Error("请先设置公钥!");
        }

        // 确保加密模式一致
        return sm2.doEncrypt(data, publicKey.value, 1);
    }

    return {
        setPublicKey, encryptData,
    }
});