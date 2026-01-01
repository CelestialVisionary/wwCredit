<template>
  <div class="not-found-view">
    <div class="error-container">
      <h1 class="error-code">404</h1>
      <p class="error-message">页面不存在</p>
      <p class="error-description">抱歉，您访问的页面不存在或已被移除</p>
      <router-link to="/" class="back-home-btn">返回首页</router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
// 404页面组件
</script>

<style scoped>
.not-found-view {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
  padding: 40px 20px;
}

.error-container {
  text-align: center;
  padding: 40px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.08);
}

.error-code {
  font-size: 80px;
  font-weight: 700;
  color: #3b82f6;
  margin: 0 0 16px 0;
  line-height: 1;
}

.error-message {
  font-size: 24px;
  color: #1e293b;
  margin: 0 0 12px 0;
  font-weight: 600;
}

.error-description {
  font-size: 16px;
  color: #64748b;
  margin: 0 0 32px 0;
}

.back-home-btn {
  display: inline-block;
  padding: 12px 24px;
  background-color: #3b82f6;
  color: white;
  border-radius: 6px;
  font-size: 16px;
  font-weight: 500;
  text-decoration: none;
  transition: background-color 0.3s;
}

.back-home-btn:hover {
  background-color: #2563eb;
}
</style>