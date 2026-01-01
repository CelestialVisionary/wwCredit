import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/invest',
    name: 'invest',
    component: () => import('../views/InvestView.vue'),
    meta: {
      title: '我要投资'
    }
  },
  {
    path: '/security',
    name: 'security',
    component: () => import('../views/SecurityView.vue'),
    meta: {
      title: '安全保障'
    }
  },
  {
    path: '/about',
    name: 'about',
    component: () => import('../views/AboutView.vue'),
    meta: {
      title: '关于我们'
    }
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/LoginView.vue'),
    meta: {
      title: '用户登录',
      noLayout: true
    }
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/RegisterView.vue'),
    meta: {
      title: '用户注册',
      noLayout: true
    }
  },
  {
    path: '/announcements',
    name: 'announcements',
    component: () => import('../views/AnnouncementsView.vue'),
    meta: {
      title: '平台公告'
    }
  },
  // 404页面
  {
    path: '/:pathMatch(.*)*',
    name: 'not-found',
    component: () => import('../views/NotFoundView.vue'),
    meta: {
      title: '页面不存在',
      noLayout: true
    }
  }
]

const router = createRouter({
  history: createWebHistory((import.meta as any).env.BASE_URL),
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
    document.title = to.meta.title ? `${to.meta.title} - 威武信贷` : '威武信贷'
    next()
  })

export default router