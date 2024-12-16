import type { FileMetadata, DownloadInfo } from './file'
import type { PaginationData } from './response'

export interface ShareItem {
  code: string
  url: string
  filePath: string
  createTime: string
  status: number
}

export interface FileInfo {
  fileName: string
  mime: string
  mountRootPath?: string
  url?: string
  fileType?: {
    mimeType: string
  }
}

export interface FileListResponse extends PaginationData<FileMetadata> {
  currentPath: string
  parentPath: string | null
}

export interface ShareListResponse extends PaginationData<ShareItem> {
  // 可能的额外字段
}

export interface DownloadResponse {
  data: DownloadInfo
} 