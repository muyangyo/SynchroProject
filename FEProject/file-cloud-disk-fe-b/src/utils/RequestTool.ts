import axios, { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse, AxiosRequestConfig } from 'axios'
import { config } from "@/GlobalConfig"
import type { ApiResponse, ApiErrorResponse } from '@/types/response'
import type { HttpMethod } from '@/types/http'
import type { RequestConfig, RequestValidation } from '@/types/request'

export const RequestMethods: Record<string, HttpMethod> = {
  GET: 'GET',
  POST: 'POST',
  PUT: 'PUT',
  DELETE: 'DELETE'
} as const

type RequestMethod = typeof RequestMethods[keyof typeof RequestMethods]

interface RequestOptions extends Omit<RequestConfig, keyof AxiosRequestConfig | 'method' | 'url'> {
  method: RequestMethod
  url: string
}

const requestTool: AxiosInstance = axios.create({
  baseURL: config.hostUrl,
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8',
    'Accept': 'application/json;charset=UTF-8'
  }
})

requestTool.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    console.log('发出的请求为:', config)
    return config
  },
  (error: unknown) => {
    console.error('请求发出时错误:', error)
    return Promise.reject(error)
  }
)

requestTool.interceptors.response.use(
  <T>(response: AxiosResponse<ApiResponse<T>>) => {
    console.log('收到的响应为:', response.data)
    if (response.data.statusCode !== 'SUCCESS') {
      const error = response.data as ApiErrorResponse
      throw new Error(error.errMsg)
    }
    return response.data
  },
  (error: unknown) => {
    if (axios.isAxiosError(error) && error.response) {
      console.error('请求返回时错误:', error.response.status, error.response.data)
    } else {
      console.error('网络错误:', error)
    }
    return Promise.reject(error)
  }
)

const validateRequestParams = ({
  method,
  relativeURL,
  data,
  checkDataFormat = true
}: RequestValidation): void => {
  if (!Object.values(RequestMethods).includes(method)) {
    throw new Error(`无效的请求方法: ${method}. 只能是 ${Object.values(RequestMethods).join(', ')} 中的一种.`)
  }

  if (typeof relativeURL !== 'string') {
    throw new Error(`无效的URL: ${relativeURL}. 它必须是一个字符串.`)
  }

  if (checkDataFormat) {
    if (typeof data !== 'object' && typeof data !== 'string' && !(data instanceof FormData)) {
      throw new Error(`无效的数据: ${data}. 它必须是一个对象、FormData对象或JSON字符串.`)
    }
  }
}

export const easyRequest = async <T>(
  method: RequestMethod,
  relativeURL: string,
  data: unknown,
  checkDataFormat = true,
  autoTransformToJSON = false,
  timeout = 5000
): Promise<ApiResponse<T>> => {
  if (autoTransformToJSON && typeof data === 'object') {
    data = JSON.stringify(data)
  }
  
  validateRequestParams({ method, relativeURL, data, checkDataFormat })

  return requestTool({
    method,
    url: relativeURL,
    data,
    timeout
  })
}

export const optionalRequest = async <T>(
  options: RequestOptions
): Promise<ApiResponse<T>> => {
  const {
    method,
    url: relativeURL,
    data,
    params,
    dataType = 'json',
    showError = true,
    errorCallback,
    checkDataFormat = false,
    responseType = 'json',
    timeout = 5000
  } = options

  validateRequestParams({ method, relativeURL, data, checkDataFormat })

  const headers = dataType === 'file'
    ? { 'Content-Type': 'multipart/form-data' }
    : { 'Content-Type': 'application/json;charset=UTF-8' }

  try {
    return await requestTool({
      method,
      url: relativeURL,
      data,
      params,
      headers,
      responseType,
      timeout
    })
  } catch (error) {
    if (showError && errorCallback && error instanceof Error) {
      errorCallback(error.message)
    }
    return Promise.reject(error)
  }
}

export default requestTool 