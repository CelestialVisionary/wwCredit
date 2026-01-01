<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import apiClient from '../utils/api'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 借款详情数据
const borrowDetail = ref<any>(null)

// 加载状态
const isLoading = ref(false)

// 错误信息
const errorMessage = ref('')

// 借款ID
const borrowId = ref(route.params.id as string)

// 状态映射
const statusMap = {
  '0': { label: '未认证', color: '#d9d9d9' },
  '1': { label: '审核中', color: '#faad14' },
  '2': { label: '审核通过', color: '#52c41a' },
  '3': { label: '还款中', color: '#1890ff' },
  '4': { label: '已完成', color: '#8c8c8c' },
  '-1': { label: '审核不通过', color: '#ff4d4f' },
  '-2': { label: '已逾期', color: '#ff7875' }
}

// 还款方式映射
const returnMethodMap = {
  '1': '等额本息',
  '2': '等额本金',
  '3': '每月还息一次还本',
  '4': '一次还本'
}

// 获取借款详情
const fetchBorrowDetail = async () => {
  if (!userStore.isLoggedIn) {
    router.push('/login')
    return
  }
  
  try {
    isLoading.value = true
    errorMessage.value = ''
    
    const response = await apiClient.get(`/borrowInfo/detail/${borrowId.value}`)
    
    if (response.data.code === 200) {
      borrowDetail.value = response.data.data
    } else {
      errorMessage.value = response.data.msg || '获取借款详情失败'
    }
  } catch (error: any) {
    console.error('获取借款详情失败:', error)
    errorMessage.value = error.response?.data?.msg || '网络错误，获取借款详情失败'
  } finally {
    isLoading.value = false
  }
}

// 返回列表
const goBackToList = () => {
  router.push('/borrow-list')
}

// 页面挂载时获取借款详情
onMounted(() => {
  fetchBorrowDetail()
})
</script>

<template>
  <div class="borrow-detail-page">
    <div class="container">
      <div class="page-header">
        <button class="back-button" @click="goBackToList">
          <span class="icon">←</span>
          返回列表
        </button>
        <h1>借款详情</h1>
      </div>
      
      <!-- 错误提示 -->
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
      
      <!-- 借款详情内容 -->
      <div v-if="isLoading" class="loading-state">
        <div class="loading-spinner"></div>
        <div class="loading-text">加载中...</div>
      </div>
      
      <div v-else-if="borrowDetail" class="detail-content">
        <!-- 基本信息卡片 -->
        <div class="detail-card card">
          <div class="card-header">
            <h2>基本信息</h2>
          </div>
          <div class="card-content">
            <div class="detail-row">
              <div class="label">借款标题</div>
              <div class="value">{{ borrowDetail.title }}</div>
            </div>
            <div class="detail-row">
              <div class="label">借款金额</div>
              <div class="value amount">{{ borrowDetail.amount }} 元</div>
            </div>
            <div class="detail-row">
              <div class="label">年化利率</div>
              <div class="value rate">{{ borrowDetail.borrowYearRate }}%</div>
            </div>
            <div class="detail-row">
              <div class="label">借款期限</div>
              <div class="value">{{ borrowDetail.period }} 个月</div>
            </div>
            <div class="detail-row">
              <div class="label">还款方式</div>
              <div class="value">{{ returnMethodMap[borrowDetail.returnMethod?.toString() as keyof typeof returnMethodMap || '1'] }}</div>
            </div>
            <div class="detail-row">
              <div class="label">状态</div>
              <div class="value">
                <span 
                  class="status-badge" 
                  :style="{ backgroundColor: statusMap[borrowDetail.status?.toString() as keyof typeof statusMap || '0']?.color || '#d9d9d9' }"
                >
                  {{ statusMap[borrowDetail.status?.toString() as keyof typeof statusMap || '0']?.label || '未知状态' }}
                </span>
              </div>
            </div>
            <div class="detail-row">
              <div class="label">申请时间</div>
              <div class="value">{{ borrowDetail.createTime }}</div>
            </div>
          </div>
        </div>
        
        <!-- 借款详情卡片 -->
        <div class="detail-card card">
          <div class="card-header">
            <h2>借款详情</h2>
          </div>
          <div class="card-content">
            <div class="detail-row">
              <div class="label">资金用途</div>
              <div class="value full-width">{{ borrowDetail.moneyUse }}</div>
            </div>
            <div v-if="borrowDetail.description" class="detail-row">
              <div class="label">借款描述</div>
              <div class="value full-width description">{{ borrowDetail.description }}</div>
            </div>
          </div>
        </div>
        
        <!-- 还款计划卡片 -->
        <div class="detail-card card">
          <div class="card-header">
            <h2>还款计划</h2>
          </div>
          <div class="card-content">
            <div class="no-plan-tip">
              <span class="icon">📅</span>
              <span>还款计划生成中...</span>
            </div>
            <div class="plan-tip">
              <span class="icon">💡</span>
              <span>审核通过后将生成详细的还款计划</span>
            </div>
          </div>
        </div>
        
        <!-- 操作按钮 -->
        <div class="action-buttons">
          <button class="btn btn-secondary" @click="goBackToList">
            返回列表
          </button>
          <!-- 根据状态显示不同的操作按钮 -->
          <button 
            v-if="borrowDetail.status === 1" 
            class="btn btn-warning"
            disabled
          >
            <span class="icon">⏳</span>
            审核中，无法操作
          </button>
          <button 
            v-else-if="borrowDetail.status === -1" 
            class="btn btn-success"
            @click="router.push('/borrow-apply')"
          >
            <span class="icon">🔄</span>
            重新申请
          </button>
          <button 
            v-else-if="borrowDetail.status === 3" 
            class="btn btn-primary"
            disabled
          >
            <span class="icon">💰</span>
            立即还款
          </button>
        </div>
      </div>
      
      <div v-else class="not-found-state">
        <div class="not-found-icon">❌</div>
        <div class="not-found-text">借款信息不存在或已被删除</div>
        <button class="btn btn-primary" @click="goBackToList">
          返回借款列表
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.borrow-detail-page {
  padding: 20px 0;
  min-height: calc(100vh - 64px);
  background-color: #f5f5f5;
}

.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 页面头部 */
.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
}

.back-button {
  background: none;
  border: none;
  color: #1890ff;
  font-size: 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  margin-right: 16px;
  padding: 8px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.back-button:hover {
  background-color: #f0f0f0;
}

.back-button .icon {
  margin-right: 4px;
  font-size: 18px;
}

.page-header h1 {
  font-size: 24px;
  color: #333;
  margin: 0;
}

/* 错误提示 */
.error-message {
  background-color: #fff2f0;
  color: #ff4d4f;
  padding: 12px 16px;
  border-radius: 4px;
  margin-bottom: 20px;
  border: 1px solid #ffccc7;
  font-size: 14px;
}

/* 加载状态 */
.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
}

.loading-spinner {
  border: 3px solid #f0f0f0;
  border-top: 3px solid #1890ff;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin-bottom: 16px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-text {
  color: #999;
  font-size: 16px;
}

/* 详情内容 */
.detail-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 详情卡片 */
.detail-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.card-header {
  padding: 16px 24px;
  border-bottom: 1px solid #f0f0f0;
  background-color: #fafafa;
}

.card-header h2 {
  font-size: 18px;
  color: #333;
  margin: 0;
}

.card-content {
  padding: 24px;
}

/* 详情行 */
.detail-row {
  display: flex;
  margin-bottom: 16px;
  align-items: flex-start;
}

.detail-row:last-child {
  margin-bottom: 0;
}

.detail-row .label {
  width: 120px;
  font-size: 14px;
  color: #8c8c8c;
  font-weight: 500;
  margin-right: 24px;
  text-align: right;
  padding-top: 4px;
}

.detail-row .value {
  flex: 1;
  font-size: 16px;
  color: #333;
  padding-top: 4px;
}

.detail-row .value.full-width {
  width: 100%;
}

.detail-row .value.amount {
  font-size: 20px;
  font-weight: bold;
  color: #1890ff;
}

.detail-row .value.rate {
  font-size: 20px;
  font-weight: bold;
  color: #ff4d4f;
}

.detail-row .value.description {
  white-space: pre-wrap;
  line-height: 1.6;
}

/* 状态标签 */
.status-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 14px;
  color: white;
  font-weight: 500;
}

/* 还款计划提示 */
.no-plan-tip,
.plan-tip {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  color: #8c8c8c;
  font-size: 14px;
  text-align: center;
}

.no-plan-tip .icon,
.plan-tip .icon {
  margin-right: 8px;
  font-size: 20px;
}

.plan-tip {
  padding-top: 0;
  font-size: 12px;
  color: #999;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  gap: 16px;
  justify-content: center;
  margin-top: 20px;
  margin-bottom: 40px;
}

.action-buttons .btn {
  display: flex;
  align-items: center;
  padding: 10px 24px;
}

.action-buttons .icon {
  margin-right: 8px;
}

/* 未找到状态 */
.not-found-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
  text-align: center;
}

.not-found-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.not-found-text {
  font-size: 16px;
  color: #999;
  margin-bottom: 24px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .container {
    padding: 0 16px;
  }
  
  .detail-row {
    flex-direction: column;
  }
  
  .detail-row .label {
    width: 100%;
    text-align: left;
    margin-right: 0;
    margin-bottom: 8px;
  }
  
  .action-buttons {
    flex-direction: column;
  }
  
  .action-buttons .btn {
    width: 100%;
    justify-content: center;
  }
}
</style>