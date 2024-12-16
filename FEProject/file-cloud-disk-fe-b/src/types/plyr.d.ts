declare module 'plyr' {
  export interface PlyrControls {
    [key: string]: boolean | string[]
  }

  export interface PlyrI18n {
    restart: string
    rewind: string
    play: string
    pause: string
    seek: string
    volume: string
    mute: string
    unmute: string
    enterFullscreen: string
    exitFullscreen: string
    frameTitle: string
    settings: string
    menuBack: string
    speed: string
    normal: string
    quality: string
    loop: string
    playbackRate: string
    search: string
    reset: string
    file: string
    pip: string
    [key: string]: string
  }

  export interface PlyrOptions {
    i18n?: PlyrI18n
    controls?: string[]
    settings?: string[]
    speed?: {
      selected: number
      options: number[]
    }
    autoplay?: boolean
    volume?: number
    loop?: {
      active: boolean
    }
    keyboard?: {
      focused: boolean
      global: boolean
    }
    tooltips?: {
      controls: boolean
      seek: boolean
    }
    ratio?: string
    storage?: {
      enabled: boolean
      key: string
    }
    fullscreen?: {
      enabled: boolean
      fallback: boolean
      iosNative: boolean
    }
    ads?: {
      enabled: boolean
    }
  }

  export default class Plyr {
    constructor(target: HTMLElement | string, options?: PlyrOptions)
    play(): Promise<void>
    pause(): void
    togglePlay(): void
    stop(): void
    restart(): void
    rewind(seekTime: number): void
    forward(seekTime: number): void
    seek(time: number): void
    getCurrentTime(): number
    getDuration(): number
    getVolume(): number
    isMuted(): boolean
    isPaused(): boolean
    isPlaying(): boolean
    destroy(): void
    on(event: string, callback: Function): void
    off(event: string, callback: Function): void
  }
} 