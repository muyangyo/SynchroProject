declare global {
  interface Window {
    _iconfont_svg_string_4759787: string
  }

  type DeepPartial<T> = {
    [P in keyof T]?: T[P] extends object ? DeepPartial<T[P]> : T[P]
  }

  type Nullable<T> = T | null

  type ValueOf<T> = T[keyof T]
}

export {} 