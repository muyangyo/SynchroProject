import { optionalRequest, RequestMethods } from "./RequestTool"
import type { ApiResponse } from '@/types/response'

interface BlobRequestData {
  path: string
  [key: string]: unknown
}

export default async function getBlobData(
  relativeURL: string, 
  data: BlobRequestData
): Promise<Blob> {
  const response = await optionalRequest<Blob>({
    method: RequestMethods.POST,
    url: "/file" + relativeURL,
    data: JSON.stringify(data),
    responseType: 'blob'
  })
  
  if (response instanceof Blob) {
    return response
  }
  throw new Error('Response is not a Blob')
} 