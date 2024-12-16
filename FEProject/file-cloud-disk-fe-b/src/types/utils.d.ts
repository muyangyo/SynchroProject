export interface FileSizeOptions {
  precision?: number
  stripTrailingZeros?: boolean
}

export type FileSizeUnit = 'B' | 'KB' | 'MB' | 'GB' | 'TB'

export interface CookieOptions {
  path?: string
  domain?: string
  secure?: boolean
  sameSite?: 'Strict' | 'Lax' | 'None'
  expires?: Date | number
}

export interface StorageOptions {
  prefix?: string
  encrypt?: boolean
}

export interface ValidateOptions {
  required?: boolean
  type?: string
  min?: number
  max?: number
  pattern?: RegExp
  message?: string
} 