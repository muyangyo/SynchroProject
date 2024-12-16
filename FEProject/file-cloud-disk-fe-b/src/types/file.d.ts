export interface FileType {
  mimeType: string
  extension: string
  category: string
}

export interface FileMetadata {
  fileName: string
  filePath: string
  fileSize: number
  fileType: FileType
  createTime: string
  updateTime: string
  isDirectory: boolean
  parentPath: string
}

export interface UploadChunkInfo {
  fileId: string
  filePid: string
  fileName: string
  fileMd5: string
  chunkIndex: number
  chunks: number
  chunkFile: Blob
}

export interface DownloadInfo {
  filePath: string
  fileName: string
  fileSize: number
  downloadUrl: string
} 