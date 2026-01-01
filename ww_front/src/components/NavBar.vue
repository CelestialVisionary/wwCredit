<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const isMenuOpen = ref(false)

// 导航菜单项定义
const navItems = [
  { label: '首页', path: '/' },
  { label: '我要投资', path: '/invest' },
  { label: '安全保障', path: '/security' },
  { label: '平台公告', path: '/announcements' },
  { label: '关于我们', path: '/about' }
]

const handleLogin = () => {
  router.push('/login')
}

const handleRegister = () => {
  router.push('/register')
}

const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value
}
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
        <button @click="handleLogin" class="btn-login">登录</button>
        <button @click="handleRegister" class="btn-register">注册</button>
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