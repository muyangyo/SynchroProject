/// <reference path="./vue-pdf-embed.d.ts" />
/// <reference path="./vue-clipboard3.d.ts" />
/// <reference path="./docx-preview.d.ts" />
/// <reference path="./plyr.d.ts" />
/// <reference path="./aplayer.d.ts" />

declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

interface ImportMetaEnv {
  readonly VITE_APP_TITLE: string
  // 更多环境变量...
}

interface ImportMeta {
  readonly env: ImportMetaEnv
} 