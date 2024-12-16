import type { ShareItem } from './api'

export interface PreviewProps {
  sourceFilePath: string
}

export interface IconProps {
  name: string
  className?: string
}

export interface ShareManagerData {
  list: ShareItem[]
  total: number
  pageSize: number
  currentPage: number
}

export interface ShareManagerMethods {
  handleDelete: (row: ShareItem) => Promise<void>
  handleBatchDeleteShareFile: (isDeleteAll: boolean) => Promise<void>
  handlePageChange: (page: number) => void
} 