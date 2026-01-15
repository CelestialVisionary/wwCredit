<script setup lang="ts">
import { ref, onMounted } from 'vue'
import apiClient from '../utils/api'

// 定义公告接口
interface Announcement {
  id: number;
  title: string;
  content: string;
  publishDate: string;
  category: string;
  isTop: number;
}

// 公告数据
const announcements = ref<Announcement[]>([])

// 获取最新公告
const fetchAnnouncements = async () => {
  try {
    const response = await apiClient.get('/announcement/latest')
    if (response.data.code === 200) {
      announcements.value = response.data.data
    }
  } catch (error) {
    console.error('获取公告失败:', error)
  }
}

// 格式化时间
const formatTime = (dateString: string) => {
  const date = new Date(dateString)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)
  
  if (minutes < 60) {
    return '刚刚'
  } else if (hours < 24) {
    return `${hours}小时前`
  } else if (days < 7) {
    return `${days}天前`
  } else {
    return date.toLocaleDateString()
  }
}

// 组件挂载时获取公告
onMounted(() => {
  fetchAnnouncements()
})
</script>

<template>
  <div class="home-view">
    <div class="home-container">
      <!-- 左侧导航栏 -->
      <aside class="sidebar">
        <div class="sidebar-content">
          <div class="sidebar-header">
            <h2>用户中心</h2>
          </div>
          
          <nav class="sidebar-nav">
            <ul class="nav-list">
              <!-- 投资管理 -->
              <li class="nav-item">
                <h3 class="nav-title">投资管理</h3>
                <ul class="nav-submenu">
                  <li class="nav-subitem">
                    <a href="/invest" class="nav-link">我要投资</a>
                  </li>
                  <li class="nav-subitem">
                    <a href="/invest/records" class="nav-link">投资记录</a>
                  </li>
                </ul>
              </li>
              
              <!-- 借款管理 -->
              <li class="nav-item">
                <h3 class="nav-title">借款管理</h3>
                <ul class="nav-submenu">
                  <li class="nav-subitem">
                    <a href="/borrow-apply" class="nav-link">我要借款</a>
                  </li>
                  <li class="nav-subitem">
                    <a href="/borrow/records" class="nav-link">借款记录</a>
                  </li>
                </ul>
              </li>
              
              <!-- 资金管理 -->
            <li class="nav-item">
              <h3 class="nav-title">资金管理</h3>
              <ul class="nav-submenu">
                <li class="nav-subitem">
                  <a href="/fund/records" class="nav-link">资金记录</a>
                </li>
                <li class="nav-subitem">
                  <a href="/fund/recharge" class="nav-link">充值</a>
                </li>
                <li class="nav-subitem">
                  <a href="/fund/withdraw" class="nav-link">提现</a>
                </li>
              </ul>
            </li>
            
            <!-- 个人中心 -->
            <li class="nav-item">
              <h3 class="nav-title">个人中心</h3>
              <ul class="nav-submenu">
                <li class="nav-subitem">
                  <a href="/profile/avatar" class="nav-link">修改头像</a>
                </li>
                <li class="nav-subitem">
                  <a href="/profile/password" class="nav-link">修改密码</a>
                </li>
              </ul>
            </li>
            
            <!-- 还款计划 -->
            <li class="nav-item">
              <a href="/repayment/plans" class="nav-link">还款计划</a>
            </li>
            </ul>
          </nav>
        </div>
      </aside>
      
      <!-- 右侧内容区域 -->
      <main class="main-content">
        <!-- 欢迎横幅 -->
        <section class="welcome-banner">
          <h1>威武信贷</h1>
          <p>让金融服务更简单</p>
        </section>
        
        <!-- 最新公告 -->
        <section class="announcements-section">
          <div class="section-header">
            <h2>最新公告</h2>
          </div>
          <div class="announcements-list">
            <div v-for="announcement in announcements" :key="announcement.id" class="announcement-item">
              <div class="announcement-content">
                <h3>{{ announcement.title }}</h3>
                <p>{{ announcement.content }}</p>
              </div>
              <div class="announcement-time">{{ formatTime(announcement.publishDate) }}</div>
            </div>
          </div>
        </section>
        
        <!-- 安全提示 -->
        <section class="security-tips-section">
          <div class="section-header">
            <h2>安全提示</h2>
          </div>
          <div class="security-tips-list">
            <div class="security-tip">
              <div class="security-icon">🔒</div>
              <div class="security-content">
                <h3>账户安全</h3>
                <p>请妥善保管您的账号密码，不要向他人透露</p>
              </div>
            </div>
            <div class="security-tip">
              <div class="security-icon">⚠️</div>
              <div class="security-content">
                <h3>警惕诈骗</h3>
                <p>不要轻信陌生人的投资建议，避免财产损失</p>
              </div>
            </div>
            <div class="security-tip">
              <div class="security-icon">📱</div>
              <div class="security-content">
                <h3>手机验证</h3>
                <p>建议开启手机验证，提高账号安全性</p>
              </div>
            </div>
          </div>
        </section>
      </main>
    </div>
  </div>
</template>

<style scoped>
.home-view {
  background-color: #f5f7fa;
  min-height: calc(100vh - 64px - 200px);
}

.home-container {
  display: flex;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/* 左侧导航栏 */
.sidebar {
  width: 240px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-right: 20px;
  flex-shrink: 0;
}

.sidebar-content {
  padding: 20px;
}

.sidebar-header h2 {
  font-size: 18px;
  color: #333;
  margin-bottom: 20px;
  text-align: center;
}

.nav-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.nav-item {
  margin-bottom: 16px;
}

.nav-title {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin-bottom: 12px;
  padding-left: 8px;
  border-left: 3px solid #1890ff;
}

.nav-submenu {
  list-style: none;
  padding-left: 24px;
  margin: 0;
}

.nav-subitem {
  margin-bottom: 8px;
}

.nav-link {
  display: block;
  padding: 8px 12px;
  color: #666;
  text-decoration: none;
  border-radius: 4px;
  transition: all 0.3s;
  font-size: 14px;
}

.nav-link:hover {
  color: #1890ff;
  background-color: #e6f7ff;
}

/* 右侧内容区域 */
.main-content {
  flex: 1;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  overflow: auto;
}

/* 欢迎横幅 */
.welcome-banner {
  background: linear-gradient(135deg, #1890ff 0%, #40a9ff 100%);
  color: white;
  padding: 40px;
  border-radius: 8px;
  text-align: center;
  margin-bottom: 20px;
}

.welcome-banner h1 {
  font-size: 32px;
  margin-bottom: 8px;
}

.welcome-banner p {
  font-size: 16px;
  opacity: 0.9;
}

/* 通用section样式 */
.section-header {
  margin-bottom: 16px;
}

.section-header h2 {
  font-size: 18px;
  color: #333;
  font-weight: 500;
}

/* 公告列表 */
.announcements-section {
  margin-bottom: 20px;
}

.announcements-list {
  background-color: #f5f7fa;
  border-radius: 8px;
  padding: 16px;
}

.announcement-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #e8e8e8;
}

.announcement-item:last-child {
  border-bottom: none;
}

.announcement-content h3 {
  font-size: 16px;
  color: #333;
  margin-bottom: 4px;
}

.announcement-content p {
  font-size: 14px;
  color: #666;
  margin: 0;
  flex: 1;
}

.announcement-time {
  color: #999;
  font-size: 12px;
  margin-left: 16px;
  white-space: nowrap;
}

/* 安全提示 */
.security-tips-section {
  margin-bottom: 20px;
}

.security-tips-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 16px;
}

.security-tip {
  display: flex;
  align-items: flex-start;
  background-color: #f5f7fa;
  padding: 16px;
  border-radius: 8px;
}

.security-icon {
  font-size: 24px;
  margin-right: 12px;
  margin-top: 4px;
}

.security-content h3 {
  font-size: 16px;
  color: #333;
  margin-bottom: 8px;
}

.security-content p {
  font-size: 14px;
  color: #666;
  margin: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .home-container {
    flex-direction: column;
    padding: 10px;
  }
  
  .sidebar {
    width: 100%;
    margin-right: 0;
    margin-bottom: 20px;
  }
  
  .main-content {
    padding: 16px;
  }
  
  .welcome-banner {
    padding: 30px 20px;
  }
  
  .security-tips-list {
    grid-template-columns: 1fr;
  }
}
</style>