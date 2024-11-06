import {createRouter, createWebHistory} from 'vue-router'
import NotFound from '@/views/common/NotFound.vue';
import {config} from '@/CustomizeGlobalConfigurations.js';
import managerIndex from '@/views/manager/ManagerIndex.vue';
import userDiskIndex from '@/views/user/UserDiskIndex.vue';
import syncFileManager from '@/views/manager/sync_manager/FileManager.vue';

/**
 * 创建路由数组
 * @param basePath 基础路径
 * @param routes 路由配置数组
 * @returns {路由配置数组}
 */
const createRoutes = (basePath, routes) => {
    return routes.map(route => {
        return {
            ...route,// 保留原有属性
            path: `${basePath}${route.path}`
        };
    });
};

const mangerUrlBaseRoutes = [
    // 路由配置
    {
        path: '/',
        redirect: '/manager/sync_manager/sync_file_manager',
        children: [
            {
                path: 'sync_manager',
                component: managerIndex,
                redirect: '/manager/sync_manager/sync_file_manager',
                children: [
                    {
                        path: 'sync_file_manager',
                        component: syncFileManager,
                    },
                ]
            }
        ]
    }
];

const userUrlBaseRoutes = [
    // 路由配置
    {
        path: '/',
        component: userDiskIndex,
    }
];

const mangerUrlRoutes = createRoutes(config.managerBaseUrl, mangerUrlBaseRoutes)
const diskUrlRoutes = createRoutes(config.userBaseUrl, userUrlBaseRoutes)

const routes = diskUrlRoutes.concat(mangerUrlRoutes); // 合并路由配置
routes.unshift({path: '/:pathMatch(.*)*', component: NotFound}) //单独添加 404
console.log('routes', routes)

const router = createRouter({
    history: createWebHistory(),
    routes,
});

router.beforeEach((to, from) => {
    // 路由跳转前的操作
    console.log('router.beforeEach', to, from);
});

export default router
