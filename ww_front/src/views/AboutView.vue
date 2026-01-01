<script setup lang="ts">
import { ref } from 'vue'

// 公司简介
const companyInfo = ref({
  name: '威武信贷控股集团有限公司',
  established: '2018年',
  registeredCapital: '1亿元人民币',
  description: '威武信贷是一家专注于为企业和个人提供专业金融信贷服务的平台，拥有丰富的行业经验和专业的团队。',
  vision: '成为最值得信赖的金融科技服务平台',
  mission: '让金融服务更简单、更透明、更普惠'
})

// 团队成员
const teamMembers = ref([
  {
    id: 1,
    name: '张三',
    position: 'CEO',
    background: '15年金融行业经验，曾任职于国内知名银行和互联网金融公司，拥有丰富的金融风险管理和平台运营经验。',
    avatar: '/images/avatar1.svg'
  },
  {
    id: 2,
    name: '李四',
    position: 'CTO',
    background: '12年互联网技术经验，曾主导多个大型金融科技平台的技术架构设计和开发，在分布式系统和大数据分析方面有深厚造诣。',
    avatar: '/images/avatar2.svg'
  },
  {
    id: 3,
    name: '王五',
    position: '风控总监',
    background: '10年风险管理经验，持有FRM认证，擅长构建全面的风险管理体系和风险评估模型。',
    avatar: '/images/avatar3.svg'
  },
  {
    id: 4,
    name: '赵六',
    position: '运营总监',
    background: '8年互联网金融运营经验，在用户增长和运营策略方面有丰富经验，曾成功主导多个金融产品的市场推广。',
    avatar: '/images/avatar4.svg'
  }
])

// 发展历程
const developmentHistory = ref([
  {
    year: '2018',
    events: ['公司成立', '获得A轮融资']
  },
  {
    year: '2019',
    events: ['平台上线', '用户突破10万', '交易额突破1亿']
  },
  {
    year: '2020',
    events: ['获得B轮融资', '完成银行资金存管对接', '风控系统全面升级']
  },
  {
    year: '2021',
    events: ['用户突破100万', '交易额突破50亿', '获得多项金融科技奖项']
  },
  {
    year: '2022',
    events: ['推出多项创新金融产品', '完成C轮融资', '国际化战略启动']
  },
  {
    year: '2023',
    events: ['用户突破200万', '交易额突破100亿', '金融科技研发中心成立']
  }
])

// 合作伙伴
const partners = ref([
  {
    id: 1,
    name: '工商银行',
    logo: '/images/partner1.svg'
  },
  {
    id: 2,
    name: '建设银行',
    logo: '/images/partner2.svg'
  },
  {
    id: 3,
    name: '腾讯云',
    logo: '/images/partner3.svg'
  },
  {
    id: 4,
    name: '阿里云',
    logo: '/images/partner4.svg'
  },
  {
    id: 5,
    name: '平安保险',
    logo: '/images/partner5.svg'
  }
])

// 联系信息
const contactInfo = ref({
  address: '北京市朝阳区金融街88号威武信贷中心25层',
  phone: '400-123-4567',
  email: 'contact@wwfinance.com',
  businessHours: '周一至周五 9:00-18:00'
})
</script>

<template>
  <div class="about-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="container">
        <h1>关于我们</h1>
        <p>了解威武信贷的发展历程、企业文化和团队理念</p>
      </div>
    </div>
    
    <!-- 公司简介 -->
    <section class="company-intro">
      <div class="container">
        <h2 class="section-title">公司简介</h2>
        <div class="intro-content card">
          <div class="intro-text">
            <h3>{{ companyInfo.name }}</h3>
            <div class="company-meta">
              <span>成立时间：{{ companyInfo.established }}</span>
              <span>注册资本：{{ companyInfo.registeredCapital }}</span>
            </div>
            <p class="company-desc">{{ companyInfo.description }}</p>
            <div class="company-vision-mission">
              <div class="vision">
                <h4>企业愿景</h4>
                <p>{{ companyInfo.vision }}</p>
              </div>
              <div class="mission">
                <h4>企业使命</h4>
                <p>{{ companyInfo.mission }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    
    <!-- 团队介绍 -->
    <section class="team-section">
      <div class="container">
        <h2 class="section-title">核心团队</h2>
        <div class="team-members">
          <div v-for="member in teamMembers" :key="member.id" class="team-member card">
            <div class="member-avatar">
              <div class="avatar-placeholder">{{ member.name.substring(0, 1) }}</div>
            </div>
            <div class="member-info">
              <h3>{{ member.name }}</h3>
              <p class="member-position">{{ member.position }}</p>
              <p class="member-background">{{ member.background }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>
    
    <!-- 发展历程 -->
    <section class="history-section">
      <div class="container">
        <h2 class="section-title">发展历程</h2>
        <div class="timeline">
          <div 
            v-for="(item, index) in developmentHistory" 
            :key="index" 
            class="timeline-item"
          >
            <div class="timeline-year">{{ item.year }}</div>
            <div class="timeline-content card">
              <ul class="events-list">
                <li v-for="(event, eventIndex) in item.events" :key="eventIndex">{{ event }}</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </section>
    
    <!-- 合作伙伴 -->
    <section class="partners-section">
      <div class="container">
        <h2 class="section-title">合作伙伴</h2>
        <div class="partners-list">
          <div v-for="partner in partners" :key="partner.id" class="partner-item card">
            <div class="partner-logo">{{ partner.name }}</div>
          </div>
        </div>
      </div>
    </section>
    
    <!-- 联系我们 -->
    <section class="contact-section">
      <div class="container">
        <h2 class="section-title">联系我们</h2>
        <div class="contact-info card">
          <div class="contact-item">
            <div class="contact-icon">📍</div>
            <div class="contact-content">
              <h4>公司地址</h4>
              <p>{{ contactInfo.address }}</p>
            </div>
          </div>
          <div class="contact-item">
            <div class="contact-icon">📞</div>
            <div class="contact-content">
              <h4>客服电话</h4>
              <p>{{ contactInfo.phone }}</p>
            </div>
          </div>
          <div class="contact-item">
            <div class="contact-icon">✉️</div>
            <div class="contact-content">
              <h4>电子邮箱</h4>
              <p>{{ contactInfo.email }}</p>
            </div>
          </div>
          <div class="contact-item">
            <div class="contact-icon">⏰</div>
            <div class="contact-content">
              <h4>工作时间</h4>
              <p>{{ contactInfo.businessHours }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.about-page {
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

/* 通用标题 */
.section-title {
  font-size: 32px;
  color: #333;
  text-align: center;
  margin-bottom: 40px;
}

/* 公司简介 */
.company-intro {
  margin-bottom: 60px;
}

.intro-content {
  padding: 30px;
}

.intro-text h3 {
  font-size: 24px;
  color: #333;
  margin-bottom: 16px;
}

.company-meta {
  display: flex;
  gap: 30px;
  margin-bottom: 24px;
  font-size: 14px;
  color: #666;
}

.company-desc {
  font-size: 16px;
  color: #333;
  line-height: 1.8;
  margin-bottom: 32px;
}

.company-vision-mission {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 30px;
}

.vision h4, .mission h4 {
  font-size: 18px;
  color: #1890ff;
  margin-bottom: 12px;
}

.vision p, .mission p {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

/* 团队介绍 */
.team-section {
  margin-bottom: 60px;
}

.team-members {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
}

.team-member {
  display: flex;
  gap: 20px;
  padding: 24px;
  transition: transform 0.3s, box-shadow 0.3s;
}

.team-member:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.member-avatar {
  width: 80px;
  height: 80px;
  flex-shrink: 0;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background-color: #1890ff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  font-weight: bold;
}

.member-info h3 {
  font-size: 20px;
  color: #333;
  margin-bottom: 4px;
}

.member-position {
  font-size: 14px;
  color: #1890ff;
  margin-bottom: 12px;
  font-weight: 500;
}

.member-background {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

/* 发展历程 */
.history-section {
  background-color: #f7f7f7;
  padding: 60px 0;
  margin-bottom: 60px;
}

.timeline {
  position: relative;
  max-width: 800px;
  margin: 0 auto;
}

.timeline::before {
  content: '';
  position: absolute;
  top: 0;
  bottom: 0;
  left: 50px;
  width: 2px;
  background-color: #d9d9d9;
}

.timeline-item {
  position: relative;
  padding-left: 100px;
  margin-bottom: 40px;
}

.timeline-item:last-child {
  margin-bottom: 0;
}

.timeline-year {
  position: absolute;
  left: 0;
  top: 0;
  width: 80px;
  height: 40px;
  background-color: #1890ff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: bold;
  border-radius: 4px;
}

.timeline-year::after {
  content: '';
  position: absolute;
  right: -8px;
  top: 16px;
  width: 0;
  height: 0;
  border-style: solid;
  border-width: 6px 0 6px 8px;
  border-color: transparent transparent transparent #1890ff;
}

.timeline-content {
  padding: 20px;
}

.events-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.events-list li {
  font-size: 14px;
  color: #333;
  line-height: 1.6;
  margin-bottom: 12px;
  position: relative;
  padding-left: 20px;
}

.events-list li:last-child {
  margin-bottom: 0;
}

.events-list li::before {
  content: '✓';
  position: absolute;
  left: 0;
  top: 2px;
  color: #52c41a;
  font-weight: bold;
}

/* 合作伙伴 */
.partners-section {
  margin-bottom: 60px;
}

.partners-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 24px;
  max-width: 1000px;
  margin: 0 auto;
}

.partner-item {
  padding: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.3s, box-shadow 0.3s;
}

.partner-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.partner-logo {
  font-size: 18px;
  color: #333;
  font-weight: 500;
}

/* 联系我们 */
.contact-section {
  margin-bottom: 60px;
}

.contact-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
  padding: 30px;
}

.contact-item {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

.contact-icon {
  font-size: 32px;
  margin-top: 4px;
}

.contact-content h4 {
  font-size: 18px;
  color: #333;
  margin-bottom: 12px;
}

.contact-content p {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
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
  
  .company-vision-mission {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .timeline::before {
    left: 40px;
  }
  
  .timeline-item {
    padding-left: 80px;
  }
  
  .timeline-year {
    width: 60px;
    height: 35px;
    font-size: 16px;
  }
  
  .contact-info {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .page-header h1 {
    font-size: 28px;
  }
  
  .intro-content {
    padding: 20px;
  }
  
  .team-member {
    flex-direction: column;
    text-align: center;
  }
  
  .member-avatar {
    width: 100px;
    height: 100px;
    margin: 0 auto 20px;
  }
  
  .partners-list {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>