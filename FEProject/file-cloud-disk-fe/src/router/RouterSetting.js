import {createRouter, createWebHistory} from 'vue-router'
import NotFound from '@/views/common/NotFound.vue';
import {config} from '@/GlobalConfig.js';
import userCloudIndex from '@/views/user/UserCloudIndex.vue';
import managerIndex from '@/views/manager/ManagerIndex.vue';
import syncSettingManager from '@/views/manager/sync_manager/SettingManager.vue';
import syncFileManager from '@/views/manager/sync_manager/FileManager.vue';

import cloudSettingManager from '@/views/manager/cloud_manager/SettingManager.vue';
import cloudFileManager from '@/views/manager/cloud_manager/FileManager.vue';


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
            path: config.managerBaseUrl,
            component: managerIndex,
            redirect: '/manager/sync_manager/sync_file_manager',
            children: [
                {
                    path: 'sync_manager/sync_file_manager', //同步文件管理
                    component: syncFileManager,
                },
                {
                    path: 'sync_manager/sync_setting_manager', //同步设置管理
                    component: syncSettingManager,
                },
                {
                    path: 'cloud_manager/cloud_file_manager', //云端文件管理
                    component: cloudFileManager,
                }, {
                    path: 'cloud_manager/cloud_setting_manager', //云端设置管理
                    component: cloudSettingManager,
                },
            ]
        }, {
            path: config.managerBaseUrl + '/login',
            component: () => import('@/views/manager/Login.vue'),
        }

    ]
;

const userUrlBaseRoutes = [
    // 路由配置
    {
        path: config.userBaseUrl,
        component: userCloudIndex,
    }, {
        path: config.userBaseUrl + '/login',
        component: () => import('@/views/user/Login.vue'),
    }
];

const routes = userUrlBaseRoutes.concat(mangerUrlBaseRoutes); // 合并路由配置
routes.unshift({path: '/:pathMatch(.*)*', component: NotFound}) //单独添加 404
console.log('routes', routes)

const router = createRouter({
    history: createWebHistory(),
    routes,
});


router.beforeEach((to, from) => {
    // 路由跳转前的操作
    console.log('router.beforeEach', to, from);

    // 管理端
    if (to.path.startsWith(config.managerBaseUrl)) {


        // 管理端登入验证
        if (!sessionStorage.getItem('session')) {
            // 未登录
            console.log('未登录')
            if (to.path !== config.managerBaseUrl + '/login') {
                // 跳转至登录页面
                return {path: config.managerBaseUrl + '/login'}
            }
        }
    } else if (to.path.startsWith(config.userBaseUrl)) {
        // 用户端登入验证
        if (!sessionStorage.getItem('session')) {
            // 未登录
            console.log('未登录')
            if (to.path !== config.userBaseUrl + '/login') {
                // 跳转至登录页面
                return {path: config.userBaseUrl + '/login'}
            }
        }
    }
});

export default router;
