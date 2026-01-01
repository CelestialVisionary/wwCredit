<template>
  <div class="announcements-view">
    <div class="page-header">
      <h2 class="page-title">公告管理</h2>
      <div class="page-actions">
        <button class="btn btn-primary" @click="addAnnouncement">添加公告</button>
      </div>
    </div>
    
    <!-- 搜索和筛选 -->
    <div class="search-filters">
      <div class="search-input">
        <input 
          type="text" 
          v-model="searchQuery" 
          placeholder="搜索公告标题或内容..."
          @keyup.enter="searchAnnouncements"
        />
        <button class="search-btn" @click="searchAnnouncements">搜索</button>
      </div>
      
      <div class="filter-options">
        <select v-model="categoryFilter">
          <option value="">全部分类</option>
          <option value="product">产品公告</option>
          <option value="system">系统公告</option>
          <option value="legal">法律公告</option>
        </select>
      </div>
    </div>
    
    <!-- 公告列表表格 -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>标题</th>
            <th>分类</th>
            <th>发布时间</th>
            <th>是否置顶</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="announcement in filteredAnnouncements" :key="announcement.id">
            <td>{{ announcement.id }}</td>
            <td class="title">{{ announcement.title }}</td>
            <td>
              <span class="category-badge" :class="getCategoryClass(announcement.category)">
                {{ getCategoryText(announcement.category) }}
              </span>
            </td>
            <td>{{ formatDate(announcement.publishDate) }}</td>
            <td>
              <input 
                type="checkbox" 
                :checked="announcement.isTop" 
                @change="toggleTop(announcement)"
              />
            </td>
            <td>
              <div class="action-buttons">
                <button class="action-btn view" @click="viewAnnouncement(announcement)">查看</button>
                <button class="action-btn edit" @click="editAnnouncement(announcement)">编辑</button>
                <button class="action-btn delete" @click="deleteAnnouncement(announcement)">删除</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      
      <!-- 空状态 -->
      <div v-if="filteredAnnouncements.length === 0" class="empty-state">
        <p>暂无公告数据</p>
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

// 定义公告接口
interface Announcement {
  id: number;
  title: string;
  content: string;
  publishDate: string;
  category: 'product' | 'system' | 'legal';
  isTop: boolean;
}

// 模拟公告数据
const mockAnnouncements: Announcement[] = [
  {
    id: 1,
    title: '系统升级通知',
    content: '尊敬的用户，我司将于2024年1月20日凌晨2:00-4:00进行系统升级维护，期间可能会出现短暂的服务不可用情况，请您提前做好安排。感谢您的理解与支持！',
    publishDate: '2024-01-15',
    category: 'system',
    isTop: true
  },
  {
    id: 2,
    title: '春节期间业务安排',
    content: '各位用户好，春节期间（2月10日至2月17日）我司客服服务时间调整为9:00-17:00，贷款审批可能会有延迟，请您提前做好资金规划。',
    publishDate: '2024-02-01',
    category: 'system',
    isTop: false
  },
  {
    id: 3,
    title: '威武信贷平台新产品上线通知',
    content: '尊敬的用户，为了给您提供更优质的金融服务，我司全新推出"微贷通"产品，最低额度1000元，最高额度5万元，期限灵活，审批快速，欢迎体验！',
    publishDate: '2024-01-25',
    category: 'product',
    isTop: true
  },
  {
    id: 4,
    title: '用户隐私保护政策更新',
    content: '为了更好地保护您的个人信息安全，我司对用户隐私保护政策进行了更新，详细内容请查看官网公告。更新后的政策将于2024年2月1日起生效。',
    publishDate: '2024-01-20',
    category: 'legal',
    isTop: false
  },
  {
    id: 5,
    title: '威武信贷平台关于规范经营的公告',
    content: '尊敬的用户，我司始终坚持合规经营，严格遵守国家相关法律法规。近期发现有不法分子冒充我司名义进行诈骗活动，请广大用户提高警惕，通过官方渠道办理业务。',
    publishDate: '2024-01-30',
    category: 'legal',
    isTop: false
  }
]

// 响应式数据
const announcements = ref<Announcement[]>(mockAnnouncements)
const searchQuery = ref('')
const categoryFilter = ref('')
const currentPage = ref(1)
const pageSize = 10

// 计算属性
const filteredAnnouncements = computed(() => {
  return announcements.value.filter(announcement => {
    const matchesSearch = !searchQuery.value || 
      announcement.title.includes(searchQuery.value) ||
      announcement.content.includes(searchQuery.value)
    
    const matchesCategory = !categoryFilter.value || announcement.category === categoryFilter.value
    
    return matchesSearch && matchesCategory
  }).sort((a, b) => {
    if (a.isTop !== b.isTop) {
      return a.isTop ? -1 : 1
    }
    return new Date(b.publishDate).getTime() - new Date(a.publishDate).getTime()
  })
})

const totalPages = computed(() => {
  return Math.ceil(filteredAnnouncements.value.length / pageSize)
})

// 方法
const searchAnnouncements = () => {
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

const getCategoryText = (category: string) => {
  const categoryMap: Record<string, string> = {
    product: '产品公告',
    system: '系统公告',
    legal: '法律公告'
  }
  return categoryMap[category] || category
}

const getCategoryClass = (category: string) => {
  const classMap: Record<string, string> = {
    product: 'category-product',
    system: 'category-system',
    legal: 'category-legal'
  }
  return classMap[category] || ''
}

const toggleTop = (announcement: Announcement) => {
  console.log('切换置顶状态:', announcement)
  // 这里可以实现切换置顶状态的逻辑
}

const viewAnnouncement = (announcement: Announcement) => {
  console.log('查看公告:', announcement)
  // 这里可以打开公告详情弹窗
}

const editAnnouncement = (announcement: Announcement) => {
  console.log('编辑公告:', announcement)
  // 这里可以打开编辑公告弹窗
}

const deleteAnnouncement = (announcement: Announcement) => {
  if (confirm(`确定要删除公告"${announcement.title}"吗？`)) {
    console.log('删除公告:', announcement)
    // 这里可以实现删除公告的逻辑
  }
}

const addAnnouncement = () => {
  console.log('添加新公告')
  // 这里可以打开添加公告弹窗
}
</script>

<style scoped>
.announcements-view {
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

.title {
  font-weight: 500;
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.category-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.category-product {
  background-color: #dbeafe;
  color: #1d4ed8;
}

.category-system {
  background-color: #d1fae5;
  color: #059669;
}

.category-legal {
  background-color: #fef3c7;
  color: #d97706;
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

.action-btn.delete {
  color: #ef4444;
  border-color: #fee2e2;
}

.action-btn.delete:hover {
  background-color: #fee2e2;
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