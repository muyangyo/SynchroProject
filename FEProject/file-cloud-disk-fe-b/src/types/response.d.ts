export interface ApiErrorResponse {
  statusCode: string
  errMsg: string
}

export type ApiSuccessResponse<T> = {
  statusCode: 'SUCCESS'
  data: T
}

export type ApiResponse<T> = ApiSuccessResponse<T> | ApiErrorResponse

export interface PaginationData<T> {
  list: T[]
  total: number
  pageSize: number
  currentPage: number
}

export type PaginationResponse<T> = ApiResponse<PaginationData<T>> 