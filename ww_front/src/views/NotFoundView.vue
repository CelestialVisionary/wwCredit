<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const countdown = ref(5)

// 返回首页倒计时
onMounted(() => {
  const timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(timer)
      router.push('/')
    }
  }, 1000)
  
  // 清理定时器
  return () => clearInterval(timer)
})

// 立即返回首页
const goHome = () => {
  router.push('/')
}
</script>

<template>
  <div class="not-found-page">
    <div class="container">
      <div class="error-content card">
        <div class="error-code">404</div>
        <h1>页面不存在</h1>
        <p>抱歉，您访问的页面不存在或已被移除</p>
        
        <div class="error-details">
          <p>可能的原因：</p>
          <ul>
            <li>输入的网址有误</li>
            <li>该页面已被移除或重命名</li>
            <li>页面链接已过期</li>
          </ul>
        </div>
        
        <div class="error-actions">
          <button class="primary-button" @click="goHome">立即返回首页</button>
          <p class="countdown-text">
            或 {{ countdown }} 秒后自动返回首页
          </p>
        </div>
      </div>
      
      <!-- 热门推荐 -->
      <div class="popular-links">
        <h3>热门推荐</h3>
        <div class="links-grid">
          <router-link to="/" class="link-card">
            <div class="link-icon">🏠</div>
            <span>首页</span>
          </router-link>
          <router-link to="/invest" class="link-card">
            <div class="link-icon">💰</div>
            <span>我要投资</span>
          </router-link>
          <router-link to="/security" class="link-card">
            <div class="link-icon">🛡️</div>
            <span>安全保障</span>
          </router-link>
          <router-link to="/announcements" class="link-card">
            <div class="link-icon">📢</div>
            <span>平台公告</span>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.not-found-page {
  min-height: calc(100vh - 200px);
  display: flex;
  align-items: center;
  padding: 60px 0;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 错误内容 */
.error-content {
  background-color: white;
  padding: 60px;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  margin-bottom: 40px;
}

.error-code {
  font-size: 120px;
  font-weight: bold;
  color: #1890ff;
  opacity: 0.9;
  margin-bottom: 20px;
  line-height: 1;
}

.error-content h1 {
  font-size: 36px;
  color: #333;
  margin-bottom: 16px;
}

.error-content p {
  font-size: 18px;
  color: #666;
  margin-bottom: 32px;
}

/* 错误详情 */
.error-details {
  background-color: #f7f7f7;
  padding: 24px;
  border-radius: 8px;
  margin: 0 auto 32px;
  max-width: 600px;
  text-align: left;
}

.error-details p {
  font-size: 16px;
  color: #333;
  margin-bottom: 12px;
  font-weight: 500;
}

.error-details ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.error-details li {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
  padding-left: 20px;
  position: relative;
}

.error-details li:last-child {
  margin-bottom: 0;
}

.error-details li::before {
  content: '•';
  position: absolute;
  left: 0;
  color: #1890ff;
  font-size: 18px;
  line-height: 1;
}

/* 错误操作 */
.error-actions {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.primary-button {
  padding: 12px 48px;
  background-color: #1890ff;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 18px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.primary-button:hover {
  background-color: #40a9ff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3);
}

.countdown-text {
  font-size: 16px;
  color: #999;
  margin: 0;
}

/* 热门推荐 */
.popular-links {
  max-width: 800px;
  margin: 0 auto;
}

.popular-links h3 {
  font-size: 24px;
  color: #333;
  text-align: center;
  margin-bottom: 24px;
}

.links-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 20px;
}

.link-card {
  background-color: white;
  padding: 24px;
  border-radius: 8px;
  text-align: center;
  text-decoration: none;
  color: #333;
  transition: all 0.3s;
  border: 1px solid #f0f0f0;
}

.link-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  border-color: #1890ff;
  color: #1890ff;
}

.link-icon {
  font-size: 36px;
  margin-bottom: 12px;
}

.link-card span {
  font-size: 16px;
  font-weight: 500;
  display: block;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .not-found-page {
    padding: 40px 0;
    min-height: calc(100vh - 150px);
  }
  
  .error-content {
    padding: 40px 20px;
  }
  
  .error-code {
    font-size: 80px;
  }
  
  .error-content h1 {
    font-size: 28px;
  }
  
  .error-content p {
    font-size: 16px;
  }
  
  .links-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .not-found-page {
    padding: 30px 0;
  }
  
  .error-content {
    padding: 30px 16px;
  }
  
  .error-code {
    font-size: 60px;
  }
  
  .error-content h1 {
    font-size: 24px;
  }
  
  .primary-button {
    width: 100%;
    max-width: 280px;
  }
  
  .links-grid {
    grid-template-columns: 1fr;
  }
  
  .link-card {
    flex-direction: row;
    align-items: center;
    justify-content: center;
    gap: 16px;
    padding: 16px;
  }
  
  .link-icon {
    font-size: 28px;
    margin-bottom: 0;
  }
}
</style>