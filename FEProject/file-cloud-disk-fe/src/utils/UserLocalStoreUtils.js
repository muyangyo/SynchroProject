export const ROLES = {
    admin: "admin",
    user: "user"
};

// UserSession 类: 用于管理用户的登录状态、权限、角色等信息
export class UserSession {
    // 从本地存储中获取数据
    static getFromLocalStorage(key) {
        return localStorage.getItem(key);
    }

    //向本地存储中设置数据
    static setToLocalStorage(key, value) {
        localStorage.setItem(key, value);
    }

    // 获取用户名
    static getUserName() {
        const userName = this.getFromLocalStorage('userName') || "";
        return userName === "" ? "unknown" : userName;
    }

    // 设置用户名
    static setUserName(name) {
        this.setToLocalStorage('userName', name);
    }

    // 获取权限
    static getPermissions() {
        return this.getFromLocalStorage('permissions') || "";
    }

    // 设置权限
    static setPermissions(permissions) {
        this.setToLocalStorage('permissions', permissions);
    }

    // 获取角色
    static getRole() {
        return this.getFromLocalStorage('role') || "";
    }

    // 设置角色
    static setRole(role) {
        this.setToLocalStorage('role', role);
    }

    // 检查是否登录
    static isLoggedIn() {
        const isLoggedIn = this.getFromLocalStorage('isLoggedIn');
        return isLoggedIn === 'true';
    }

    // 设置登录状态
    static setLoginStatus(value) {
        this.setToLocalStorage('isLoggedIn', String(value));
    }

    // 登录方法
    static login(role, username, permissions) {
        this.setLoginStatus(true);
        this.setRole(role);
        this.setUserName(username);
        this.setPermissions(permissions);
    }

    // 登出方法
    static logout() {
        this.setLoginStatus(false);
        this.setRole("");
        this.setUserName("");
        this.setPermissions("");
    }
}