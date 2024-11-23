import {defineStore} from 'pinia';
import {ref} from 'vue';

export const useUserStore = defineStore('userStore', () => {
    // 提供共享数据
    const userName = ref(""); // 用户名

    const getUserName = () => {
        console.log("userName.value:" + userName.value)
        return userName.value === "" ? "unknown" : userName.value;
    } // 获取用户名

    const setUserName = (name) => {
        userName.value = name;
    } // 设置用户名

    const isLoggedInBoolean = ref(false);

    const setLoginStatus = (value) => {
        isLoggedInBoolean.value = value;
    } // 设置是否登录

    const IsLoggedIn = () => {
        console.log("是否登录:" + isLoggedInBoolean.value);
        return isLoggedInBoolean.value;
    } // 获取是否登录

    const logout = () => {
        setLoginStatus(false);
        setUserName("");
    } // 退出登录


    // 返回共享数据和修改函数
    return {
        setLoginStatus, IsLoggedIn, setUserName, getUserName, logout
    }
});