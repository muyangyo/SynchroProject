declare module 'vue-pdf-embed' {
  import { DefineComponent } from 'vue'

  interface Props {
    source: string | null
    page?: number
    rotation?: number
    width?: number | string
  }

  const VuePdfEmbed: DefineComponent<Props>
  export default VuePdfEmbed
} 