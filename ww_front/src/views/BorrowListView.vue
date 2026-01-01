<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import apiClient from '../utils/api'

const router = useRouter()
const userStore = useUserStore()

// 借款列表数据
const borrowList = ref<any[]>([])

// 加载状态
const isLoading = ref(false)

// 错误信息
const errorMessage = ref('')

// 分页信息
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0
})

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

// 获取借款列表
const fetchBorrowList = async () => {
  if (!userStore.isLoggedIn) {
    router.push('/login')
    return
  }
  
  try {
    isLoading.value = true
    errorMessage.value = ''
    
    const response = await apiClient.get('/borrowInfo/borrower/list')
    
    if (response.data.code === 200) {
      borrowList.value = response.data.data
    } else {
      errorMessage.value = response.data.msg || '获取借款列表失败'
    }
  } catch (error: any) {
    console.error('获取借款列表失败:', error)
    errorMessage.value = error.response?.data?.msg || '网络错误，获取借款列表失败'
  } finally {
    isLoading.value = false
  }
}

// 跳转到借款详情页
const goToBorrowDetail = (id: string | number) => {
  router.push(`/borrow-detail/${id}`)
}

// 跳转到借款申请页
const goToBorrowApply = () => {
  router.push('/borrow-apply')
}

// 页面挂载时获取借款列表
onMounted(() => {
  fetchBorrowList()
})
</script>

<template>
  <div class="borrow-list-page">
    <div class="container">
      <div class="page-header">
        <h1>我的借款</h1>
        <button class="btn btn-primary" @click="goToBorrowApply">
          <span class="icon">+</span>
          我要借款
        </button>
      </div>
      
      <!-- 错误提示 -->
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
      
      <!-- 借款列表 -->
      <div class="borrow-list-container">
        <!-- 列表头部 -->
        <div class="list-header card">
          <div class="list-row header-row">
            <div class="col title-col">借款标题</div>
            <div class="col amount-col">借款金额</div>
            <div class="col rate-col">年化利率</div>
            <div class="col period-col">借款期限</div>
            <div class="col status-col">状态</div>
            <div class="col action-col">操作</div>
          </div>
        </div>
        
        <!-- 列表内容 -->
        <div class="list-content">
          <div v-if="isLoading" class="loading-state">
            <div class="loading-spinner"></div>
            <div class="loading-text">加载中...</div>
          </div>
          
          <div v-else-if="borrowList.length === 0" class="empty-state">
            <div class="empty-icon">📋</div>
            <div class="empty-text">暂无借款记录</div>
            <button class="btn btn-primary" @click="goToBorrowApply">
              立即申请借款
            </button>
          </div>
          
          <div v-else>
            <div v-for="borrow in borrowList" :key="borrow.id" class="list-row card">
              <div class="col title-col">
                <div class="title">{{ borrow.title }}</div>
                <div class="borrow-time">{{ borrow.createTime }}</div>
              </div>
              <div class="col amount-col">
                <div class="amount">¥{{ borrow.amount }}</div>
              </div>
              <div class="col rate-col">
                <div class="rate">{{ borrow.borrowYearRate }}%</div>
              </div>
              <div class="col period-col">
                <div class="period">{{ borrow.period }} 个月</div>
              </div>
              <div class="col status-col">
                <span 
                  class="status-badge" 
                  :style="{ backgroundColor: statusMap[borrow.status?.toString() as keyof typeof statusMap || '0']?.color || '#d9d9d9' }"
                >
                  {{ statusMap[borrow.status?.toString() as keyof typeof statusMap || '0']?.label || '未知状态' }}
                </span>
              </div>
              <div class="col action-col">
                <button 
                  class="btn btn-secondary" 
                  @click="goToBorrowDetail(borrow.id)"
                >
                  详情
                </button>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 分页 -->
        <div v-if="borrowList.length > 0" class="pagination">
          <!-- 这里可以添加分页组件，暂时简化处理 -->
          <div class="pagination-info">
            共 {{ pagination.total }} 条记录，第 {{ pagination.current }} / {{ Math.ceil(pagination.total / pagination.pageSize) }} 页
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.borrow-list-page {
  padding: 20px 0;
  min-height: calc(100vh - 64px);
  background-color: #f5f5f5;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 页面头部 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-header h1 {
  font-size: 24px;
  color: #333;
  margin: 0;
}

.page-header .btn {
  display: flex;
  align-items: center;
}

.page-header .icon {
  margin-right: 8px;
  font-size: 16px;
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

/* 列表容器 */
.borrow-list-container {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* 列表头部 */
.list-header {
  border-bottom: 1px solid #f0f0f0;
}

.list-row {
  display: flex;
  align-items: center;
  padding: 16px 24px;
}

.header-row {
  font-weight: 500;
  color: #8c8c8c;
  font-size: 14px;
  background-color: #fafafa;
}

/* 列表项 */
.list-content .list-row {
  border-bottom: 1px solid #f0f0f0;
  transition: background-color 0.3s;
}

.list-content .list-row:hover {
  background-color: #fafafa;
}

.list-content .list-row:last-child {
  border-bottom: none;
}

/* 列样式 */
.col {
  display: flex;
  align-items: center;
}

.title-col {
  flex: 2;
}

.amount-col,
.rate-col,
.period-col,
.status-col {
  flex: 1;
}

.action-col {
  flex: 0.5;
}

/* 标题列 */
.title {
  font-size: 16px;
  color: #333;
  margin-bottom: 4px;
}

.borrow-time {
  font-size: 12px;
  color: #999;
}

/* 金额列 */
.amount {
  font-size: 16px;
  color: #333;
  font-weight: 500;
}

/* 利率列 */
.rate {
  font-size: 16px;
  color: #ff4d4f;
  font-weight: 500;
}

/* 期限列 */
.period {
  font-size: 16px;
  color: #333;
}

/* 状态列 */
.status-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 14px;
  color: white;
  font-weight: 500;
}

/* 操作列 */
.action-col {
  justify-content: flex-end;
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

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
  text-align: center;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.empty-text {
  font-size: 16px;
  color: #999;
  margin-bottom: 24px;
}

/* 分页 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  border-top: 1px solid #f0f0f0;
  background-color: #fafafa;
}

.pagination-info {
  font-size: 14px;
  color: #8c8c8c;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .list-row {
    padding: 12px 16px;
  }
  
  .title {
    font-size: 14px;
  }
  
  .amount,
  .rate,
  .period {
    font-size: 14px;
  }
  
  .status-badge {
    padding: 3px 8px;
    font-size: 12px;
  }
}

@media (max-width: 768px) {
  .container {
    padding: 0 16px;
  }
  
  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .page-header h1 {
    margin-bottom: 16px;
  }
  
  .list-row {
    flex-wrap: wrap;
    text-align: left;
    padding: 16px;
  }
  
  .header-row {
    display: none;
  }
  
  .col {
    width: 100%;
    justify-content: space-between;
    margin-bottom: 8px;
  }
  
  .col:last-child {
    margin-bottom: 0;
  }
  
  .col::before {
    content: attr(data-label);
    font-size: 12px;
    color: #8c8c8c;
    margin-right: 8px;
  }
  
  .title-col::before { content: '借款标题'; }
  .amount-col::before { content: '借款金额'; }
  .rate-col::before { content: '年化利率'; }
  .period-col::before { content: '借款期限'; }
  .status-col::before { content: '状态'; }
  .action-col::before { content: '操作'; }
  
  .action-col {
    justify-content: flex-start;
  }
}
</style>