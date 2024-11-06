import {createRouter, createWebHistory} from 'vue-router'

const routes = [
    // 路由配置
];

const router = createRouter({
    history: createWebHistory(),
    routes,
})

router.beforeEach((to, from) => {
    // 路由跳转前的操作
    console.log('router.beforeEach', to, from);
});

export default router
