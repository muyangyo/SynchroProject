import type { AxiosRequestConfig } from 'axios'
import type { ApiResponse } from './response'
import type { RequestConfig } from './request'

declare module 'axios' {
  export interface AxiosRequestConfig extends Omit<RequestConfig, keyof AxiosRequestConfig> {}

  export interface AxiosResponse<T = unknown> {
    data: ApiResponse<T>
    status: number
    statusText: string
    headers: Record<string, string>
    config: AxiosRequestConfig
    request?: XMLHttpRequest
  }
} 