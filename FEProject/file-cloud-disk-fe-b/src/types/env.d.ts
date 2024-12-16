/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_API_URL: string
  readonly VITE_APP_TITLE: string
  readonly VITE_APP_ENV: 'development' | 'production' | 'test'
  // 更多环境变量...
}

interface ImportMeta {
  readonly env: ImportMetaEnv
} 