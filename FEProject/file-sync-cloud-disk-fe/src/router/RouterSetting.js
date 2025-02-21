import {createRouter, createWebHashHistory} from 'vue-router'
import {config} from '@/GlobalConfig.js';
import {ROLES} from "@/utils/UserLocalStoreUtils.js";


// 管理端路由配置
const mangerUrlBaseRoutes = [
        // 路由配置
        {
            path: config.managerRouterBaseUrl,
            component: () => import('@/views/manager/ManagerIndex.vue'),
            redirect: '/manager/cloud_manager/cloud_setting_manager',
            // redirect: '/manager/sync_manager/sync_file_manager',
            children: [
                {
                    path: 'sync_manager/sync_setting_manager', //同步文件管理
                    component: () => import('@/views/manager/sync_manager/SettingManager.vue'),
                },
                {
                    path: 'sync_manager/sync_log_manager', //同步设置管理
                    component: () => import('@/views/manager/sync_manager/OperationLog.vue'),
                },
                {
                    path: 'cloud_manager/cloud_log_manager', //操作日志
                    component: () => import('@/views/manager/cloud_manager/OperationLog.vue'),
                }, {
                    path: 'cloud_manager/cloud_setting_manager', //云盘设置管理
                    component: () => import('@/views/manager/cloud_manager/SettingManager.vue'),
                },
            ]
        }, {
            path: config.managerRouterBaseUrl + '/login',
            component: () => import('@/views/manager/Login.vue'),
        }

    ]
;


const userUrlBaseRoutes = [
    // 路由配置
    {
        path: '/share', // 分享文件路由
        component: () => import('@/views/user/OutsideshareIndex.vue'),
        meta: {
            requiresAuth: false // 不需要登录验证
        }
    },
    {
        path: config.userRouterBaseUrl,
        component: () => import('@/views/user/UserCloudIndex.vue'),
    }, {
        path: config.userRouterBaseUrl + '/login',
        component: () => import('@/views/user/Login.vue'),
    },
    {
        path: config.userRouterBaseUrl + '/:pathMatch(.*)*',
        component: () => import('@/views/user/UserCloudIndex.vue')
    }
];

const routes = userUrlBaseRoutes.concat(mangerUrlBaseRoutes); // 合并路由配置
routes.push({
    path: '/:pathMatch(.*)*',
    component: () => import('@/views/common/NotFound.vue')
}) //单独添加 404

const router = createRouter({
    history: createWebHashHistory(),
    routes,
});

// 路由守卫
router.beforeEach((to, from) => {
    const token = getCookie(tokenName);
    const role = localStorage.getItem("role");


    if (to.meta.requiresAuth === false) {
        // 免登入路由
    } else if (to.path.startsWith(config.managerRouterBaseUrl)) {
        // 管理端登入验证
        if (token == null || role !== ROLES.admin) {
            // 未登录
            console.warn('未登录')
            if (to.path !== config.managerRouterBaseUrl + '/login') {
                // 跳转至登录页面
                return {path: config.managerRouterBaseUrl + '/login'}
            }
        }
    } else if (to.path.startsWith(config.userRouterBaseUrl)) {
        // 用户端登入验证
        if (token == null || role !== ROLES.user) {
            // 未登录
            console.warn('未登录')
            if (to.path !== config.userRouterBaseUrl + '/login') {
                // 跳转至登录页面
                return {path: config.userRouterBaseUrl + '/login'}
            }
        }
    } else {
        return {path: config.userRouterBaseUrl + '/login'}
    }

});


export const tokenName = 'Token';

export const getCookie = (name) => {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
    return null;
}

export const deleteCookie = (name) => {
    document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
};

export default router;
