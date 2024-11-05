import {createRouter, createWebHashHistory} from 'vue-router'

import Find from '@/views/Find.vue'
import Friend from '@/views/Friend.vue'
import NotFindPage from '@/views/NotFindPage.vue'

// children: [
//     {
//         // 推荐
//         path: 'recommend', // 注意：二级路由开始，path 不加 / ，但是今后访问这个路由路径的时候，需要写完整路径
//         component: Recommend
//     },
//     {
//         // 排行榜
//         path: 'toplist',
//         component: TopList
//     },
//     {
//         // 歌单
//         path: 'playlist',
//         component: PlayList
//     }
// ]

const router = createRouter({
    // 指明路由模式：当前创建时哈希模式，路径后面会带 #
    history: createWebHashHistory(),
    // 路由表规则
    routes: [
        {
            // 默认打开页面，会访问 / 根路径，这里借助 redirect 让其强制跳转到 /find
            path: '/',
            redirect: '/find'// 重定向
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

// 路由守卫
router.beforeEach((to, from) => {
    const isAuthenticated = checkAuthentication(); // 自定义的权限检查函数

    if (to.meta.requiresAuth && !isAuthenticated) {
        // 如果目标路由需要权限且用户未登录，重定向到登录页面
        return '/login';
    } else if (to.path === '/restricted') {
        // 如果目标路由是 /restricted，取消导航
        return false;
    } else {
        // 继续导航
        return true;
    }
});

function checkAuthentication() {
    // 这里可以实现具体的权限检查逻辑
    return localStorage.getItem('token') !== null;
}

export default router

