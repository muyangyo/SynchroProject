declare module '@element-plus/icons-vue' {
  import type { Component, DefineComponent } from 'vue'

  export interface IconProps {
    size?: number | string
    color?: string
    class?: string
    style?: Record<string, string>
  }

  export type IconComponent = DefineComponent<IconProps>

  export const Close: IconComponent
  export const Delete: IconComponent
  // ... 其他图标组件

  const icons: Record<string, IconComponent>
  export default icons
} 