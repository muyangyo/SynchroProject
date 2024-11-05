// 导入 Vue 相关的模块
import {createApp} from 'vue'
import {createPinia} from 'pinia'

// 导入应用的主组件和路由
import App from './App.vue'
import router from './router'
// 导入 Element Plus 组件库
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 导入自定义主题样式文件
import 'element-plus/theme-chalk/dark/css-vars.css'
import '@/style/global-style.scss'

// 创建 Vue 应用实例
const app = createApp(App);
// 创建 Pinia 状态管理实例
const pinia = createPinia();

// 使用 Element Plus 组件库
app.use(ElementPlus)
// 使用 Pinia 状态管理
app.use(pinia);
// 使用路由
app.use(router);

// 挂载应用到指定的 DOM 元素
app.mount('#app');
