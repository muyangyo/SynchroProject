export type HttpMethod = 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH'

export interface HttpHeaders {
  [key: string]: string
}

export interface HttpRequestConfig {
  method: HttpMethod
  url: string
  data?: unknown
  params?: Record<string, string>
  headers?: HttpHeaders
  timeout?: number
  responseType?: 'json' | 'blob' | 'text' | 'arraybuffer'
  withCredentials?: boolean
}

export interface HttpResponse<T = unknown> {
  data: T
  status: number
  statusText: string
  headers: HttpHeaders
}

export interface HttpError extends Error {
  config: HttpRequestConfig
  code?: string
  request?: XMLHttpRequest
  response?: HttpResponse
  isAxiosError: boolean
} 