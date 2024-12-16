declare module 'docx-preview' {
  export interface DocxOptions {
    className?: string
    inWrapper?: boolean
    ignoreWidth?: boolean
    ignoreHeight?: boolean
    ignoreFonts?: boolean
    breakPages?: boolean
    ignoreLastRenderedPageBreak?: boolean
    renderHeaders?: boolean
    renderFooters?: boolean
    renderFootnotes?: boolean
    renderEndnotes?: boolean
    useBase64URL?: boolean
    useMathMLPolyfill?: boolean
    renderChanges?: boolean
    debug?: boolean
  }

  export function renderAsync(
    blob: Blob,
    container: HTMLElement,
    options?: DocxOptions
  ): Promise<void>
} 