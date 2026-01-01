<template>
  <div class="admin-container">
    <!-- 侧边栏 -->
    <aside class="sidebar">
      <div class="sidebar-header">
        <h1>威武信贷</h1>
        <p class="subtitle">管理后台</p>
      </div>
      <nav class="sidebar-nav">
        <router-link to="/" class="nav-item">
          <span class="nav-icon">📊</span>
          <span class="nav-text">数据概览</span>
        </router-link>
        <router-link to="/users" class="nav-item">
          <span class="nav-icon">👥</span>
          <span class="nav-text">用户管理</span>
        </router-link>
        <router-link to="/loans" class="nav-item">
          <span class="nav-icon">💰</span>
          <span class="nav-text">贷款管理</span>
        </router-link>
        <router-link to="/announcements" class="nav-item">
          <span class="nav-icon">📢</span>
          <span class="nav-text">公告管理</span>
        </router-link>
        <div class="nav-item dropdown" @click="integralGradeDropdownOpen = !integralGradeDropdownOpen">
          <span class="nav-icon">⭐</span>
          <span class="nav-text">积分等级管理</span>
          <span class="nav-arrow" :class="{'open': integralGradeDropdownOpen}">▼</span>
          <div class="dropdown-menu" :class="{'open': integralGradeDropdownOpen}">
            <router-link to="/integral-grade/list" class="dropdown-item">
              积分等级列表
            </router-link>
          </div>
        </div>
        <router-link to="/settings" class="nav-item">
          <span class="nav-icon">⚙️</span>
          <span class="nav-text">系统设置</span>
        </router-link>
      </nav>
    </aside>
    
    <!-- 主内容区 -->
    <div class="main-area">
      <!-- 头部 -->
      <header class="header">
        <div class="header-left">
          <template v-if="route.path.startsWith('/integral-grade')">
            <div class="nav-container">
              <div class="nav-item">
                <span class="nav-text">首页/积分等级列表</span>
              </div>
            </div>
          </template>
          <template v-else>
            <h2>{{ currentTitle }}</h2>
          </template>
        </div>
        <div class="header-right">
          <div class="user-info">
            <span class="user-avatar">👤</span>
            <span class="user-name">管理员</span>
          </div>
        </div>
      </header>
      
      <!-- 路由视图 -->
      <main class="main-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import IntegralGradeLayout from './views/core/integral-grade/IntegralGradeLayout.vue'

const route = useRoute()
// Dropdown states
const integralGradeDropdownOpen = ref(false)

// 根据当前路由计算页面标题
const currentTitle = computed(() => {
  const titles: Record<string, string> = {
    '/': '数据概览',
    '/users': '用户管理',
    '/loans': '贷款管理',
    '/announcements': '公告管理',
    '/integral-grade': '积分等级管理',
    '/integral-grade/list': '积分等级列表',
    '/settings': '系统设置'
  }
  return titles[route.path] || '管理后台'
})
</script>

<style scoped>
.sidebar-header {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #334155;
}

.sidebar-header h1 {
  font-size: 24px;
  margin-bottom: 5px;
  color: #f8fafc;
}

.subtitle {
  font-size: 14px;
  color: #94a3b8;
}

.sidebar-nav {
  display: flex;
  flex-direction: column;
}

.nav-item.dropdown {
  position: relative;
  cursor: pointer;
  flex-wrap: wrap;
}

.nav-arrow {
  margin-left: auto;
  transition: transform 0.3s ease;
}

.nav-arrow.open {
  transform: rotate(180deg);
}

.dropdown-menu {
  display: none;
  width: 100%;
  padding-left: 32px;
  margin-top: 8px;
  box-sizing: border-box;
  border-left: 2px solid #3b82f6;
}

.dropdown-menu.open {
  display: flex;
  flex-direction: column;
}

.dropdown-item {
  padding: 5px 0;
  color: #94a3b8;
  text-decoration: none;
  transition: color 0.3s ease;
}

.dropdown-item:hover {
  color: #f8fafc;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 8px;
  color: #94a3b8;
  text-decoration: none;
  transition: all 0.3s;
}

.nav-item:hover {
  background-color: #334155;
  color: #f8fafc;
}

.nav-item.router-link-active {
  background-color: #3b82f6;
  color: white;
}

.nav-icon {
  font-size: 18px;
  width: 20px;
  text-align: center;
}

.nav-text {
  font-size: 16px;
}

.main-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-avatar {
  font-size: 24px;
}

.user-name {
  color: #334155;
  font-weight: 500;
}
</style>