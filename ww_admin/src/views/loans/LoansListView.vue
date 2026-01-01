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
          placeholder="搜索贷款ID、用户名称或手机号..."
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
        
        <select v-model="typeFilter">
          <option value="">全部类型</option>
          <option value="small">小额借款</option>
          <option value="medium">中额借款</option>
          <option value="large">大额借款</option>
        </select>
      </div>
    </div>
    
    <!-- 贷款列表表格 -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>贷款ID</th>
            <th>用户信息</th>
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
            <td class="user-info">
              <div class="user-name">{{ loan.userName }}</div>
              <div class="user-phone">{{ loan.userPhone }}</div>
            </td>
            <td class="amount">¥{{ loan.amount.toLocaleString() }}</td>
            <td>{{ loan.term }}期</td>
            <td>{{ loan.interestRate }}%</td>
            <td>{{ formatDate(loan.applicationDate) }}</td>
            <td>
              <span class="status-badge" :class="getStatusClass(loan.status)">
                {{ getStatusText(loan.status) }}
              </span>
            </td>
            <td>
              <div class="action-buttons">
                <button class="action-btn view" @click="viewLoan(loan)">查看详情</button>
                <button 
                  v-if="loan.status === 'pending'"
                  class="action-btn approve" 
                  @click="approveLoan(loan)"
                >
                  批准
                </button>
                <button 
                  v-if="loan.status === 'pending'"
                  class="action-btn reject" 
                  @click="rejectLoan(loan)"
                >
                  拒绝
                </button>
                <button 
                  v-if="loan.status === 'overdue'"
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
      <div v-if="filteredLoans.length === 0" class="empty-state">
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
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

// 定义贷款接口
interface Loan {
  id: string;
  userName: string;
  userPhone: string;
  amount: number;
  term: number;
  interestRate: number;
  applicationDate: string;
  status: 'pending' | 'approved' | 'rejected' | 'active' | 'completed' | 'overdue';
  type: 'small' | 'medium' | 'large';
}

// 模拟贷款数据
const mockLoans: Loan[] = [
  { id: 'LOAN2024001', userName: '张三', userPhone: '138****1234', amount: 5000, term: 6, interestRate: 8.5, applicationDate: '2024-01-15', status: 'active', type: 'small' },
  { id: 'LOAN2024002', userName: '李四', userPhone: '139****5678', amount: 20000, term: 12, interestRate: 7.2, applicationDate: '2024-02-20', status: 'completed', type: 'medium' },
  { id: 'LOAN2024003', userName: '王五', userPhone: '137****9012', amount: 8000, term: 6, interestRate: 8.8, applicationDate: '2024-03-10', status: 'pending', type: 'small' },
  { id: 'LOAN2024004', userName: '赵六', userPhone: '136****3456', amount: 50000, term: 24, interestRate: 6.5, applicationDate: '2024-04-05', status: 'rejected', type: 'large' },
  { id: 'LOAN2024005', userName: '钱七', userPhone: '135****7890', amount: 15000, term: 12, interestRate: 7.5, applicationDate: '2024-05-18', status: 'overdue', type: 'medium' },
  { id: 'LOAN2024006', userName: '孙八', userPhone: '134****2345', amount: 3000, term: 3, interestRate: 9.0, applicationDate: '2024-06-22', status: 'approved', type: 'small' },
  { id: 'LOAN2024007', userName: '周九', userPhone: '133****6789', amount: 30000, term: 18, interestRate: 7.0, applicationDate: '2024-07-30', status: 'active', type: 'large' },
  { id: 'LOAN2024008', userName: '吴十', userPhone: '132****0123', amount: 12000, term: 9, interestRate: 7.8, applicationDate: '2024-08-15', status: 'completed', type: 'medium' },
  { id: 'LOAN2024009', userName: '郑十一', userPhone: '131****4567', amount: 7000, term: 6, interestRate: 8.6, applicationDate: '2024-09-10', status: 'pending', type: 'small' },
  { id: 'LOAN2024010', userName: '王十二', userPhone: '130****8901', amount: 25000, term: 12, interestRate: 7.3, applicationDate: '2024-10-25', status: 'active', type: 'medium' },
]

// 响应式数据
const loans = ref<Loan[]>(mockLoans)
const searchQuery = ref('')
const statusFilter = ref('')
const typeFilter = ref('')
const currentPage = ref(1)
const pageSize = 10

// 计算属性
const filteredLoans = computed(() => {
  return loans.value.filter(loan => {
    const matchesSearch = !searchQuery.value || 
      loan.id.includes(searchQuery.value) ||
      loan.userName.includes(searchQuery.value) ||
      loan.userPhone.includes(searchQuery.value)
    
    const matchesStatus = !statusFilter.value || loan.status === statusFilter.value
    const matchesType = !typeFilter.value || loan.type === typeFilter.value
    
    return matchesSearch && matchesStatus && matchesType
  })
})

const totalPages = computed(() => {
  return Math.ceil(filteredLoans.value.length / pageSize)
})

// 方法
const searchLoans = () => {
  currentPage.value = 1
}

const changePage = (page: number) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
  }
}

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('zh-CN')
}

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    pending: '待审核',
    approved: '已批准',
    rejected: '已拒绝',
    active: '还款中',
    completed: '已完成',
    overdue: '已逾期'
  }
  return statusMap[status] || status
}

const getStatusClass = (status: string) => {
  const classMap: Record<string, string> = {
    pending: 'status-pending',
    approved: 'status-approved',
    rejected: 'status-rejected',
    active: 'status-active',
    completed: 'status-completed',
    overdue: 'status-overdue'
  }
  return classMap[status] || ''
}

const viewLoan = (loan: Loan) => {
  console.log('查看贷款详情:', loan)
  // 这里可以打开贷款详情弹窗
}

const approveLoan = (loan: Loan) => {
  console.log('批准贷款:', loan)
  // 这里可以实现批准贷款的逻辑
}

const rejectLoan = (loan: Loan) => {
  console.log('拒绝贷款:', loan)
  // 这里可以实现拒绝贷款的逻辑
}

const remindPayment = (loan: Loan) => {
  console.log('提醒还款:', loan)
  // 这里可以实现提醒还款的逻辑
}
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

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #64748b;
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