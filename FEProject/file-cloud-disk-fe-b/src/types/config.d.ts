export interface ThemeConfig {
  mode: 'light' | 'dark' | 'system'
  primaryColor: string
  backgroundColor: string
  textColor: string
}

export interface ApiConfig {
  baseURL: string
  timeout: number
  withCredentials: boolean
  headers: Record<string, string>
}

export interface AppConfig {
  theme: ThemeConfig
  api: ApiConfig
  storage: {
    prefix: string
    encrypt: boolean
  }
  upload: {
    maxSize: number
    allowedTypes: string[]
    chunkSize: number
  }
} 