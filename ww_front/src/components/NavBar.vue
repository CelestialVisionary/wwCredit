<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const router = useRouter()
const isMenuOpen = ref(false)
const isDropdownOpen = ref(false)
const userStore = useUserStore()

// 导航菜单项定义
const navItems = [
  { label: '首页', path: '/' },
  { label: '我要投资', path: '/invest' },
  { label: '安全保障', path: '/security' },
  { label: '平台公告', path: '/announcements' },
  { label: '关于我们', path: '/about' }
]

// 个人中心下拉菜单项
const dropdownItems = [
  { label: '个人中心', path: '/profile' },
  { label: '修改头像', path: '/profile/avatar' },
  { label: '修改密码', path: '/profile/password' }
]

// 计算登录状态
const isLoggedIn = computed(() => userStore.isLoggedIn)
const userInfo = computed(() => userStore.userInfo)

const handleLogin = () => {
  router.push('/login')
}

const handleRegister = () => {
  router.push('/register')
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
  isDropdownOpen.value = false
}

const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value
}

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}

// 点击下拉菜单项
const handleDropdownItemClick = (path: string) => {
  router.push(path)
  isDropdownOpen.value = false
}

// 点击外部关闭下拉菜单
const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as HTMLElement
  if (!target.closest('.user-dropdown')) {
    isDropdownOpen.value = false
  }
}

// 添加点击外部关闭下拉菜单的事件监听
onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<template>
  <header class="navbar">
    <div class="container">
      <div class="navbar-brand">
        <router-link to="/" class="logo">
          <span class="logo-text">威武信贷</span>
        </router-link>
      </div>
      
      <!-- 桌面端导航 -->
      <nav class="nav-desktop">
        <ul class="nav-list">
          <li v-for="item in navItems" :key="item.path" class="nav-item">
            <router-link 
              :to="item.path" 
              class="nav-link"
              :class="{ active: $route.path === item.path }"
            >
              {{ item.label }}
            </router-link>
          </li>
        </ul>
      </nav>
      
      <div class="nav-actions">
        <!-- 登录状态显示 - 带下拉菜单 -->
        <div v-if="isLoggedIn" class="user-dropdown">
          <div 
            class="user-info" 
            @click="toggleDropdown"
          >
            <div class="user-avatar">👤</div>
            <span class="user-name">{{ userInfo?.name || '用户' }}</span>
            <span class="dropdown-arrow" :class="{ 'rotate': isDropdownOpen }">▼</span>
          </div>
          
          <!-- 下拉菜单 -->
          <div class="dropdown-menu" :class="{ 'open': isDropdownOpen }">
            <div 
              v-for="item in dropdownItems" 
              :key="item.path" 
              class="dropdown-item"
              @click="handleDropdownItemClick(item.path)"
            >
              {{ item.label }}
            </div>
            <div class="dropdown-divider"></div>
            <div class="dropdown-item logout" @click="handleLogout">
              退出登录
            </div>
          </div>
        </div>
        
        <!-- 未登录状态显示 -->
        <div v-else>
          <button @click="handleLogin" class="btn-login">登录</button>
          <button @click="handleRegister" class="btn-register">注册</button>
        </div>
        
        <button class="btn-menu-toggle" @click="toggleMenu" aria-label="菜单">
          <span class="menu-icon"></span>
        </button>
      </div>
    </div>
    
    <!-- 移动端导航菜单 -->
    <div class="nav-mobile" :class="{ open: isMenuOpen }">
      <ul class="nav-list-mobile">
        <li v-for="item in navItems" :key="item.path" class="nav-item-mobile">
          <router-link 
            :to="item.path" 
            class="nav-link-mobile"
            @click="isMenuOpen = false"
          >
            {{ item.label }}
          </router-link>
        </li>
        <li class="nav-item-mobile">
          <button @click="handleLogin" class="btn-login-mobile">登录</button>
        </li>
        <li class="nav-item-mobile">
          <button @click="handleRegister" class="btn-register-mobile">注册</button>
        </li>
      </ul>
    </div>
  </header>
</template>

<style scoped>
.navbar {
  background-color: #ffffff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
}

.navbar-brand {
  display: flex;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  text-decoration: none;
}

.logo-text {
  font-size: 20px;
  font-weight: bold;
  color: #1890ff;
}

.nav-desktop {
  display: flex;
  align-items: center;
}

.nav-list {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
}

.nav-item {
  margin: 0 15px;
}

.nav-link {
  color: #333;
  text-decoration: none;
  font-size: 16px;
  padding: 8px 0;
  position: relative;
  transition: color 0.3s;
}

.nav-link:hover,
.nav-link.active {
  color: #1890ff;
}

.nav-link.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #1890ff;
}

.nav-actions {
  display: flex;
  align-items: center;
}

.btn-login,
.btn-register {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
  margin-left: 10px;
}

.btn-login {
  background-color: transparent;
  color: #1890ff;
  border: 1px solid #1890ff;
}

.btn-login:hover {
  background-color: #1890ff;
  color: white;
}

.btn-register {
  background-color: #1890ff;
  color: white;
}

.btn-register:hover {
  background-color: #40a9ff;
}

/* 用户下拉菜单样式 */
.user-dropdown {
  position: relative;
  margin-right: 10px;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.user-avatar {
  font-size: 20px;
  margin-right: 8px;
}

.user-name {
  color: #333;
  font-size: 14px;
  margin-right: 8px;
}

.dropdown-arrow {
  font-size: 10px;
  color: #909399;
  transition: transform 0.3s;
}

.dropdown-arrow.rotate {
  transform: rotate(180deg);
}

/* 下拉菜单 */
.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  background-color: white;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  min-width: 160px;
  z-index: 1000;
  opacity: 0;
  visibility: hidden;
  transform: translateY(-10px);
  transition: all 0.3s;
}

.dropdown-menu.open {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

.dropdown-item {
  padding: 10px 16px;
  color: #606266;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
  white-space: nowrap;
}

.dropdown-item:hover {
  background-color: #ecf5ff;
  color: #409eff;
}

.dropdown-item.logout:hover {
  background-color: #fef0f0;
  color: #f56c6c;
}

.dropdown-divider {
  height: 1px;
  background-color: #ebeef5;
  margin: 5px 0;
}

/* 退出登录按钮 */
.btn-logout {
  padding: 6px 12px;
  background-color: transparent;
  color: #ff4d4f;
  border: 1px solid #ff4d4f;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-logout:hover {
  background-color: #ff4d4f;
  color: white;
}

.btn-menu-toggle {
  display: none;
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px;
  margin-left: 10px;
}

.menu-icon {
  display: block;
  width: 24px;
  height: 2px;
  background-color: #333;
  position: relative;
  transition: background-color 0.3s;
}

.menu-icon::before,
.menu-icon::after {
  content: '';
  position: absolute;
  width: 24px;
  height: 2px;
  background-color: #333;
  transition: all 0.3s;
}

.menu-icon::before {
  top: -8px;
}

.menu-icon::after {
  bottom: -8px;
}

.nav-mobile {
  display: none;
  background-color: white;
  position: absolute;
  top: 64px;
  left: 0;
  right: 0;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transform: translateY(-100%);
  opacity: 0;
  transition: transform 0.3s, opacity 0.3s;
}

.nav-mobile.open {
  display: block;
  transform: translateY(0);
  opacity: 1;
}

.nav-list-mobile {
  list-style: none;
  margin: 0;
  padding: 0;
}

.nav-item-mobile {
  border-bottom: 1px solid #f0f0f0;
}

.nav-link-mobile {
  display: block;
  padding: 16px 20px;
  color: #333;
  text-decoration: none;
  transition: background-color 0.3s;
}

.nav-link-mobile:hover {
  background-color: #f5f5f5;
}

.btn-login-mobile,
.btn-register-mobile {
  width: 100%;
  padding: 16px 20px;
  border: none;
  background: none;
  font-size: 16px;
  text-align: left;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-login-mobile:hover,
.btn-register-mobile:hover {
  background-color: #f5f5f5;
}

@media (max-width: 768px) {
  .nav-desktop {
    display: none;
  }
  
  .btn-menu-toggle {
    display: block;
  }
  
  .btn-login,
  .btn-register {
    display: none;
  }
}
</style>