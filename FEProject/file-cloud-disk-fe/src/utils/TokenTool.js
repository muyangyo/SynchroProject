// 这个文件在本项目中未被使用。
// 它被保留在这里以供参考。
// 本地存储token
let tokenTool = "";
export const setToken = (token) => {
    localStorage.setItem("token", token); // 设置本地存储的token
}

export const getToken = () => {
    return localStorage.getItem("token"); // 获取本地存储的token
}
export const removeToken = () => {
    localStorage.removeItem("token"); // 移除本地存储的token
}
