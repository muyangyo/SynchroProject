import axios from 'axios'

const request = axios.create({                 // 创建axios实例
    baseURL: "http://localhost:3000/api/", // 请求地址
    timeout: 5000                              // 请求超时时间
})

// 请求拦截器
request.interceptors.request.use(config => {
    // 在发送请求之前做某事
    return config
}, error => {
    // 请求错误时做些事
    return Promise.reject(error)
})

// 响应拦截器
request.interceptors.response.use(response => {
    // 对响应数据做点什么
    return response
}, error => {
    // 请求错误时做些事
    return Promise.reject(error)
})

export default request                     // 导出axios请求对象