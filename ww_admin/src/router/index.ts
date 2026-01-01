import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

// 路由配置
const routes: Array<RouteRecordRaw> = [
  {    
    path: '/',
    name: 'Dashboard',
    component: () => import('../views/dashboard/DashboardView.vue')
  },
  {
    path: '/users',
    name: 'Users',
    component: () => import('../views/users/UsersListView.vue')
  },
  {    
    path: '/loans',
    name: 'Loans',
    component: () => import('../views/loans/LoansListView.vue')
  },
  {    
    path: '/announcements',
    name: 'Announcements',
    component: () => import('../views/announcements/AnnouncementsListView.vue')
  },
  {    
    path: '/settings',
    name: 'Settings',
    component: () => import('../views/settings/SettingsView.vue')
  },
  {    
    path: '/integral-grade',
    name: 'IntegralGrade',
    component: () => import('../views/core/integral-grade/IntegralGradeLayout.vue'),
    children: [
      {        
        path: '',
        redirect: '/integral-grade/list'
      },
      {        
        path: 'list',
        name: 'IntegralGradeList',
        component: () => import('../views/core/integral-grade/IntegralGradeListView.vue')
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('../views/NotFoundView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior(_to, _from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 全局前置守卫，设置页面标题
router.beforeEach((to, _from, next) => {
  const titles: Record<string, string> = {
    Dashboard: '数据概览',
    Users: '用户管理',
    Loans: '贷款管理',
    Announcements: '公告管理',
    IntegralGrade: '积分等级管理',
    IntegralGradeList: '积分等级列表',
    Settings: '系统设置'
  }
  document.title = titles[to.name as string] ? `${titles[to.name as string]} - 威武信贷管理后台` : '威武信贷管理后台'
  next()
})

export default router