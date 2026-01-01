<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 投资产品分类
const categories = ref([
  { id: 'all', name: '全部产品' },
  { id: 'fixed', name: '定期理财' },
  { id: 'flexible', name: '活期产品' },
  { id: 'highRisk', name: '高收益' }
])

// 筛选条件
const filters = ref({
  category: 'all',
  minRate: 0,
  maxRate: 20,
  period: 'all', // all, 1m, 3m, 6m, 12m
  minAmount: 0
})

// 模拟产品数据
const allProducts = ref([
  {
    id: 1,
    name: '稳健理财1号',
    category: 'fixed',
    rate: 5.2,
    period: 3,
    periodUnit: '月',
    minInvest: 1000,
    totalAmount: 5000000,
    investedAmount: 3200000,
    status: 'hot',
    description: '适合稳健型投资者的定期理财产品，收益稳定，风险较低。',
    riskLevel: 'R2',
    tags: ['新手推荐', '热门']
  },
  {
    id: 2,
    name: '尊享收益3号',
    category: 'fixed',
    rate: 7.5,
    period: 6,
    periodUnit: '月',
    minInvest: 5000,
    totalAmount: 10000000,
    investedAmount: 4500000,
    status: 'new',
    description: '高收益定期理财，适合风险承受能力较强的投资者。',
    riskLevel: 'R3',
    tags: ['新品', '高收益']
  },
  {
    id: 3,
    name: '灵活宝',
    category: 'flexible',
    rate: 4.8,
    period: 0,
    periodUnit: '活期',
    minInvest: 100,
    totalAmount: 20000000,
    investedAmount: 15800000,
    status: 'popular',
    description: '随存随取，灵活便捷，收益稳定的活期理财产品。',
    riskLevel: 'R1',
    tags: ['灵活存取', '低风险']
  },
  {
    id: 4,
    name: '稳健理财2号',
    category: 'fixed',
    rate: 6.0,
    period: 12,
    periodUnit: '月',
    minInvest: 2000,
    totalAmount: 8000000,
    investedAmount: 2300000,
    status: 'normal',
    description: '一年期定期理财，收益较高，适合长期闲置资金。',
    riskLevel: 'R2',
    tags: ['长期投资']
  },
  {
    id: 5,
    name: '高收益精选',
    category: 'highRisk',
    rate: 9.8,
    period: 12,
    periodUnit: '月',
    minInvest: 10000,
    totalAmount: 6000000,
    investedAmount: 1800000,
    status: 'normal',
    description: '高风险高收益产品，适合风险承受能力强的投资者。',
    riskLevel: 'R4',
    tags: ['高风险高收益', '专业投资']
  },
  {
    id: 6,
    name: '短期宝',
    category: 'fixed',
    rate: 4.5,
    period: 1,
    periodUnit: '月',
    minInvest: 1000,
    totalAmount: 3000000,
    investedAmount: 2100000,
    status: 'normal',
    description: '一个月短期理财，适合短期闲置资金管理。',
    riskLevel: 'R1',
    tags: ['短期投资', '低风险']
  }
])

// 计算过滤后的产品
const filteredProducts = computed(() => {
  return allProducts.value.filter(product => {
    // 分类过滤
    if (filters.value.category !== 'all' && product.category !== filters.value.category) {
      return false
    }
    
    // 利率范围过滤
    if (product.rate < filters.value.minRate || product.rate > filters.value.maxRate) {
      return false
    }
    
    // 期限过滤
    if (filters.value.period !== 'all') {
      const periodMap: Record<string, number> = {
        '1m': 1,
        '3m': 3,
        '6m': 6,
        '12m': 12
      }
      if (product.period !== periodMap[filters.value.period]) {
        return false
      }
    }
    
    // 起投金额过滤
    if (product.minInvest < filters.value.minAmount) {
      return false
    }
    
    return true
  })
})

// 计算投资进度百分比
const getInvestProgress = (product: any) => {
  return Math.round((product.investedAmount / product.totalAmount) * 100)
}

// 格式化金额显示
const formatAmount = (amount: number) => {
  if (amount >= 10000) {
    return (amount / 10000).toFixed(2) + '万'
  }
  return amount.toLocaleString()
}

// 处理投资按钮点击
const handleInvest = (productId: number) => {
  // 模拟检查登录状态，实际应该检查真实的用户登录状态
  const isLoggedIn = false
  
  if (isLoggedIn) {
    // 跳转到投资详情页
    console.log('跳转到投资详情页，产品ID:', productId)
  } else {
    // 跳转到登录页
    router.push('/login')
  }
}

// 切换分类
const changeCategory = (categoryId: string) => {
  filters.value.category = categoryId
}

// 初始化页面
onMounted(() => {
  // 这里可以从API获取真实数据
  console.log('加载投资产品数据')
})
</script>

<template>
  <div class="invest-page">
    <div class="container">
      <div class="page-header">
        <h1>我要投资</h1>
        <p>选择适合您的理财产品，实现资产增值</p>
      </div>
      
      <!-- 产品筛选 -->
      <div class="filter-section card">
        <!-- 产品分类 -->
        <div class="filter-categories">
          <button
            v-for="category in categories"
            :key="category.id"
            :class="['category-btn', { active: filters.category === category.id }]"
            @click="changeCategory(category.id)"
          >
            {{ category.name }}
          </button>
        </div>
        
        <!-- 高级筛选 -->
        <div class="advanced-filters">
          <div class="filter-group">
            <label>收益率范围：</label>
            <div class="rate-slider">
              <input 
                type="range" 
                v-model.number="filters.minRate" 
                min="0" 
                max="20" 
                step="0.1"
              />
              <span>{{ filters.minRate }}%</span>
              <span>-</span>
              <input 
                type="range" 
                v-model.number="filters.maxRate" 
                min="0" 
                max="20" 
                step="0.1"
              />
              <span>{{ filters.maxRate }}%</span>
            </div>
          </div>
          
          <div class="filter-group">
            <label>投资期限：</label>
            <select v-model="filters.period">
              <option value="all">全部期限</option>
              <option value="1m">1个月</option>
              <option value="3m">3个月</option>
              <option value="6m">6个月</option>
              <option value="12m">12个月</option>
            </select>
          </div>
          
          <div class="filter-group">
            <label>起投金额：</label>
            <select v-model.number="filters.minAmount">
              <option :value="0">不限</option>
              <option :value="100">100元</option>
              <option :value="1000">1000元</option>
              <option :value="5000">5000元</option>
              <option :value="10000">10000元</option>
            </select>
          </div>
        </div>
      </div>
      
      <!-- 产品列表 -->
      <div class="products-section">
        <div class="section-header">
          <h2>产品列表</h2>
          <span class="product-count">共 {{ filteredProducts.length }} 个产品</span>
        </div>
        
        <div class="products-list">
          <div v-for="product in filteredProducts" :key="product.id" class="product-item card">
            <!-- 产品头部信息 -->
            <div class="product-header">
              <div class="product-title">
                <h3>{{ product.name }}</h3>
                <div class="product-tags">
                  <span v-for="(tag, index) in product.tags" :key="index" class="tag">{{ tag }}</span>
                </div>
              </div>
              <div class="product-risk">
                风险等级: <span :class="['risk-level', `risk-${product.riskLevel}`]">{{ product.riskLevel }}</span>
              </div>
            </div>
            
            <!-- 产品核心信息 -->
            <div class="product-core">
              <div class="product-rate">
                <span class="rate-number">{{ product.rate }}%</span>
                <span class="rate-label">预期年化收益率</span>
              </div>
              
              <div class="product-info">
                <div class="info-item">
                  <span class="info-label">投资期限</span>
                  <span class="info-value">{{ product.periodUnit === '活期' ? '活期' : `${product.period}${product.periodUnit}` }}</span>
                </div>
                <div class="info-item">
                  <span class="info-label">起投金额</span>
                  <span class="info-value">{{ formatAmount(product.minInvest) }}元</span>
                </div>
                <div class="info-item">
                  <span class="info-label">募集规模</span>
                  <span class="info-value">{{ formatAmount(product.totalAmount) }}元</span>
                </div>
              </div>
              
              <div class="product-actions">
                <button class="btn btn-primary invest-btn" @click="handleInvest(product.id)">
                  立即投资
                </button>
              </div>
            </div>
            
            <!-- 投资进度 -->
            <div class="product-progress">
              <div class="progress-info">
                <span>已投 {{ getInvestProgress(product) }}%</span>
                <span>剩余 {{ formatAmount(product.totalAmount - product.investedAmount) }}元</span>
              </div>
              <div class="progress-bar">
                <div 
                  class="progress-fill" 
                  :style="{ width: getInvestProgress(product) + '%' }"
                ></div>
              </div>
            </div>
            
            <!-- 产品描述 -->
            <div class="product-description">
              {{ product.description }}
            </div>
          </div>
        </div>
        
        <!-- 无产品时显示 -->
        <div v-if="filteredProducts.length === 0" class="no-products">
          <p>暂无符合条件的产品</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.invest-page {
  padding: 40px 0;
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
}

.page-header h1 {
  font-size: 32px;
  color: #333;
  margin-bottom: 16px;
}

.page-header p {
  font-size: 16px;
  color: #666;
}

/* 筛选区域 */
.filter-section {
  padding: 24px;
  margin-bottom: 40px;
}

.filter-categories {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.category-btn {
  padding: 8px 24px;
  border: 1px solid #d9d9d9;
  background-color: white;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
}

.category-btn:hover {
  border-color: #1890ff;
  color: #1890ff;
}

.category-btn.active {
  background-color: #1890ff;
  color: white;
  border-color: #1890ff;
}

.advanced-filters {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 12px;
}

.filter-group label {
  font-size: 14px;
  color: #333;
  white-space: nowrap;
}

.filter-group select {
  padding: 6px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  background-color: white;
  cursor: pointer;
}

.rate-slider {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.rate-slider input[type="range"] {
  flex: 1;
}

.rate-slider span {
  font-size: 14px;
  color: #666;
  min-width: 40px;
}

/* 产品列表 */
.products-section {
  margin-bottom: 40px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.section-header h2 {
  font-size: 24px;
  color: #333;
}

.product-count {
  color: #666;
  font-size: 14px;
}

.products-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.product-item {
  transition: transform 0.3s, box-shadow 0.3s;
}

.product-item:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.product-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.product-title h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 8px;
}

.product-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.tag {
  padding: 2px 8px;
  font-size: 12px;
  background-color: #e6f7ff;
  color: #1890ff;
  border-radius: 4px;
}

.product-risk {
  font-size: 14px;
  color: #666;
}

.risk-level {
  font-weight: 500;
}

.risk-R1 {
  color: #52c41a;
}

.risk-R2 {
  color: #1890ff;
}

.risk-R3 {
  color: #fa8c16;
}

.risk-R4, .risk-R5 {
  color: #ff4d4f;
}

.product-core {
  display: grid;
  grid-template-columns: 200px 1fr auto;
  gap: 30px;
  margin-bottom: 20px;
  align-items: center;
}

.product-rate {
  text-align: center;
}

.rate-number {
  font-size: 40px;
  font-weight: bold;
  color: #ff4d4f;
  line-height: 1;
}

.rate-label {
  font-size: 14px;
  color: #666;
  display: block;
  margin-top: 8px;
}

.product-info {
  display: flex;
  gap: 40px;
  flex-wrap: wrap;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.info-label {
  font-size: 14px;
  color: #666;
}

.info-value {
  font-size: 16px;
  color: #333;
  font-weight: 500;
}

.invest-btn {
  padding: 10px 32px;
  font-size: 16px;
}

.product-progress {
  margin-bottom: 16px;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 14px;
  color: #666;
}

.progress-bar {
  height: 8px;
  background-color: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background-color: #1890ff;
  transition: width 0.3s ease;
}

.product-description {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.no-products {
  text-align: center;
  padding: 60px 0;
  color: #999;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .product-core {
    grid-template-columns: 150px 1fr auto;
    gap: 20px;
  }
  
  .rate-number {
    font-size: 32px;
  }
}

@media (max-width: 992px) {
  .product-core {
    grid-template-columns: 1fr;
    text-align: center;
    gap: 20px;
  }
  
  .product-info {
    justify-content: center;
  }
  
  .invest-btn {
    width: 100%;
    max-width: 200px;
    margin: 0 auto;
  }
}

@media (max-width: 768px) {
  .filter-group {
    flex-direction: column;
    align-items: stretch;
    text-align: center;
  }
  
  .rate-slider {
    margin-top: 8px;
  }
  
  .product-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .product-info {
    flex-direction: column;
    gap: 16px;
  }
  
  .info-item {
    text-align: left;
  }
}
</style>