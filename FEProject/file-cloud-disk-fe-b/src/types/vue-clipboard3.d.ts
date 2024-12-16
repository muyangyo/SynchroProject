declare module 'vue-clipboard3' {
  export interface ClipboardAPI {
    toClipboard: (text: string) => Promise<void>
    // 可能的其他方法
  }

  export interface ClipboardOptions {
    appendToBody?: boolean
    // 其他可能的选项
  }

  const useClipboard: (options?: ClipboardOptions) => ClipboardAPI
  export default useClipboard
} 