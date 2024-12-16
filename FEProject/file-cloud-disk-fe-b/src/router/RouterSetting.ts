import { createRouter, createWebHistory, RouteRecordRaw, NavigationGuardReturn } from 'vue-router'
import NotFound from '@/views/common/NotFound.vue'
import { config } from '@/GlobalConfig'
import userCloudIndex from '@/views/user/UserCloudIndex.vue'
import managerIndex from '@/views/manager/ManagerIndex.vue'
import { ROLES, RoleType } from "@/stores/userStore"

const mangerUrlBaseRoutes: RouteRecordRaw[] = [
  {
    path: config.managerRouterBaseUrl,
    component: managerIndex,
    redirect: '/manager/sync_manager/sync_file_manager',
    children: [
      {
        path: 'sync_manager/sync_file_manager',
        component: () => import('@/views/manager/sync_manager/FileManager.vue'),
      },
      {
        path: 'sync_manager/sync_setting_manager',
        component: () => import('@/views/manager/sync_manager/SettingManager.vue'),
      },
      {
        path: 'cloud_manager/cloud_file_manager',
        component: () => import('@/views/manager/cloud_manager/FileManager.vue'),
      },
      {
        path: 'cloud_manager/cloud_setting_manager',
        component: () => import('@/views/manager/cloud_manager/SettingManager.vue'),
      },
    ]
  },
  {
    path: config.managerRouterBaseUrl + '/login',
    component: () => import('@/views/manager/Login.vue'),
  }
]

const userUrlBaseRoutes: RouteRecordRaw[] = [
  {
    path: '/share',
    component: () => import('@/views/user/OutsideshareIndex.vue'),
    meta: {
      requiresAuth: false
    }
  },
  {
    path: config.userRouterBaseUrl,
    component: userCloudIndex,
  },
  {
    path: config.userRouterBaseUrl + '/login',
    component: () => import('@/views/user/Login.vue'),
  },
  {
    path: config.userRouterBaseUrl + '/:pathMatch(.*)*',
    component: userCloudIndex
  }
]

const routes: RouteRecordRaw[] = [
  ...userUrlBaseRoutes,
  ...mangerUrlBaseRoutes,
  {
    path: '/:pathMatch(.*)*',
    component: NotFound
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 路由守卫
router.beforeEach((to, from): NavigationGuardReturn => {
  const token = getCookie(tokenName)
  const role = localStorage.getItem("role") as RoleType | null

  if (to.meta.requiresAuth === false) {
    return true
  }
  
  if (to.path.startsWith(config.managerRouterBaseUrl)) {
    if (!token || role !== ROLES.admin) {
      console.warn('未登录')
      if (to.path !== config.managerRouterBaseUrl + '/login') {
        return { path: config.managerRouterBaseUrl + '/login' }
      }
      return true
    }
  } else if (to.path.startsWith(config.userRouterBaseUrl)) {
    if (!token || role !== ROLES.user) {
      console.warn('未登录')
      if (to.path !== config.userRouterBaseUrl + '/login') {
        return { path: config.userRouterBaseUrl + '/login' }
      }
      return true
    }
  } else {
    return { path: config.userRouterBaseUrl + '/login' }
  }
  return true
})

export const tokenName = 'Token'

export const getCookie = (name: string): string | null => {
  const value = `; ${document.cookie}`
  const parts = value.split(`; ${name}=`)
  if (parts.length === 2) return parts.pop()?.split(';').shift() ?? null
  return null
}

export const deleteCookie = (name: string): void => {
  document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`
}

export default router 