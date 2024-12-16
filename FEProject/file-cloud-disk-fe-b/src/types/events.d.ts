export interface FileUploadEvent {
  file: File
  fileList: File[]
}

export interface FileProgressEvent {
  file: File
  percent: number
}

export interface FileSuccessEvent {
  file: File
  response: unknown
}

export interface FileErrorEvent {
  file: File
  error: Error
}

export interface FileDragEvent {
  event: globalThis.DragEvent
  file: File
}

export type FileEventHandler = (event: FileUploadEvent) => void
export type ProgressEventHandler = (event: FileProgressEvent) => void
export type SuccessEventHandler = (event: FileSuccessEvent) => void
export type ErrorEventHandler = (event: FileErrorEvent) => void
export type DragEventHandler = (event: FileDragEvent) => void 