import { defineStore } from 'pinia'
import { ref } from 'vue'
import JSEncrypt from 'jsencrypt'

export const useKeyStore = defineStore('keyStore', () => {
  const publicKey = ref<string>("")
  const openEncrypt = ref<boolean>(true)

  const setPublicKey = (key: string): void => {
    if (key === "NONE") {
      console.warn("未开启加密功能,请注意网络环境")
      openEncrypt.value = false
      return
    }

    if (!key || typeof key !== 'string') {
      throw new Error("公钥应为有效的非空字符串")
    }
    publicKey.value = key
  }

  const encryptData = (data: string): string => {
    if (openEncrypt.value === true) {
      if (!publicKey.value) {
        throw new Error("请先设置公钥!")
      }

      const encryptor = new JSEncrypt()
      encryptor.setPublicKey(publicKey.value)
      const encrypted = encryptor.encrypt(data)
      if (!encrypted) {
        throw new Error("加密失败!")
      }
      return encrypted
    } else {
      return data
    }
  }

  return {
    setPublicKey,
    encryptData,
  }
}) 