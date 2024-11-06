import axios from 'axios'
import {config} from "@/customizeGlobalConfigurations.js";

// 创建axios实例并设置基础URL和请求超时
const requestTool = axios.create({
    baseURL: config.baseUrl,
    timeout: 5000
})

// 请求拦截器
requestTool.interceptors.request.use(config => {
    // 在请求发送前处理请求配置
    console.log(config);
    return config
}, error => {
    // 处理请求错误
    console.log(error);
    return Promise.reject(error)
})

// 响应拦截器
requestTool.interceptors.response.use(response => {
    // 处理响应数据
    console.log(response);
    return response;
}, error => {
    // 处理响应错误
    console.log(error);
    return Promise.reject(error)
})

// 封装通用请求方法
const easyRequest = (method, relativeURL, data) => {
    // 限定method的值
    const validMethods = ['GET', 'POST', 'PUT', 'DELETE'];
    if (!validMethods.includes(method)) {
        throw new Error(`无效的请求方法: ${method}. 只能是 ${validMethods.join(', ')} 中的一种.`);
    }

    // 限定url必须是字符串
    if (typeof relativeURL !== 'string') {
        throw new Error(`无效的URL: ${relativeURL}. 它必须是一个字符串.`);
    }

    // 限定data为对象或JSON字符串
    if (typeof data !== 'object' && typeof data !== 'string') {
        throw new Error(`无效的数据: ${data}. 它必须是一个对象或JSON字符串.`);
    }

    // 发送请求并返回结果
    return requestTool({
        method: method,
        url: relativeURL,
        data: data
    });
}

export {easyRequest}// 导出请求方法
export default requestTool // 导出axios请求对象