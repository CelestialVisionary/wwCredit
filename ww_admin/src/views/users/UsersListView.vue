<template>
  <div class="users-view">
    <div class="page-header">
      <h2 class="page-title">用户管理</h2>
      <div class="page-actions">
        <button class="btn btn-primary">导出用户数据</button>
      </div>
    </div>
    
    <!-- 搜索和筛选 -->
    <div class="search-filters">
      <div class="search-input">
        <input 
          type="text" 
          v-model="adminUserQuery.keyword" 
          placeholder="搜索用户姓名、手机号..."
          @keyup.enter="searchUsers"
        />
        <button class="search-btn" @click="searchUsers">搜索</button>
      </div>
      
      <div class="filter-options">
        <select v-model="adminUserQuery.status">
          <option value="">全部状态</option>
          <option value="1">正常</option>
          <option value="0">锁定</option>
        </select>
      </div>
    </div>
    
    <!-- 用户列表表格 -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>用户ID</th>
            <th>用户姓名</th>
            <th>手机号</th>
            <th>创建时间</th>
            <th>更新时间</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in pageModel.records" :key="user.id">
            <td>{{ user.id }}</td>
            <td class="user-name">{{ user.name }}</td>
            <td>{{ user.mobile }}</td>
            <td>{{ formatDate(user.createTime) }}</td>
            <td>{{ formatDate(user.updateTime) }}</td>
            <td>
              <span class="status-badge" :class="getStatusClass(user.status)">
                {{ getStatusText(user.status) }}
              </span>
            </td>
            <td>
              <div class="action-buttons">
                <button class="action-btn view" @click="viewUser(user)">查看</button>
                <button 
                  class="action-btn block" 
                  :class="{ 'unblock': user.status === 0 }"
                  @click="toggleBlockUser(user)"
                >
                  {{ user.status === 0 ? '解锁' : '锁定' }}
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      
      <!-- 空状态 -->
      <div v-if="pageModel.records.length === 0" class="empty-state">
        <p>暂无用户数据</p>
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
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

// 定义查询参数接口
interface AdminUserQuery {
  keyword?: string
  status?: string
}

// 定义用户接口
interface User {
  id: number
  name: string
  mobile: string
  createTime: string
  updateTime: string
  status: number // 0: 锁定, 1: 正常
}

// 定义分页响应接口
interface PageModel {
  records: User[]
  total: number
  size: number
  current: number
  pages: number
}

// 响应式数据
const adminUserQuery = ref<AdminUserQuery>({})
const currentPage = ref(1)
const pageSize = 10
const pageModel = ref<PageModel>({
  records: [],
  total: 0,
  size: pageSize,
  current: 1,
  pages: 0
})
const loading = ref(false)

// 计算属性
const totalPages = computed(() => {
  return pageModel.value.pages
})

// 方法
const fetchUserList = async () => {
  loading.value = true
  try {
    const response = await axios.post(`/admin/core/user/list/${currentPage.value}/${pageSize}`, adminUserQuery.value)
    if (response.data.code === 200) {
      pageModel.value = response.data.data
    } else {
      alert('获取用户列表失败: ' + response.data.message)
    }
  } catch (error) {
    console.error('获取用户列表异常:', error)
    alert('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

const searchUsers = () => {
  currentPage.value = 1
  fetchUserList()
}

const changePage = async (page: number) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
    await fetchUserList()
  }
}

const formatDate = (dateString: string) => {
  if (!dateString) return '-'
  return new Date(dateString).toLocaleString('zh-CN')
}

const getStatusText = (status: number) => {
  return status === 1 ? '正常' : '锁定'
}

const getStatusClass = (status: number) => {
  return status === 1 ? 'status-active' : 'status-blocked'
}

const viewUser = (user: User) => {
  console.log('查看用户:', user)
  // 这里可以打开用户详情弹窗或跳转到用户详情页面
}

const toggleBlockUser = async (user: User) => {
  try {
    const targetStatus = user.status === 1 ? 0 : 1
    const response = await axios.post(`/admin/core/user/lock/${user.id}/${targetStatus}`)
    if (response.data.code === 200) {
      alert(response.data.message)
      // 重新获取用户列表
      await fetchUserList()
    } else {
      alert('操作失败: ' + response.data.message)
    }
  } catch (error) {
    console.error('锁定/解锁用户异常:', error)
    alert('操作失败')
  }
}

// 初始化时加载数据
onMounted(() => {
  fetchUserList()
})
</script>

<style scoped>
.users-view {
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

.user-name {
  font-weight: 500;
}

.status-badge,
.level-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-active {
  background-color: #dcfce7;
  color: #16a34a;
}

.status-inactive {
  background-color: #f3f4f6;
  color: #64748b;
}

.status-blocked {
  background-color: #fee2e2;
  color: #dc2626;
}

.level-vip {
  background-color: #fef3c7;
  color: #d97706;
}

.level-normal {
  background-color: #dbeafe;
  color: #2563eb;
}

.action-buttons {
  display: flex;
  gap: 8px;
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

.action-btn.edit {
  color: #10b981;
  border-color: #d1fae5;
}

.action-btn.edit:hover {
  background-color: #d1fae5;
}

.action-btn.block {
  color: #ef4444;
  border-color: #fee2e2;
}

.action-btn.block:hover {
  background-color: #fee2e2;
}

.action-btn.block.unblock {
  color: #8b5cf6;
  border-color: #ddd6fe;
}

.action-btn.block.unblock:hover {
  background-color: #ddd6fe;
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