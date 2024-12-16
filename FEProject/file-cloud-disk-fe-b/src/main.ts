import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router/RouterSetting'
import ElementPlus from 'element-plus'
import type { InstallOptions } from 'element-plus'
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
import '@/style/GlobalStyle.scss'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import { initTheme } from "@/GlobalConfig"

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)

const elementPlusOptions: InstallOptions = {
  // element-plus 配置选项
}
app.use(ElementPlus, elementPlusOptions)

// Register Element Plus icons
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(router)
app.mount('#app')

initTheme() 