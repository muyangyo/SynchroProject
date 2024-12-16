import type { PreviewBaseProps, PreviewEvents } from './preview'
import type { ShareItem } from './api'

export interface AudioPreviewProps extends PreviewBaseProps, PreviewEvents {
  autoplay?: boolean
  volume?: number
}

export interface VideoPreviewProps extends PreviewBaseProps, PreviewEvents {
  autoplay?: boolean
  controls?: boolean
  poster?: string
}

export interface ShareManagerProps {
  pageSize?: number
  defaultSort?: {
    prop: keyof ShareItem
    order: 'ascending' | 'descending'
  }
}

export interface IconProps {
  name: string
  size?: string | number
  color?: string
  className?: string
} 