import {defineStore} from 'pinia';
import {ref} from 'vue';

export const ROLES = {
    admin: "admin",
    user: "user"
};

export const useUserStore = defineStore('userStore', () => {
    const userName = ref(getFromLocalStorage('userName') || "");
    const isLoggedIn = ref(getFromLocalStorage('isLoggedIn') || false);

    function getFromLocalStorage(key) {
        return localStorage.getItem(key);
    }

    function setToLocalStorage(key, value) {
        localStorage.setItem(key, value);
    }

    const setUserName = (name) => {
        userName.value = name;
        setToLocalStorage('userName', name);
    };

    const getUserName = () => {
        console.warn("userName.value:" + userName.value);
        return userName.value === "" ? "unknown" : userName.value;
    };

    const setLoginStatus = (value, role) => {
        isLoggedIn.value = value;
        setToLocalStorage('isLoggedIn', value);
        setToLocalStorage('role', role);
    };

    const IsLoggedIn = () => {
        console.warn("是否登录:" + isLoggedIn.value);
        return isLoggedIn.value;
    };

    const logout = () => {
        setLoginStatus(false, "");
        setUserName("");
    };

    return {
        setLoginStatus,
        IsLoggedIn,
        setUserName,
        getUserName,
        logout
    };
});