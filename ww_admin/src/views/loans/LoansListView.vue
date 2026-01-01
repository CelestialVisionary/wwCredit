<template>
  <div class="loans-view">
    <div class="page-header">
      <h2 class="page-title">贷款管理</h2>
      <div class="page-actions">
        <button class="btn btn-primary">导出报表</button>
      </div>
    </div>
    
    <!-- 搜索和筛选 -->
    <div class="search-filters">
      <div class="search-input">
        <input 
          type="text" 
          v-model="searchQuery" 
          placeholder="搜索贷款ID或标题..."
          @keyup.enter="searchLoans"
        />
        <button class="search-btn" @click="searchLoans">搜索</button>
      </div>
      
      <div class="filter-options">
        <select v-model="statusFilter">
          <option value="">全部状态</option>
          <option value="pending">待审核</option>
          <option value="approved">已批准</option>
          <option value="rejected">已拒绝</option>
          <option value="active">还款中</option>
          <option value="completed">已完成</option>
          <option value="overdue">已逾期</option>
        </select>
      </div>
    </div>
    
    <!-- 贷款列表表格 -->
    <div class="table-container">
      <!-- 加载状态 -->
      <div v-if="isLoading" class="loading-state">
        <div class="loading-spinner"></div>
        <p>加载中...</p>
      </div>
      
      <!-- 数据表格 -->
      <table v-else class="data-table">
        <thead>
          <tr>
            <th>贷款ID</th>
            <th>贷款标题</th>
            <th>贷款金额</th>
            <th>期限</th>
            <th>利率</th>
            <th>申请时间</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="loan in filteredLoans" :key="loan.id">
            <td>{{ loan.id }}</td>
            <td>{{ loan.title }}</td>
            <td class="amount">¥{{ loan.amount?.toLocaleString() }}</td>
            <td>{{ loan.period }}期</td>
            <td>{{ loan.borrowYearRate }}%</td>
            <td>{{ formatDate(loan.createTime) }}</td>
            <td>
              <span class="status-badge" :class="getStatusClass(loan.status)">
                {{ getStatusText(loan.status) }}
              </span>
            </td>
            <td>
              <div class="action-buttons">
                <button class="action-btn view" @click="viewLoan(loan)">查看详情</button>
                <button 
                  v-if="loan.status === 1"
                  class="action-btn approve" 
                  @click="approveLoan(loan)"
                >
                  批准
                </button>
                <button 
                  v-if="loan.status === 1"
                  class="action-btn reject" 
                  @click="rejectLoan(loan)"
                >
                  拒绝
                </button>
                <button 
                  v-if="loan.status === -2"
                  class="action-btn remind" 
                  @click="remindPayment(loan)"
                >
                  提醒还款
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      
      <!-- 空状态 -->
      <div v-if="!isLoading && filteredLoans.length === 0" class="empty-state">
        <p>暂无贷款数据</p>
      </div>
    </div>
    
    <!-- 分页 -->
    <div class="pagination">
      <button 
        class="page-btn" 
        :disabled="currentPage === 1"
        @click="changePage(currentPage - 1)"
      >
        上一页
      </button>
      
      <span class="page-info">第 {{ currentPage }} / {{ totalPages }} 页</span>
      
      <button 
        class="page-btn" 
        :disabled="currentPage === totalPages"
        @click="changePage(currentPage + 1)"
      >
        下一页
      </button>
    </div>
    
    <!-- 贷款详情弹窗 -->
    <div v-if="showDetailModal" class="modal-overlay" @click="showDetailModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>贷款详情</h3>
          <button class="close-btn" @click="showDetailModal = false">×</button>
        </div>
        <div class="modal-body" v-if="selectedLoan">
          <div class="detail-row">
            <div class="detail-label">贷款ID</div>
            <div class="detail-value">{{ selectedLoan.id }}</div>
          </div>
          <div class="detail-row">
            <div class="detail-label">贷款标题</div>
            <div class="detail-value">{{ selectedLoan.title }}</div>
          </div>
          <div class="detail-row">
            <div class="detail-label">用户ID</div>
            <div class="detail-value">{{ selectedLoan.userId }}</div>
          </div>
          <div class="detail-row">
            <div class="detail-label">贷款金额</div>
            <div class="detail-value">¥{{ selectedLoan.amount?.toLocaleString() }}</div>
          </div>
          <div class="detail-row">
            <div class="detail-label">贷款期限</div>
            <div class="detail-value">{{ selectedLoan.period }} 个月</div>
          </div>
          <div class="detail-row">
            <div class="detail-label">年化利率</div>
            <div class="detail-value">{{ selectedLoan.borrowYearRate }}%</div>
          </div>
          <div class="detail-row">
            <div class="detail-label">申请时间</div>
            <div class="detail-value">{{ formatDate(selectedLoan.createTime) }}</div>
          </div>
          <div class="detail-row">
            <div class="detail-label">贷款状态</div>
            <div class="detail-value">
              <span class="status-badge" :class="getStatusClass(selectedLoan.status)">
                {{ getStatusText(selectedLoan.status) }}
              </span>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="showDetailModal = false">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import axios from '../../utils/axios.config'

// 定义贷款接口
interface Loan {
  id: string;
  title: string;
  amount: number;
  period: number;
  borrowYearRate: number;
  createTime: string;
  status: number;
  userId: number;
  // 其他字段
}

// 定义API响应接口
interface ApiResponse {
  code: number;
  msg: string;
  data: any;
}

// 响应式数据
const loans = ref<Loan[]>([])
const searchQuery = ref('')
const statusFilter = ref('')
const currentPage = ref(1)
const pageSize = 10
const isLoading = ref(false)
const totalRecords = ref(0)

// 计算属性
const filteredLoans = computed(() => {
  return loans.value
})

const totalPages = computed(() => {
  return Math.ceil(totalRecords.value / pageSize)
})

// 状态转换函数：前端状态转后端状态码
const convertStatusToBackend = (status: string): number | null => {
  const statusMap: Record<string, number> = {
    pending: 1,    // 待审核
    approved: 2,   // 审核通过
    rejected: -1,  // 审核不通过
    active: 3,     // 还款中
    completed: 4,  // 已完成
    overdue: -2    // 已逾期
  }
  return status ? statusMap[status] : null
}

// 方法
const fetchLoans = async () => {
  isLoading.value = true
  try {
    // 将前端状态转换为后端状态码
    const backendStatus = convertStatusToBackend(statusFilter.value)
    
    const response = await axios.get<ApiResponse>('/api/core/borrowInfo/list', {
      params: {
        current: currentPage.value,
        limit: pageSize,
        keyword: searchQuery.value,
        status: backendStatus
      }
    })
    
    if (response.data.code === 200) {
      loans.value = response.data.data.records
      totalRecords.value = response.data.data.total
    }
  } catch (error) {
    console.error('获取贷款列表失败:', error)
  } finally {
    isLoading.value = false
  }
}

const searchLoans = () => {
  currentPage.value = 1
  fetchLoans()
}

const changePage = (page: number) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
    fetchLoans()
  }
}

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('zh-CN')
}

const getStatusText = (status: number) => {
  const statusMap: Record<number, string> = {
    0: '未认证',
    1: '审核中',
    2: '审核通过',
    3: '还款中',
    4: '已完成',
    '-1': '审核不通过',
    '-2': '已逾期'
  } as any
  return statusMap[status] || '未知状态'
}

const getStatusClass = (status: number) => {
  const statusMap: Record<number, string> = {
    0: 'status-pending',
    1: 'status-pending',
    2: 'status-approved',
    3: 'status-active',
    4: 'status-completed',
    '-1': 'status-rejected',
    '-2': 'status-overdue'
  } as any
  return statusMap[status] || ''
}

// 贷款详情弹窗相关
const showDetailModal = ref(false)
const selectedLoan = ref<Loan | null>(null)

const viewLoan = (loan: Loan) => {
  selectedLoan.value = loan
  showDetailModal.value = true
}

const approveLoan = async (loan: Loan) => {
  try {
    await axios.post<ApiResponse>('/api/core/borrowInfo/approval', {
      id: loan.id,
      status: 2 // 审核通过
    })
    // 重新获取贷款列表
    fetchLoans()
  } catch (error) {
    console.error('批准贷款失败:', error)
  }
}

const rejectLoan = async (loan: Loan) => {
  try {
    await axios.post<ApiResponse>('/api/core/borrowInfo/approval', {
      id: loan.id,
      status: -1 // 审核不通过
    })
    // 重新获取贷款列表
    fetchLoans()
  } catch (error) {
    console.error('拒绝贷款失败:', error)
  }
}

const remindPayment = (loan: Loan) => {
  console.log('提醒还款:', loan)
  // 这里可以实现提醒还款的逻辑
}

// 页面挂载时获取贷款列表
onMounted(() => {
  fetchLoans()
})
</script>

<style scoped>
.loans-view {
  padding: 20px 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-title {
  font-size: 24px;
  color: #1e293b;
}

.search-filters {
  display: flex;
  gap: 20px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.search-input {
  flex: 1;
  display: flex;
  gap: 10px;
}

.search-input input {
  flex: 1;
  padding: 10px 16px;
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.3s;
}

.search-input input:focus {
  outline: none;
  border-color: #3b82f6;
}

.search-btn {
  padding: 10px 20px;
  background-color: #3b82f6;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.search-btn:hover {
  background-color: #2563eb;
}

.filter-options {
  display: flex;
  gap: 10px;
}

.filter-options select {
  padding: 10px 16px;
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  font-size: 14px;
  background-color: white;
  cursor: pointer;
  transition: border-color 0.3s;
}

.filter-options select:focus {
  outline: none;
  border-color: #3b82f6;
}

.table-container {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #e2e8f0;
}

.data-table th {
  background-color: #f8fafc;
  font-weight: 600;
  color: #64748b;
  font-size: 14px;
}

.data-table td {
  color: #1e293b;
  font-size: 14px;
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-name {
  font-weight: 500;
}

.user-phone {
  color: #64748b;
  font-size: 13px;
}

.amount {
  font-weight: 600;
  color: #3b82f6;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-pending {
  background-color: #fef3c7;
  color: #d97706;
}

.status-approved {
  background-color: #d1fae5;
  color: #059669;
}

.status-rejected {
  background-color: #fee2e2;
  color: #dc2626;
}

.status-active {
  background-color: #dbeafe;
  color: #1d4ed8;
}

.status-completed {
  background-color: #dcfce7;
  color: #16a34a;
}

.status-overdue {
  background-color: #fee2e2;
  color: #dc2626;
  font-weight: 600;
}

.action-buttons {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.action-btn {
  padding: 6px 12px;
  border: 1px solid #e2e8f0;
  background-color: white;
  border-radius: 4px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.3s;
}

.action-btn:hover {
  background-color: #f8fafc;
}

.action-btn.view {
  color: #3b82f6;
  border-color: #dbeafe;
}

.action-btn.view:hover {
  background-color: #dbeafe;
}

.action-btn.approve {
  color: #10b981;
  border-color: #d1fae5;
}

.action-btn.approve:hover {
  background-color: #d1fae5;
}

.action-btn.reject {
  color: #ef4444;
  border-color: #fee2e2;
}

.action-btn.reject:hover {
  background-color: #fee2e2;
}

.action-btn.remind {
  color: #f59e0b;
  border-color: #fde68a;
}

.action-btn.remind:hover {
  background-color: #fde68a;
}

.loading-state {
  text-align: center;
  padding: 60px 20px;
  color: #64748b;
}

.loading-spinner {
  display: inline-block;
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3b82f6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 16px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #64748b;
}

/* 弹窗样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #e2e8f0;
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  color: #64748b;
  cursor: pointer;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.close-btn:hover {
  background-color: #f1f5f9;
}

.modal-body {
  padding: 20px;
  overflow-y: auto;
  flex: 1;
}

.detail-row {
  display: flex;
  margin-bottom: 16px;
  align-items: center;
}

.detail-row:last-child {
  margin-bottom: 0;
}

.detail-label {
  width: 100px;
  font-weight: 500;
  color: #64748b;
  font-size: 14px;
}

.detail-value {
  flex: 1;
  font-size: 14px;
  color: #1e293b;
}

.modal-footer {
  padding: 16px 20px;
  border-top: 1px solid #e2e8f0;
  text-align: right;
}

.modal-footer .btn {
  padding: 8px 16px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-secondary {
  background-color: #f1f5f9;
  color: #334155;
  border: 1px solid #e2e8f0;
}

.btn-secondary:hover {
  background-color: #e2e8f0;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-top: 30px;
}

.page-btn {
  padding: 8px 16px;
  border: 1px solid #e2e8f0;
  background-color: white;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.page-btn:hover:not(:disabled) {
  background-color: #f8fafc;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  font-size: 14px;
  color: #64748b;
}

@media (max-width: 768px) {
  .search-filters {
    flex-direction: column;
  }
  
  .search-input {
    width: 100%;
  }
}
</style>