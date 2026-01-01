<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'

// 定义公告接口
interface Announcement {
  id: number;
  title: string;
  content: string;
  publishDate: string;
  category: string;
  isTop: boolean;
}

// 公告列表数据
const announcements = ref<Announcement[]>([
  {
    id: 1,
    title: '系统维护通知',
    content: '尊敬的用户，我们将于今晚22:00至次日凌晨02:00进行系统维护，请提前做好准备。维护期间，部分功能可能暂时无法使用，给您带来的不便，敬请谅解。',
    publishDate: '2023-07-15',
    category: '系统公告',
    isTop: true
  },
  {
    id: 2,
    title: '平台安全升级公告',
    content: '为了保障用户资金安全，提升平台服务体验，我们将于近期对平台安全系统进行全面升级。升级后，平台将采用更先进的加密技术和风控系统，为用户提供更加安全、可靠的金融服务环境。',
    publishDate: '2023-07-10',
    category: '安全公告',
    isTop: true
  },
  {    id: 3,    title: '威武信贷平台新产品上线通知',    content: '尊敬的用户，我们很高兴地宣布，威武信贷平台将于7月20日正式上线全新的个人消费信贷产品。该产品具有额度高、利率低、申请便捷等特点，满足您多样化的资金需求。',    publishDate: '2023-07-05',    category: '产品公告',    isTop: false  },
  {
    id: 4,
    title: '关于调整部分产品利率的公告',
    content: '根据市场情况及政策要求，我们将于2023年8月1日起对部分信贷产品的利率进行调整。具体调整内容请查看平台公告详情。我们承诺将继续为您提供优质的金融服务。',
    publishDate: '2023-06-30',
    category: '产品公告',
    isTop: false
  },
  {    id: 5,    title: '关于《威武信贷平台用户服务协议》更新的公告',    content: '尊敬的用户，为了更好地保障您的权益，我们对《威武信贷平台用户服务协议》进行了更新，主要涉及用户信息保护、服务内容变更等条款。请您仔细阅读并遵守相关规定。',    publishDate: '2023-06-25',    category: '法律公告',    isTop: false  },
  {
    id: 6,
    title: '端午节放假安排通知',
    content: '根据国家法定节假日规定，结合平台实际情况，端午节期间（6月22日至6月24日）客服工作时间调整为每天10:00-16:00。如有紧急问题，请通过平台留言功能提交，我们将尽快为您处理。',
    publishDate: '2023-06-20',
    category: '服务公告',
    isTop: false
  }
])

// 搜索关键词
const searchKeyword = ref('')

// 分类筛选
const categoryFilter = ref('全部')

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)

// 分类选项
const categories = ref(['全部', '系统公告', '安全公告', '产品公告', '法律公告', '服务公告'])

// 选中的公告（用于详情展示）
const selectedAnnouncement = ref<Announcement | null>(null)

// 计算筛选后的公告列表
const filteredAnnouncements = computed(() => {
  let result = [...announcements.value]
  
  // 按关键词筛选
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(item => 
      item.title.toLowerCase().includes(keyword) || 
      item.content.toLowerCase().includes(keyword)
    )
  }
  
  // 按分类筛选
  if (categoryFilter.value !== '全部') {
    result = result.filter(item => item.category === categoryFilter.value)
  }
  
  // 按置顶状态和发布日期排序
  result.sort((a, b) => {
    if (a.isTop !== b.isTop) {
      return a.isTop ? -1 : 1
    }
    return new Date(b.publishDate).getTime() - new Date(a.publishDate).getTime()
  })
  
  return result
})

// 计算总页数
const totalPages = computed(() => {
  return Math.ceil(filteredAnnouncements.value.length / pageSize.value)
})

// 计算当前页显示的公告
const currentPageAnnouncements = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredAnnouncements.value.slice(start, end)
})

// 切换到指定页
const changePage = (page: number) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
    // 滚动到页面顶部
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
}

// 查看公告详情
const viewAnnouncement = (announcement: Announcement) => {
  selectedAnnouncement.value = announcement
}

// 关闭公告详情
const closeAnnouncement = () => {
  selectedAnnouncement.value = null
}

// 搜索公告
const searchAnnouncements = () => {
  currentPage.value = 1 // 重置到第一页
}

// 处理搜索框回车事件
const handleSearchKeydown = (event: KeyboardEvent) => {
  if (event.key === 'Enter') {
    searchAnnouncements()
  }
}

// 清除搜索
const clearSearch = () => {
  searchKeyword.value = ''
  categoryFilter.value = '全部'
  currentPage.value = 1
}

// 生成页码列表
const getPageNumbers = () => {
  const pages = []
  const total = totalPages.value
  const current = currentPage.value
  
  // 总是显示首页
  if (current > 1) {
    pages.push(1)
    if (current > 2) {
      pages.push('...')
    }
  }
  
  // 显示当前页附近的页码
  for (let i = Math.max(1, current - 1); i <= Math.min(total, current + 1); i++) {
    if (i !== 1 || current <= 2) {
      pages.push(i)
    }
  }
  
  // 总是显示末页
  if (current < total) {
    if (current < total - 1) {
      pages.push('...')
    }
    pages.push(total)
  }
  
  return pages
}

// 组件挂载时执行
onMounted(() => {
  // 可以在这里从API获取公告数据
  console.log('获取公告列表数据')
})
</script>

<template>
  <div class="announcements-page">
    <div class="container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1>平台公告</h1>
        <p>了解平台最新动态和重要通知</p>
      </div>
      
      <!-- 搜索和筛选区域 -->
      <div class="search-filter-area card">
        <div class="search-box">
          <div class="input-wrapper">
            <span class="input-icon">🔍</span>
            <input 
              type="text" 
              v-model="searchKeyword"
              placeholder="请输入关键词搜索公告"
              @keydown="handleSearchKeydown"
            />
            <button v-if="searchKeyword" class="clear-search" @click="clearSearch">✕</button>
          </div>
          <button class="search-button" @click="searchAnnouncements">搜索</button>
        </div>
        
        <div class="filter-box">
          <span class="filter-label">分类：</span>
          <select v-model="categoryFilter" @change="currentPage = 1">
            <option v-for="category in categories" :key="category" :value="category">
              {{ category }}
            </option>
          </select>
        </div>
      </div>
      
      <!-- 公告列表 -->
      <div class="announcements-list">
        <div 
          v-for="announcement in currentPageAnnouncements" 
          :key="announcement.id" 
          class="announcement-item card"
          @click="viewAnnouncement(announcement)"
        >
          <div class="announcement-header">
            <h3 class="announcement-title">
              <span v-if="announcement.isTop" class="top-tag">置顶</span>
              {{ announcement.title }}
            </h3>
            <span class="announcement-category">{{ announcement.category }}</span>
          </div>
          <p class="announcement-content">{{ announcement.content }}</p>
          <div class="announcement-footer">
            <span class="publish-date">{{ announcement.publishDate }}</span>
            <span class="view-more">查看详情 →</span>
          </div>
        </div>
        
        <!-- 无数据提示 -->
        <div v-if="filteredAnnouncements.length === 0" class="no-data card">
          <p>暂无符合条件的公告</p>
        </div>
      </div>
      
      <!-- 分页 -->
      <div v-if="filteredAnnouncements.length > 0" class="pagination">
        <button 
          class="page-button" 
          :disabled="currentPage === 1"
          @click="changePage(currentPage - 1)"
        >
          上一页
        </button>
        
        <template v-for="page in getPageNumbers()" :key="page">
          <button 
            v-if="typeof page === 'number'" 
            class="page-button"
            :class="{ active: currentPage === page }"
            @click="changePage(page)"
          >
            {{ page }}
          </button>
          <span v-else class="page-ellipsis">...</span>
        </template>
        
        <button 
          class="page-button" 
          :disabled="currentPage === totalPages"
          @click="changePage(currentPage + 1)"
        >
          下一页
        </button>
      </div>
      
      <!-- 公告详情弹窗 -->
      <div v-if="selectedAnnouncement" class="modal-overlay" @click="closeAnnouncement">
        <div class="modal-content card" @click.stop>
          <div class="modal-header">
            <h2 class="modal-title">{{ selectedAnnouncement.title }}</h2>
            <button class="close-button" @click="closeAnnouncement">✕</button>
          </div>
          <div class="modal-body">
            <div class="announcement-meta">
              <span class="meta-item">
                <strong>分类：</strong>{{ selectedAnnouncement.category }}
              </span>
              <span class="meta-item">
                <strong>发布时间：</strong>{{ selectedAnnouncement.publishDate }}
              </span>
              <span v-if="selectedAnnouncement.isTop" class="top-badge">置顶公告</span>
            </div>
            <div class="announcement-detail-content">
              {{ selectedAnnouncement.content }}
            </div>
          </div>
          <div class="modal-footer">
            <button class="close-modal-button" @click="closeAnnouncement">关闭</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.announcements-page {
  padding: 40px 0;
}

/* 页面标题 */
.page-header {
  margin-bottom: 40px;
  text-align: center;
}

.page-header h1 {
  font-size: 32px;
  color: #333;
  margin-bottom: 12px;
}

.page-header p {
  font-size: 16px;
  color: #666;
}

/* 搜索和筛选区域 */
.search-filter-area {
  padding: 20px;
  margin-bottom: 30px;
}

.search-box {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
}

.input-wrapper {
  flex: 1;
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 15px;
  font-size: 18px;
  color: #999;
  z-index: 1;
}

.input-wrapper input {
  width: 100%;
  padding: 12px 45px 12px 45px;
  font-size: 16px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  transition: all 0.3s;
}

.input-wrapper input:focus {
  outline: none;
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

.clear-search {
  position: absolute;
  right: 15px;
  background: none;
  border: none;
  font-size: 18px;
  color: #999;
  cursor: pointer;
  padding: 4px;
  border-radius: 50%;
  transition: all 0.3s;
}

.clear-search:hover {
  background-color: #f0f0f0;
  color: #666;
}

.search-button {
  padding: 0 24px;
  background-color: #1890ff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s;
  white-space: nowrap;
}

.search-button:hover {
  background-color: #40a9ff;
}

.filter-box {
  display: flex;
  align-items: center;
  gap: 12px;
}

.filter-label {
  font-size: 14px;
  color: #333;
}

.filter-box select {
  padding: 8px 12px;
  font-size: 14px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  background-color: white;
  cursor: pointer;
  transition: all 0.3s;
}

.filter-box select:focus {
  outline: none;
  border-color: #1890ff;
}

/* 公告列表 */
.announcements-list {
  margin-bottom: 30px;
}

.announcement-item {
  padding: 24px;
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s;
}

.announcement-item:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.announcement-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.announcement-title {
  font-size: 18px;
  color: #333;
  font-weight: 500;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.top-tag {
  background-color: #ff4d4f;
  color: white;
  padding: 2px 8px;
  font-size: 12px;
  border-radius: 4px;
  font-weight: normal;
}

.announcement-category {
  font-size: 14px;
  color: #1890ff;
  background-color: #e6f7ff;
  padding: 4px 12px;
  border-radius: 4px;
  white-space: nowrap;
}

.announcement-content {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 16px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.announcement-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.publish-date {
  font-size: 14px;
  color: #999;
}

.view-more {
  font-size: 14px;
  color: #1890ff;
  transition: color 0.3s;
}

.announcement-item:hover .view-more {
  color: #40a9ff;
}

/* 无数据提示 */
.no-data {
  padding: 60px 0;
  text-align: center;
  color: #999;
  font-size: 16px;
}

/* 分页 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
}

.page-button {
  padding: 8px 16px;
  background-color: white;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  font-size: 14px;
  color: #333;
  cursor: pointer;
  transition: all 0.3s;
}

.page-button:hover:not(:disabled) {
  border-color: #1890ff;
  color: #1890ff;
}

.page-button.active {
  background-color: #1890ff;
  border-color: #1890ff;
  color: white;
}

.page-button:disabled {
  background-color: #f5f5f5;
  color: #d9d9d9;
  cursor: not-allowed;
  border-color: #d9d9d9;
}

.page-ellipsis {
  padding: 8px 16px;
  color: #999;
  font-size: 14px;
}

/* 公告详情弹窗 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  width: 100%;
  max-width: 800px;
  max-height: 90vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.modal-header {
  padding: 24px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-title {
  font-size: 20px;
  color: #333;
  margin: 0;
  flex: 1;
}

.close-button {
  background: none;
  border: none;
  font-size: 24px;
  color: #999;
  cursor: pointer;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.3s;
}

.close-button:hover {
  background-color: #f0f0f0;
  color: #666;
}

.modal-body {
  padding: 24px;
  overflow-y: auto;
  flex: 1;
}

.announcement-meta {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  align-items: center;
  gap: 24px;
}

.meta-item {
  font-size: 14px;
  color: #666;
}

.top-badge {
  background-color: #ff4d4f;
  color: white;
  padding: 4px 12px;
  font-size: 12px;
  border-radius: 4px;
}

.announcement-detail-content {
  font-size: 16px;
  color: #333;
  line-height: 1.8;
}

.modal-footer {
  padding: 16px 24px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  justify-content: flex-end;
}

.close-modal-button {
  padding: 8px 24px;
  background-color: #1890ff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.close-modal-button:hover {
  background-color: #40a9ff;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .announcements-page {
    padding: 30px 0;
  }
  
  .page-header h1 {
    font-size: 28px;
  }
  
  .search-box {
    flex-direction: column;
  }
  
  .filter-box {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .announcement-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .announcement-category {
    align-self: flex-start;
  }
  
  .pagination {
    flex-wrap: wrap;
  }
  
  .modal-overlay {
    padding: 10px;
  }
  
  .modal-content {
    max-height: 95vh;
  }
  
  .announcement-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}

@media (max-width: 480px) {
  .page-header h1 {
    font-size: 24px;
  }
  
  .announcement-item {
    padding: 16px;
  }
  
  .modal-header,
  .modal-body,
  .modal-footer {
    padding: 16px;
  }
  
  .modal-title {
    font-size: 18px;
  }
}
</style>