<script setup lang="ts">
import { ref } from 'vue'

// 安全保障模块数据
const securityModules = ref([
  {
    id: 'funds',
    title: '资金安全',
    icon: '🔒',
    description: '银行资金存管，确保您的资金安全',
    details: [
      '与国内知名银行合作，实现用户资金与平台运营资金完全分离',
      '所有资金流向透明可查，确保每一笔交易真实可信',
      '采用多重加密技术，保障资金交易过程的安全性',
      '资金提现严格审核，确保资金安全到账'
    ],
    image: '/images/security-funds.svg'
  },
  {
    id: 'risk',
    title: '风控保障',
    icon: '🛡️',
    description: '专业风控团队，严格审核流程',
    details: [
      '拥有多年金融行业经验的专业风控团队',
      '采用先进的风险评估模型，多维度评估借款项目',
      '严格的贷前审核、贷中监控和贷后管理体系',
      '建立风险准备金机制，为投资者提供多层保障'
    ],
    image: '/images/security-risk.svg'
  },
  {
    id: 'legal',
    title: '法律保障',
    icon: '📝',
    description: '合规经营，法律权益有保障',
    details: [
      '严格遵守国家金融法律法规，合规经营',
      '专业律师团队提供法律支持，确保平台运营合法合规',
      '所有投资项目均有法律文件保障，权利义务明确',
      '提供专业的法律援助，保障投资者合法权益'
    ],
    image: '/images/security-legal.svg'
  },
  {
    id: 'tech',
    title: '技术保障',
    icon: '💻',
    description: '先进技术架构，信息安全防护',
    details: [
      '采用银行级别的数据加密技术，保障用户信息安全',
      '分布式服务器架构，确保系统稳定运行',
      '定期进行安全审计和漏洞扫描，防范潜在风险',
      '24小时实时监控系统，保障交易安全进行'
    ],
    image: '/images/security-tech.svg'
  }
])

// 安全认证数据
const certifications = ref([
  {
    id: 1,
    name: 'ICP备案',
    number: '粤ICP备12345678号',
    icon: '📊'
  },
  {
    id: 2,
    name: '金融许可证',
    number: 'J001234567',
    icon: '🏦'
  },
  {
    id: 3,
    name: 'ISO27001认证',
    number: 'ISO27001-2020',
    icon: '✅'
  },
  {
    id: 4,
    name: '等保三级认证',
    number: 'DB31010020230001',
    icon: '🛡️'
  }
])

// 安全问答数据
const faqs = ref([
  {
    id: 1,
    question: '如何保障我的资金安全？',
    answer: '我们与国内知名银行合作，实现资金存管，将用户资金与平台运营资金完全分离。所有资金流向透明可查，并采用多重加密技术保障交易安全。同时，我们建立了风险准备金机制，为投资者提供额外保障。'
  },
  {
    id: 2,
    question: '平台如何控制投资风险？',
    answer: '我们拥有专业的风控团队，采用先进的风险评估模型，对每个借款项目进行多维度评估。同时，我们建立了严格的贷前审核、贷中监控和贷后管理体系，确保投资项目的安全性。'
  },
  {
    id: 3,
    question: '我的个人信息会被泄露吗？',
    answer: '我们采用银行级别的数据加密技术，严格保护用户个人信息。平台不会向任何第三方泄露用户信息，所有员工均签署保密协议，确保您的信息安全。'
  },
  {
    id: 4,
    question: '投资到期后如何提现？',
    answer: '投资到期后，资金会自动返还到您的账户余额中。您可以在账户中心发起提现申请，我们会在1-3个工作日内将资金转入您绑定的银行卡中。提现过程安全便捷，全程可追踪。'
  },
  {
    id: 5,
    question: '如果借款方逾期不还，怎么办？',
    answer: '如果借款方出现逾期，我们会立即启动催收程序。同时，我们的风险准备金机制会优先保障投资者的本金和收益安全。对于严重逾期的情况，我们将通过法律途径维护投资者权益。'
  }
])
</script>

<template>
  <div class="security-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="container">
        <h1>安全保障</h1>
        <p>多重安全保障体系，为您的投资保驾护航</p>
      </div>
    </div>
    
    <!-- 安全承诺 -->
    <section class="security-commitment">
      <div class="container">
        <div class="commitment-content">
          <h2>安全承诺</h2>
          <p class="commitment-text">
            威武信贷始终将用户资金安全和信息安全放在首位，建立了完善的风险控制体系和安全保障机制。
            我们承诺：坚守合规经营底线，严格风控审核流程，保障每一位投资者的合法权益。
          </p>
          
          <!-- 安全数据 -->
          <div class="security-stats">
            <div class="stat-item">
              <div class="stat-number">0.01%</div>
              <div class="stat-label">逾期率</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">100%</div>
              <div class="stat-label">本息保障</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">99.99%</div>
              <div class="stat-label">系统可用性</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">24h</div>
              <div class="stat-label">实时监控</div>
            </div>
          </div>
        </div>
      </div>
    </section>
    
    <!-- 安全保障体系 -->
    <section class="security-system">
      <div class="container">
        <h2 class="section-title">安全保障体系</h2>
        
        <div class="security-modules">
          <div 
            v-for="module in securityModules" 
            :key="module.id" 
            class="security-module card"
          >
            <div class="module-header">
              <div class="module-icon">{{ module.icon }}</div>
              <div class="module-title">
                <h3>{{ module.title }}</h3>
                <p>{{ module.description }}</p>
              </div>
            </div>
            
            <div class="module-details">
              <ul class="detail-list">
                <li v-for="(detail, index) in module.details" :key="index">
                  <span class="detail-icon">✓</span>
                  <span>{{ detail }}</span>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </section>
    
    <!-- 平台认证 -->
    <section class="certifications">
      <div class="container">
        <h2 class="section-title">平台认证</h2>
        
        <div class="certification-list">
          <div 
            v-for="cert in certifications" 
            :key="cert.id" 
            class="certification-item card"
          >
            <div class="cert-icon">{{ cert.icon }}</div>
            <div class="cert-info">
              <h3>{{ cert.name }}</h3>
              <p>{{ cert.number }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>
    
    <!-- 安全问答 -->
    <section class="security-faq">
      <div class="container">
        <h2 class="section-title">安全问答</h2>
        
        <div class="faq-list card">
          <div v-for="faq in faqs" :key="faq.id" class="faq-item">
            <h3 class="faq-question">{{ faq.question }}</h3>
            <p class="faq-answer">{{ faq.answer }}</p>
          </div>
        </div>
      </div>
    </section>
    
    <!-- 安全提示 -->
    <section class="security-tips">
      <div class="container">
        <div class="tips-card card">
          <h3>安全提示</h3>
          <ul class="tips-list">
            <li>请勿向他人透露您的账号、密码和验证码等信息</li>
            <li>定期修改密码，使用强密码保护您的账户安全</li>
            <li>请通过官方渠道访问我们的平台，警惕钓鱼网站</li>
            <li>投资有风险，请根据自身风险承受能力选择合适的产品</li>
            <li>如发现可疑情况，请立即联系客服400-123-4567</li>
          </ul>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.security-page {
  padding-bottom: 60px;
}

/* 页面头部 */
.page-header {
  background: linear-gradient(135deg, #1890ff 0%, #40a9ff 100%);
  color: white;
  padding: 80px 0;
  margin-bottom: 40px;
}

.page-header h1 {
  font-size: 40px;
  font-weight: bold;
  margin-bottom: 16px;
}

.page-header p {
  font-size: 18px;
  opacity: 0.9;
}

/* 安全承诺 */
.security-commitment {
  background-color: #f7f7f7;
  padding: 60px 0;
  margin-bottom: 60px;
}

.commitment-content {
  text-align: center;
}

.commitment-content h2 {
  font-size: 32px;
  color: #333;
  margin-bottom: 24px;
}

.commitment-text {
  font-size: 16px;
  color: #666;
  line-height: 1.8;
  max-width: 800px;
  margin: 0 auto 40px;
}

.security-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 30px;
  max-width: 900px;
  margin: 0 auto;
}

.stat-item {
  background-color: white;
  padding: 30px 20px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-number {
  font-size: 36px;
  font-weight: bold;
  color: #1890ff;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 16px;
  color: #666;
}

/* 通用标题 */
.section-title {
  font-size: 32px;
  color: #333;
  text-align: center;
  margin-bottom: 40px;
}

/* 安全保障体系 */
.security-system {
  margin-bottom: 60px;
}

.security-modules {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
}

.security-module {
  transition: transform 0.3s, box-shadow 0.3s;
}

.security-module:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.module-header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 24px;
}

.module-icon {
  font-size: 48px;
}

.module-title h3 {
  font-size: 20px;
  color: #333;
  margin-bottom: 8px;
}

.module-title p {
  color: #666;
  font-size: 14px;
}

.module-details {
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.detail-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.detail-list li {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 16px;
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.detail-list li:last-child {
  margin-bottom: 0;
}

.detail-icon {
  color: #52c41a;
  font-weight: bold;
  margin-top: 2px;
}

/* 平台认证 */
.certifications {
  background-color: #f7f7f7;
  padding: 60px 0;
  margin-bottom: 60px;
}

.certification-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
  max-width: 1000px;
  margin: 0 auto;
}

.certification-item {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 24px;
  background-color: white;
  transition: transform 0.3s, box-shadow 0.3s;
}

.certification-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.cert-icon {
  font-size: 40px;
}

.cert-info h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 8px;
}

.cert-info p {
  color: #666;
  font-size: 14px;
}

/* 安全问答 */
.security-faq {
  margin-bottom: 60px;
}

.faq-list {
  max-width: 800px;
  margin: 0 auto;
}

.faq-item {
  padding: 24px 0;
  border-bottom: 1px solid #f0f0f0;
}

.faq-item:last-child {
  border-bottom: none;
}

.faq-question {
  font-size: 18px;
  color: #333;
  margin-bottom: 16px;
  font-weight: 500;
}

.faq-answer {
  color: #666;
  font-size: 14px;
  line-height: 1.8;
}

/* 安全提示 */
.tips-card {
  max-width: 800px;
  margin: 0 auto;
  padding: 30px;
}

.tips-card h3 {
  font-size: 20px;
  color: #333;
  margin-bottom: 20px;
}

.tips-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.tips-list li {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 16px;
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.tips-list li:last-child {
  margin-bottom: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header {
    padding: 60px 0;
  }
  
  .page-header h1 {
    font-size: 32px;
  }
  
  .section-title {
    font-size: 24px;
  }
  
  .security-stats {
    grid-template-columns: 1fr 1fr;
    gap: 20px;
  }
  
  .stat-number {
    font-size: 28px;
  }
  
  .module-header {
    flex-direction: column;
    text-align: center;
    gap: 16px;
  }
  
  .certification-item {
    flex-direction: column;
    text-align: center;
    gap: 16px;
  }
}

@media (max-width: 480px) {
  .page-header h1 {
    font-size: 28px;
  }
  
  .commitment-text {
    font-size: 14px;
  }
  
  .security-stats {
    grid-template-columns: 1fr;
  }
  
  .certification-list {
    grid-template-columns: 1fr;
  }
  
  .tips-card {
    padding: 20px;
  }
}
</style>