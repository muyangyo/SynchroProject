declare module 'aplayer' {
  interface APlayerAudio {
    name: string
    artist?: string
    url: string
    cover?: string
    type?: string
    lrc?: string
    theme?: string
  }

  interface APlayerOptions {
    container: HTMLElement
    audio: APlayerAudio | APlayerAudio[]
    mini?: boolean
    autoplay?: boolean
    theme?: string
    loop?: 'all' | 'one' | 'none'
    order?: 'list' | 'random'
    preload?: 'none' | 'metadata' | 'auto'
    volume?: number
    mutex?: boolean
    listFolded?: boolean
    listMaxHeight?: string
    lrcType?: number
    fixed?: boolean
    seeked?: boolean
    showlrc?: boolean
    storageName?: string
  }

  export default class APlayer {
    constructor(options: APlayerOptions)
    play(): void
    pause(): void
    seek(time: number): void
    toggle(): void
    on(event: string, handler: Function): void
    volume(percentage: number, nostorage: boolean): void
    destroy(): void
  }
} 