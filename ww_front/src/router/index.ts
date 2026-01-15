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
    path: '/home',
    name: 'homeView',
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
    path: '/borrow-apply',
    name: 'borrow-apply',
    component: () => import('../views/BorrowApplyView.vue'),
    meta: {
      title: '借款申请',
      requiresAuth: true
    }
  },
  {
    path: '/borrow-list',
    name: 'borrow-list',
    component: () => import('../views/BorrowListView.vue'),
    meta: {
      title: '我的借款',
      requiresAuth: true
    }
  },
  {
    path: '/borrow-detail/:id',
    name: 'borrow-detail',
    component: () => import('../views/BorrowDetailView.vue'),
    meta: {
      title: '借款详情',
      requiresAuth: true
    }
  },
  {
    path: '/profile',
    name: 'profile',
    component: () => import('../views/ProfileView.vue'),
    meta: {
      title: '个人中心',
      requiresAuth: true
    }
  },
  {
    path: '/profile/avatar',
    name: 'profile-avatar',
    component: () => import('../views/AvatarView.vue'),
    meta: {
      title: '修改头像',
      requiresAuth: true
    }
  },
  {
    path: '/profile/password',
    name: 'profile-password',
    component: () => import('../views/PasswordView.vue'),
    meta: {
      title: '修改密码',
      requiresAuth: true
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

// 全局前置守卫，设置页面标题和权限控制
router.beforeEach((to, _from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 威武信贷` : '威武信贷'
  
  // 检查路由是否需要认证
  const requiresAuth = to.meta.requiresAuth as boolean
  const isLoggedIn = !!localStorage.getItem('token')
  
  if (requiresAuth && !isLoggedIn) {
    // 如果需要认证且未登录，重定向到登录页面
    next({ name: 'login', query: { redirect: to.fullPath } })
  } else {
    next()
  }
})

export default router