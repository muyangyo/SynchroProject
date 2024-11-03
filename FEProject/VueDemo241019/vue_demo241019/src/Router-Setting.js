import {createRouter, createWebHashHistory} from 'vue-router'

import Find from '@/views/Find.vue'
import Friend from '@/views/Friend.vue'
import NotFindPage from '@/views/NotFindPage.vue'


const router = createRouter({
    // 指明路由模式：当前创建时哈希模式，路径后面会带 #
    history: createWebHashHistory(),
    // 路由表规则
    routes: [
        {
            // 默认打开页面，会访问 / 根路径，这里借助 redirect 让其强制跳转到 /find
            path: '/',
            redirect: '/find' // 重定向
        },
        {
            path: '/find',
            component: Find
        },
        {
            path: '/friend',
            component: Friend
        }, {
            // 配置 404 规则
            path: '/:pathMatch(.*)*',
            component: NotFindPage //需要自己写
        }
    ]
})
export default router

