declare module 'element-plus/global' {
  export {}
}

declare module 'element-plus' {
  import type { App, Component } from 'vue'
  
  export interface InstallOptions {
    size?: string
    zIndex?: number
    locale?: Record<string, unknown>
  }

  export interface ElIcon extends Component {}

  const ElementPlus: {
    install: (app: App, options?: InstallOptions) => void
  }

  export default ElementPlus
} 