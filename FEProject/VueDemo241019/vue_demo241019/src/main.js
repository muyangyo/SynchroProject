import {createApp} from 'vue'
import App from './App.vue'

let app = createApp(App);
// app.directive('color', {
//     // 指令所在的DOM元素变成真实DOM了，会自动执行一次(就是挂载时执行一次,后续不会执行了)
//     mounted(el, binding) {
//         console.log("mounted执行了")
//         console.log(el);
//         console.log(binding);
//         el.style.color = binding.value;
//     },
//
//     // 每次指令绑定表达式的值变了，都会执行一次
//     updated(el, binding) {
//         console.log("updated执行了")
//         console.log(el);
//         console.log(binding);
//         el.style.color = binding.value;
//     }
// })
app.directive('color', (el, binding) => {
    console.log("设置的回调函数执行了...")
    console.log(el);
    console.log(binding);
    el.style.color = binding.value
})

import router from "@/Router-Setting.js"
// 导入
import {createPinia} from 'pinia'
// 创建
const pinia = createPinia()
// 注册
app.use(pinia)

app.use(router);

import ElementPlus from 'element-plus'  // 导入element-plus
import 'element-plus/dist/index.css'    // 导入element-plus样式
app.use(ElementPlus);    // 使用element-plus

app.mount('#app');
