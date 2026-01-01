<script setup lang="ts">
import { RouterView } from 'vue-router'
import MainLayout from './components/MainLayout.vue'

import { onMounted } from 'vue'
import { useUserStore } from './stores/user'

// 延迟获取用户信息直到组件挂载完成
onMounted(async () => {
  const userStore = useUserStore()
  const token = userStore.token
  if (token) {
    await userStore.fetchUserInfo()
  }
})
</script>

<template>
  <MainLayout>
    <RouterView />
  </MainLayout>
</template>

<style>
/* 全局样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #333;
  background-color: #f0f2f5;
}

a {
  color: #1890ff;
  text-decoration: none;
}

a:hover {
  color: #40a9ff;
  text-decoration: underline;
}

button {
  cursor: pointer;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 通用卡片样式 */
.card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
}

/* 通用按钮样式 */
.btn {
  display: inline-block;
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
  text-align: center;
}

.btn-primary {
  background-color: #1890ff;
  color: white;
}

.btn-primary:hover {
  background-color: #40a9ff;
}

.btn-secondary {
  background-color: #f0f0f0;
  color: #333;
}

.btn-secondary:hover {
  background-color: #e6f7ff;
  color: #1890ff;
}
</style>