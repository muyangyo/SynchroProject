export interface PreviewBaseProps {
  sourceFilePath: string
}

export interface AudioFile {
  mime: string
  realFile: string | null
  name: string
  artist: string
}

export interface VideoFile {
  url: string
  mime: string
}

export interface PreviewState {
  loading: boolean
  error: string | null
}

export interface PreviewEvents {
  onClose?: () => void
  onError?: (error: Error) => void
  onLoad?: () => void
} 