<template>
  <div class="dashboard">
    <h2 class="page-title">数据概览</h2>
    
    <!-- 统计卡片区域 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">👥</div>
        <div class="stat-content">
          <div class="stat-number">{{ totalUsers.toLocaleString() }}</div>
          <div class="stat-label">总用户数</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">📊</div>
        <div class="stat-content">
          <div class="stat-number">{{ userRatio }}</div>
          <div class="stat-label">用户男女比例</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">🔄</div>
        <div class="stat-content">
          <div class="stat-number">{{ totalTransactions }}</div>
          <div class="stat-label">总交易数</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">💰</div>
        <div class="stat-content">
          <div class="stat-number">{{ totalLoanAmount }}万</div>
          <div class="stat-label">总借款额</div>
        </div>
      </div>
    </div>
    
    <!-- 活跃度统计 -->
    <div class="activity-stats">
      <div class="stat-card">
        <div class="stat-icon">📈</div>
        <div class="stat-content">
          <div class="stat-number">{{ dailyActiveUsers.toLocaleString() }}</div>
          <div class="stat-label">日活用户</div>
          <div class="stat-trend positive">
            <span class="trend-icon">↑</span>
            <span class="trend-value">24%</span>
            <span class="trend-label">昨日同比</span>
          </div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">📅</div>
        <div class="stat-content">
          <div class="stat-number">{{ monthlyActiveUsers.toLocaleString() }}</div>
          <div class="stat-label">月活跃用户</div>
          <div class="stat-trend negative">
            <span class="trend-icon">↓</span>
            <span class="trend-value">12%</span>
            <span class="trend-label">上月环比</span>
          </div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">⚡</div>
        <div class="stat-content">
          <div class="stat-number">{{ todayNewTransactions.toLocaleString() }}</div>
          <div class="stat-label">今日新增交易</div>
          <div class="stat-trend positive">
            <span class="trend-icon">↑</span>
            <span class="trend-value">16%</span>
            <span class="trend-label">相比昨日</span>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 图表区域 -->
    <div class="charts-container">
      <div class="chart-card">
        <div class="chart-header">
          <h3>用户增长趋势</h3>
          <div class="chart-filters">
            <button class="filter-btn active">本周</button>
            <button class="filter-btn">本月</button>
            <button class="filter-btn">全年</button>
          </div>
        </div>
        <div class="chart-content">
          <!-- 简化的图表表示 -->
          <div class="chart-placeholder">
            <div class="line-chart">
              <div class="chart-grid"></div>
              <div class="chart-line"></div>
              <div class="chart-labels">
                <div class="x-labels">
                  <span>周一</span>
                  <span>周二</span>
                  <span>周三</span>
                  <span>周四</span>
                  <span>周五</span>
                  <span>周六</span>
                  <span>周日</span>
                </div>
                <div class="y-labels">
                  <span>3,000</span>
                  <span>3,250</span>
                  <span>3,500</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="chart-card">
        <div class="chart-header">
          <h3>借款分布情况</h3>
        </div>
        <div class="chart-content">
          <!-- 简化的饼图表示 -->
          <div class="pie-chart-placeholder">
            <div class="pie-chart">
              <div class="pie-slice slice-small"></div>
              <div class="pie-slice slice-medium"></div>
              <div class="pie-slice slice-large"></div>
            </div>
            <div class="pie-legend">
              <div class="legend-item">
                <span class="legend-color small"></span>
                <span>小额借款</span>
              </div>
              <div class="legend-item">
                <span class="legend-color medium"></span>
                <span>中额借款</span>
              </div>
              <div class="legend-item">
                <span class="legend-color large"></span>
                <span>大额借款</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// 模拟统计数据
const totalUsers = 268500
const userRatio = '138/100'
const totalTransactions = 99
const totalLoanAmount = '562'
const dailyActiveUsers = 98500
const monthlyActiveUsers = 693700
const todayNewTransactions = 72000
</script>

<style scoped>
.dashboard {
  padding: 20px 0;
}

.page-title {
  font-size: 24px;
  color: #1e293b;
  margin-bottom: 24px;
}

.stats-grid,
.activity-stats {
  display: grid;
  gap: 20px;
  margin-bottom: 30px;
}

.stats-grid {
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
}

.activity-stats {
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
}

.stat-card {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-icon {
  font-size: 36px;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f8fafc;
  border-radius: 12px;
}

.stat-content {
  flex: 1;
}

.stat-number {
  font-size: 28px;
  font-weight: 700;
  color: #1e293b;
  line-height: 1.2;
}

.stat-label {
  font-size: 14px;
  color: #64748b;
  margin-top: 4px;
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  margin-top: 8px;
  font-size: 14px;
}

.stat-trend.positive {
  color: #10b981;
}

.stat-trend.negative {
  color: #ef4444;
}

.trend-icon {
  font-size: 16px;
}

.charts-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.chart-card {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.chart-header h3 {
  font-size: 18px;
  color: #1e293b;
}

.chart-filters {
  display: flex;
  gap: 8px;
}

.filter-btn {
  padding: 6px 12px;
  border: 1px solid #e2e8f0;
  background-color: white;
  border-radius: 6px;
  font-size: 14px;
  color: #64748b;
  cursor: pointer;
  transition: all 0.3s;
}

.filter-btn:hover {
  border-color: #cbd5e1;
}

.filter-btn.active {
  background-color: #3b82f6;
  border-color: #3b82f6;
  color: white;
}

.chart-content {
  height: 300px;
  position: relative;
}

.chart-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.line-chart {
  width: 100%;
  height: 100%;
  position: relative;
}

.chart-grid {
  width: 100%;
  height: 100%;
  background-image: linear-gradient(to right, #f1f5f9 1px, transparent 1px),
                    linear-gradient(to bottom, #f1f5f9 1px, transparent 1px);
  background-size: 20% 33.33%;
}

.chart-line {
  position: absolute;
  bottom: 40px;
  left: 0;
  width: 100%;
  height: calc(100% - 60px);
  background: linear-gradient(to right, transparent calc(100% - 2px), transparent) bottom / 100% calc(300px - 80px);
  background-repeat: no-repeat;
  mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Cpath d='M0,80 C20,60 40,75 60,50 80,30 100,20 100,20' stroke='black' fill='none' stroke-width='2'/%3E%3C/svg%3E");
  mask-size: 100% 100%;
  mask-position: center;
  mask-repeat: no-repeat;
  background-color: #3b82f6;
}

.chart-labels {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-between;
}

.x-labels {
  position: absolute;
  bottom: -30px;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #64748b;
}

.y-labels {
  position: absolute;
  top: 0;
  left: -30px;
  bottom: 0;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  font-size: 12px;
  color: #64748b;
}

.pie-chart-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 20px;
}

.pie-chart {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  position: relative;
  overflow: hidden;
  border: 8px solid #f1f5f9;
}

.pie-slice {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  transform-origin: center;
}

.slice-small {
  background-color: #3b82f6;
  transform: rotate(120deg);
}

.slice-medium {
  background-color: #10b981;
  transform: rotate(120deg) skew(120deg);
}

.slice-large {
  background-color: #f59e0b;
  transform: rotate(240deg) skew(120deg);
}

.pie-legend {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #64748b;
}

.legend-color {
  width: 16px;
  height: 16px;
  border-radius: 4px;
}

.legend-color.small {
  background-color: #3b82f6;
}

.legend-color.medium {
  background-color: #10b981;
}

.legend-color.large {
  background-color: #f59e0b;
}

@media (max-width: 768px) {
  .charts-container {
    grid-template-columns: 1fr;
  }
}
</style>