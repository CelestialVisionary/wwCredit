<template>
  <div class="integral-grade-layout">
    <!-- 导航栏 -->
    <div class="nav-container">
      <div class="nav-item">
        <span class="nav-text">首页/积分等级列表</span>
      </div>
    </div>
    
    <!-- 子路由内容 -->
    <div class="content-container">
      <router-view />
    </div>
  </div>
</template>

<script setup lang="ts">
// 布局组件逻辑
</script>

<style scoped>
.integral-grade-layout {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.nav-container {
  margin-bottom: 20px;
}

.nav-item {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 10px;
  cursor: pointer;
}

.nav-text {
  color: #000000;
}

.nav-arrow {
  margin-left: 5px;
  font-size: 14px;
}

.sub-nav {
  margin-left: 20px;
  padding-left: 10px;
  border-left: 2px solid #3b82f6;
}

.sub-nav a {
  display: block;
  padding: 5px 0;
  color: #3b82f6;
  text-decoration: none;
  transition: all 0.3s ease;
}

.sub-nav a:hover {
  color: #2563eb;
}

.sub-nav a.active {
  font-weight: 600;
  color: #2563eb;
}

.content-container {
  flex: 1;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>