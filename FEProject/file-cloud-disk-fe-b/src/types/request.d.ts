import type { HttpMethod } from './http'

export type RequestMethod = HttpMethod

export interface RequestConfig {
  method: RequestMethod
  url: string
  data?: unknown
  params?: Record<string, string>
  headers?: Record<string, string>
  timeout?: number
  responseType?: 'json' | 'blob' | 'text' | 'arraybuffer'
  withCredentials?: boolean
  dataType?: 'json' | 'file'
  showError?: boolean
  errorCallback?: (error: string) => void
  checkDataFormat?: boolean
}

export interface RequestValidation {
  method: RequestMethod
  relativeURL: string
  data: unknown
  checkDataFormat?: boolean
}

export interface RequestInterceptors {
  request?: {
    onFulfilled?: (config: RequestConfig) => RequestConfig | Promise<RequestConfig>
    onRejected?: (error: unknown) => unknown
  }
  response?: {
    onFulfilled?: <T>(response: T) => T | Promise<T>
    onRejected?: (error: unknown) => unknown
  }
} 