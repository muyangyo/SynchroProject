import {defineStore} from 'pinia';
import {ref} from 'vue';

export const useUserStore = defineStore('userStore', () => {
    // 提供共享数据
    const userName = ref("");

    const getUserName = () => {
        console.log("userName.value:" + userName.value)
        return userName.value === "" ? "unknown" : userName.value;
    }
    const setUserName = (name) => {
        userName.value = name;
    }

    const isLoggedIn = ref(true);
    const setIsLoggedIn = (value) => {
        isLoggedIn.value = value;
    }
    const getIsLoggedIn = () => {
        console.log("getIsLoggedIn:" + isLoggedIn.value)
        return isLoggedIn.value;
    }

    const logout = () => {
        setIsLoggedIn(false);
        setUserName("");
    }


    // 返回共享数据和修改函数
    return {
        setIsLoggedIn, getIsLoggedIn, setUserName, getUserName, logout
    }
});