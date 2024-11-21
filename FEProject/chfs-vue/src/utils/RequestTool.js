import axios from 'axios';
// 请求方法
const RequestMethods = {
    GET: 'GET', POST: 'POST', PUT: 'PUT', DELETE: 'DELETE',
};

// 创建axios实例并设置基础URL和请求超时
const requestTool = axios.create({
    baseURL: "http://127.0.0.1:8080/", timeout: 5000
});

// 请求拦截器
requestTool.interceptors.request.use(config => {
    // 在请求发送前处理请求配置
    console.log(config);
    return config;
}, error => {
    // 处理请求错误
    console.log(error);
    return Promise.reject(error);
});

// 响应拦截器
requestTool.interceptors.response.use(response => {
    // 处理响应数据
    console.log(response);
    return response;
}, error => {
    // 处理响应错误
    console.log(error);
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
    // 限定method的值
    const validMethods = ['GET', 'POST', 'PUT', 'DELETE'];

    if (!Object.values(RequestMethods).includes(method)) {
        throw new Error(`无效的请求方法: ${method}. 只能是 ${validMethods.join(', ')} 中的一种.`);
    }

    // 限定url必须是字符串
    if (typeof relativeURL !== 'string') {
        throw new Error(`无效的URL: ${relativeURL}. 它必须是一个字符串.`);
    }

    if (checkDataFormat) {
        // 限定data为对象或JSON字符串
        if (typeof data !== 'object' && typeof data !== 'string') {
            throw new Error(`无效的数据: ${data}. 它必须是一个对象或JSON字符串.`);
        }
    }
}

// 封装通用请求方法
const easyRequest = (method, relativeURL, data) => {
    validateRequestParams({method, relativeURL, data});

    // 发送请求并返回结果
    return requestTool({
        method: method, url: relativeURL, data: data
    });
};

// 封装支持自定义选项的请求方法
const optionalRequest = (options) => {
    const {
        method, // 请求方法
        url: relativeURL, // 请求URL
        dataType = 'json', // 默认请求数据类型
        data, // 请求数据
        params, // 请求参数
        showError = true, // 是否显示错误信息
        errorCallback, // 错误回调函数
        checkDataFormat = false, // 是否检查data数据的格式
    } = options;

    validateRequestParams({method, relativeURL, data, checkDataFormat});

    // 发送请求并返回结果
    return requestTool({
        method, url: relativeURL, data, params, headers: {
            'Content-Type': dataType === 'file' ? 'multipart/form-data' : 'application/json',
        },
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