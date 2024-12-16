export interface ApiError extends Error {
  code: string
  statusCode?: number
  data?: unknown
}

export interface ValidationError extends Error {
  field: string
  value: unknown
  constraint: string
}

export interface FileError extends Error {
  file: File
  type: 'size' | 'type' | 'upload' | 'network'
  details?: unknown
}

export type ErrorHandler = (error: Error) => void
export type ApiErrorHandler = (error: ApiError) => void
export type ValidationErrorHandler = (error: ValidationError) => void
export type FileErrorHandler = (error: FileError) => void 