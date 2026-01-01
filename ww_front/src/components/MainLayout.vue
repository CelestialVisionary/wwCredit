<script setup lang="ts">
import NavBar from './NavBar.vue'
</script>

<template>
  <div class="main-layout">
    <NavBar />
    <main class="main-content">
      <slot></slot>
    </main>
    <footer class="footer">
      <div class="container">
        <div class="footer-content">
          <div class="footer-section">
            <h3>威武信贷</h3>
            <p>让金融服务更简单</p>
            <div class="contact-info">
              <p>客服热线：400-123-4567</p>
              <p>工作时间：周一至周五 9:00-18:00</p>
            </div>
          </div>
          <div class="footer-section">
            <h4>产品服务</h4>
            <ul>
              <li>个人贷款</li>
              <li>企业融资</li>
              <li>投资理财产品</li>
              <li>金融顾问服务</li>
            </ul>
          </div>
          <div class="footer-section">
            <h4>关于我们</h4>
            <ul>
              <li>公司简介</li>
              <li>团队介绍</li>
              <li>媒体报道</li>
              <li>加入我们</li>
            </ul>
          </div>
          <div class="footer-section">
            <h4>法律条款</h4>
            <ul>
              <li>隐私政策</li>
              <li>用户协议</li>
              <li>风险提示</li>
              <li>免责声明</li>
            </ul>
          </div>
        </div>
        <div class="copyright">
          <p>© 2025 威武信贷. 保留所有权利.</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<style scoped>
.main-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.main-content {
  flex: 1;
  padding: 20px 0;
}

.footer {
  background-color: #f5f5f5;
  padding: 40px 0;
  color: #666;
  margin-top: 40px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
  margin-bottom: 30px;
}

.footer-section h3 {
  color: #333;
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 18px;
}

.footer-section h4 {
  color: #333;
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 16px;
}

.footer-section p {
  margin: 8px 0;
  font-size: 14px;
  line-height: 1.6;
}

.footer-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-section li {
  margin: 8px 0;
  font-size: 14px;
  cursor: pointer;
  transition: color 0.3s;
}

.footer-section li:hover {
  color: #1890ff;
}

.contact-info {
  margin-top: 15px;
}

.copyright {
  border-top: 1px solid #e8e8e8;
  padding-top: 20px;
  text-align: center;
  font-size: 13px;
}

@media (max-width: 768px) {
  .footer-content {
    grid-template-columns: 1fr;
    gap: 20px;
  }
}
</style>