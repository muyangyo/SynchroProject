// 导入 Vue 相关的模块
import {createApp} from 'vue'

// 导入应用的主组件和路由
import App from './App.vue'
import router from './router/RouterSetting.js'
// 导入 Element Plus 组件库
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 导入自定义主题样式文件
import 'element-plus/theme-chalk/dark/css-vars.css'
import '@/style/GlobalStyle.scss'
import * as ElementPlusIconsVue from '@element-plus/icons-vue';
// 导入 iconfont
import '@/assets/iconfont.js'
/*

import VConsole from 'vconsole';

const vConsole = new VConsole();
*/


// 创建 Vue 应用实例
const app = createApp(App);


// 使用 Element Plus 组件库
app.use(ElementPlus)
// 全局注册 Element Plus 的图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component);
}

// 使用路由
app.use(router);

// 挂载应用到指定的 DOM 元素
app.mount('#app');

// 导入全局配置并初始化主题
import {initTheme} from "@/GlobalConfig.js";

initTheme();