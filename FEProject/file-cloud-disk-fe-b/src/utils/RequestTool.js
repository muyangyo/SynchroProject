import axios from 'axios';
import {config} from "@/GlobalConfig.js";

// 请求方法
const RequestMethods = {
    GET: 'GET', POST: 'POST', PUT: 'PUT', DELETE: 'DELETE',
};

// 创建axios实例并设置基础URL和请求超时
const requestTool = axios.create({
    baseURL: config.hostUrl,
    timeout: 5000, // 默认超时时间
    headers: { // 设置默认请求头
        'Content-Type': 'application/json;charset=UTF-8',
        'Accept': 'application/json;charset=UTF-8'
    }
});

// 请求拦截器
requestTool.interceptors.request.use(config => {
    // 在请求发送前处理请求配置
    console.log('发出的请求为:', config);
    return config;
}, error => {
    // 处理请求错误
    console.error('请求发出时错误:', error);
    return Promise.reject(error);
});

// 响应拦截器
requestTool.interceptors.response.use(response => {
    // 处理响应数据
    console.log('收到的响应为:', response.data);
    return response.data;
}, error => {
    // 处理响应错误
    if (error.response) {
        console.error('请求返回时错误:', error.response.status, error.response.data);
    } else {
        console.error('网络错误:', error.message);
    }
    return Promise.reject(error);
});

/**
 * 验证请求参数
 * @param {string} method 请求方法
 * @param {string} relativeURL 请求URL
 * @param {object|string} data 请求数据
 * @param checkDataFormat 是否检查请求数据格式
 */
const validateRequestParams = ({method, relativeURL, data, checkDataFormat = true}) => {
    if (!Object.values(RequestMethods).includes(method)) {
        throw new Error(`无效的请求方法: ${method}. 只能是 ${Object.values(RequestMethods).join(', ')} 中的一种.`);
    }

    // 限定url必须是字符串
    if (typeof relativeURL !== 'string') {
        throw new Error(`无效的URL: ${relativeURL}. 它必须是一个字符串.`);
    }

    if (checkDataFormat) {
        if (typeof data !== 'object' && typeof data !== 'string' && !(data instanceof FormData)) {
            throw new Error(`无效的数据: ${data}. 它必须是一个对象、FormData对象或JSON字符串.`);
        }
    }
};

// 封装通用请求方法
const easyRequest = async (method, relativeURL, data, checkDataFormat = true, autoTransformToJSON = false, timeout = 5000) => {
    // 自动转换请求数据为JSON字符串
    if (autoTransformToJSON && typeof data !== 'string') {
        data = JSON.stringify(data);
    }
    validateRequestParams({method, relativeURL, data, checkDataFormat});

    // 发送请求并返回结果
    return requestTool({
        method,
        url: relativeURL,
        data,
        timeout // 设置超时时间
    });
};

// 封装支持自定义选项的请求方法
const optionalRequest = async (options) => {
    const {
        method, // 请求方法
        url: relativeURL, // 请求URL
        data, // 请求数据
        params, // 请求参数
        dataType = 'json', // 默认请求数据类型
        showError = true, // 是否显示错误信息
        errorCallback, // 错误回调函数
        checkDataFormat = false, // 是否检查data数据的格式
        responseType = 'json', // 默认响应数据类型
        timeout = 5000 // 默认超时时间
    } = options;

    validateRequestParams({method, relativeURL, data, checkDataFormat});

    const headers = dataType === 'file'
        ? {'Content-Type': 'multipart/form-data'}
        : {'Content-Type': 'application/json;charset=UTF-8'};

    // 发送请求并返回结果
    return requestTool({
        method,
        url: relativeURL,
        data,
        params,
        headers,
        responseType,
        timeout // 设置超时时间
    }).then(response => {
        return response;
    }).catch(error => {
        if (showError && errorCallback) {
            errorCallback(error.message);
        }
        return Promise.reject(error);
    });
};

/**
 * 使用实例
 * optionalRequest({
 *     method: 'POST',
 *     url: "/file/uploadFile",
 *     data: {
 *         file: chunkFile,
 *         fileName: file.name,
 *         fileMd5: currentFile.md5,
 *         chunkIndex: i,
 *         chunks: chunks,
 *         fileId: currentFile.fileId,
 *         filePid: currentFile.filePid,
 *     },
 *     dataType: "file",
 *     showError: true,
 *     errorCallback: (errorMsg) => {
 *          console.log(errorMsg);
 *     },
 * });
 */

export {easyRequest, optionalRequest, RequestMethods}; // 导出请求方法
export default requestTool; // 导出axios请求对象