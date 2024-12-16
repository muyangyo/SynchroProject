import { useDark, useToggle } from "@vueuse/core"

export interface Config {
  theme: "system" | "dark" | "light"
  hostUrl: string
  userRouterBaseUrl: string
  managerRouterBaseUrl: string
}

export const config: Config = {
  theme: "system",
  hostUrl: "/api",
  userRouterBaseUrl: "/user",
  managerRouterBaseUrl: "/manager"
}

interface ThemeResult {
  toggleDark: ReturnType<typeof useToggle>
}

type ThemeCleanup = () => void

export const initTheme = (): ThemeResult | ThemeCleanup => {
  const theme = ["system", "dark", "light"] as const
  type Theme = (typeof theme)[number]
  
  const isDark = useDark()
  const toggleDark = useToggle(isDark)

  const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)')

  const setTheme = () => {
    const currentTheme = config.theme as Theme
    if (theme.includes(currentTheme)) {
      if (currentTheme === "system") {
        isDark.value = mediaQuery.matches
      } else if (currentTheme === "dark") {
        isDark.value = true
      } else if (currentTheme === "light") {
        isDark.value = false
      }
    }
  }

  setTheme()

  if (config.theme === "system") {
    mediaQuery.addEventListener('change', setTheme)
    return () => {
      mediaQuery.removeEventListener('change', setTheme)
    }
  }

  return { toggleDark }
}

export { initTheme, config } 