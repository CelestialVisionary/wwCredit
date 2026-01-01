<script setup lang="ts">
import { ref } from 'vue'

// 产品数据
const products = ref([
  {
    id: 1,
    name: '稳健理财',
    rate: '5.2%',
    period: '3个月',
    minInvest: '1000元',
    status: 'hot'
  },
  {
    id: 2,
    name: '尊享收益',
    rate: '7.5%',
    period: '6个月',
    minInvest: '5000元',
    status: 'new'
  },
  {
    id: 3,
    name: '灵活宝',
    rate: '4.8%',
    period: '活期',
    minInvest: '100元',
    status: 'popular'
  }
])

// 公告数据
const announcements = ref([
  {
    id: 1,
    title: '系统维护通知',
    content: '尊敬的用户，我们将于今晚22:00至次日凌晨02:00进行系统维护，请提前做好准备。',
    time: '刚刚'
  },
  {
    id: 2,
    title: '新用户注册送豪礼',
    content: '即日起，新用户注册即可获得1000元投资体验金，限时活动，先到先得！',
    time: '2小时前'
  },
  {
    id: 3,
    title: '平台运营数据公示',
    content: '2025年10月运营数据已更新，累计成交额突破10亿，逾期率0.01%。',
    time: '1天前'
  }
])

// 安全保障特点
const securityFeatures = ref([
  {
    icon: '🔒',
    title: '资金安全',
    description: '银行存管，资金流向透明，多重加密保障'
  },
  {
    icon: '🛡️',
    title: '风控保障',
    description: '专业风控团队，严格审核流程，风险可控'
  },
  {
    icon: '📝',
    title: '法律保障',
    description: '合规经营，专业律师团队，法律权益保障'
  },
  {
    icon: '👨‍💼',
    title: '团队保障',
    description: '金融精英团队，多年行业经验，专业可靠'
  }
])

// 模拟数据统计
const stats = ref({
  totalUsers: '258,369',
  totalInvestment: '10.58亿',
  avgRate: '6.8%',
  totalTransactions: '432,156'
})
</script>

<template>
  <div class="home-page">
    <!-- 横幅广告 -->
    <section class="banner">
      <div class="container">
        <div class="banner-content">
          <h1>威武信贷</h1>
          <p>让金融服务更简单</p>
          <button class="btn btn-primary banner-btn">立即投资</button>
        </div>
      </div>
    </section>

    <!-- 平台数据 -->
    <section class="platform-stats">
      <div class="container">
        <div class="stats-grid">
          <div class="stat-item">
            <h3>{{ stats.totalUsers }}</h3>
            <p>注册用户</p>
          </div>
          <div class="stat-item">
            <h3>{{ stats.totalInvestment }}</h3>
            <p>累计投资</p>
          </div>
          <div class="stat-item">
            <h3>{{ stats.avgRate }}</h3>
            <p>平均收益率</p>
          </div>
          <div class="stat-item">
            <h3>{{ stats.totalTransactions }}</h3>
            <p>交易笔数</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 热门产品 -->
    <section class="products-section">
      <div class="container">
        <div class="section-header">
          <h2>热门产品</h2>
          <a href="/invest" class="view-more">查看更多</a>
        </div>
        <div class="products-grid">
          <div v-for="product in products" :key="product.id" class="product-card card">
            <div class="product-header">
              <h3>{{ product.name }}</h3>
              <span v-if="product.status === 'hot'" class="status-badge hot">热门</span>
              <span v-else-if="product.status === 'new'" class="status-badge new">新品</span>
              <span v-else-if="product.status === 'popular'" class="status-badge popular">人气</span>
            </div>
            <div class="product-rate">
              <span class="rate-number">{{ product.rate }}</span>
              <span class="rate-unit">预期年化</span>
            </div>
            <div class="product-info">
              <div class="info-item">
                <span class="info-label">投资期限</span>
                <span class="info-value">{{ product.period }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">起投金额</span>
                <span class="info-value">{{ product.minInvest }}</span>
              </div>
            </div>
            <button class="btn btn-primary product-btn">立即投资</button>
          </div>
        </div>
      </div>
    </section>

    <!-- 安全保障 -->
    <section class="security-section">
      <div class="container">
        <div class="section-header">
          <h2>安全保障</h2>
        </div>
        <div class="security-grid">
          <div v-for="(feature, index) in securityFeatures" :key="index" class="security-item">
            <div class="security-icon">{{ feature.icon }}</div>
            <h3>{{ feature.title }}</h3>
            <p>{{ feature.description }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 最新公告 -->
    <section class="announcements-section">
      <div class="container">
        <div class="section-header">
          <h2>最新公告</h2>
          <a href="/announcements" class="view-more">更多公告</a>
        </div>
        <div class="announcements-list card">
          <div v-for="announcement in announcements" :key="announcement.id" class="announcement-item">
            <div class="announcement-content">
              <h3>{{ announcement.title }}</h3>
              <p>{{ announcement.content }}</p>
            </div>
            <div class="announcement-time">{{ announcement.time }}</div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.home-page {
  padding-bottom: 40px;
}

/* 横幅样式 */
.banner {
  background: linear-gradient(135deg, #1890ff 0%, #40a9ff 100%);
  color: white;
  padding: 80px 0;
  margin-bottom: 40px;
}

.banner-content {
  text-align: center;
}

.banner-content h1 {
  font-size: 48px;
  margin-bottom: 16px;
  font-weight: bold;
}

.banner-content p {
  font-size: 20px;
  margin-bottom: 32px;
  opacity: 0.9;
}

.banner-btn {
  padding: 12px 32px;
  font-size: 18px;
  border-radius: 6px;
}

/* 平台数据 */
.platform-stats {
  background-color: white;
  padding: 40px 0;
  margin-bottom: 40px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 30px;
  text-align: center;
}

.stat-item h3 {
  font-size: 32px;
  color: #1890ff;
  margin-bottom: 8px;
}

.stat-item p {
  color: #666;
  font-size: 14px;
}

/* 通用部分标题 */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.section-header h2 {
  font-size: 24px;
  color: #333;
}

.view-more {
  color: #1890ff;
  font-size: 14px;
}

/* 产品部分 */
.products-section {
  margin-bottom: 40px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
}

.product-card {
  text-align: center;
  transition: transform 0.3s, box-shadow 0.3s;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.product-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.product-header h3 {
  font-size: 18px;
  color: #333;
}

.status-badge {
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.status-badge.hot {
  background-color: #ff4d4f;
  color: white;
}

.status-badge.new {
  background-color: #52c41a;
  color: white;
}

.status-badge.popular {
  background-color: #fa8c16;
  color: white;
}

.product-rate {
  margin-bottom: 24px;
}

.rate-number {
  font-size: 36px;
  font-weight: bold;
  color: #ff4d4f;
  margin-right: 4px;
}

.rate-unit {
  font-size: 14px;
  color: #666;
}

.product-info {
  border-top: 1px solid #f0f0f0;
  padding-top: 16px;
  margin-bottom: 20px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
}

.info-label {
  color: #666;
  font-size: 14px;
}

.info-value {
  color: #333;
  font-size: 14px;
  font-weight: 500;
}

.product-btn {
  width: 100%;
  padding: 10px 0;
}

/* 安全保障 */
.security-section {
  background-color: #f7f7f7;
  padding: 60px 0;
  margin-bottom: 40px;
}

.security-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
  text-align: center;
}

.security-item {
  padding: 30px 20px;
  background-color: white;
  border-radius: 8px;
  transition: transform 0.3s, box-shadow 0.3s;
}

.security-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.security-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.security-item h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 12px;
}

.security-item p {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
}

/* 公告部分 */
.announcements-section {
  margin-bottom: 40px;
}

.announcement-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
}

.announcement-item:last-child {
  border-bottom: none;
}

.announcement-content h3 {
  font-size: 16px;
  color: #333;
  margin-bottom: 8px;
}

.announcement-content p {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.announcement-time {
  color: #999;
  font-size: 14px;
  margin-left: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .banner {
    padding: 60px 0;
  }
  
  .banner-content h1 {
    font-size: 36px;
  }
  
  .stats-grid {
    grid-template-columns: 1fr 1fr;
  }
  
  .stat-item h3 {
    font-size: 24px;
  }
  
  .products-grid {
    grid-template-columns: 1fr;
  }
  
  .announcement-item {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .announcement-time {
    margin-left: 0;
    margin-top: 10px;
  }
}

@media (max-width: 480px) {
  .banner-content h1 {
    font-size: 28px;
  }
  
  .banner-content p {
    font-size: 16px;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
}
</style>