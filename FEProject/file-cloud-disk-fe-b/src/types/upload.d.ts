import type { FileProgressEvent, FileSuccessEvent, FileErrorEvent } from './events'

export interface UploadOptions {
  url: string
  method?: 'POST' | 'PUT'
  headers?: Record<string, string>
  withCredentials?: boolean
  data?: Record<string, unknown>
  filename?: string
  file: File | Blob
  onProgress?: (event: FileProgressEvent) => void
  onSuccess?: (event: FileSuccessEvent) => void
  onError?: (event: FileErrorEvent) => void
}

export interface ChunkUploadOptions extends UploadOptions {
  chunkSize?: number
  chunkRetry?: number
  concurrent?: number
  chunkFilename?: string
}

export interface UploadTask {
  abort: () => void
  onProgress: (callback: (event: FileProgressEvent) => void) => void
  onSuccess: (callback: (event: FileSuccessEvent) => void) => void
  onError: (callback: (event: FileErrorEvent) => void) => void
} 