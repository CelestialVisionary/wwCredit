<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import apiClient from '../utils/api'

const router = useRouter()
const userStore = useUserStore()

// 表单数据
const formData = reactive({
  title: '',
  amount: '',
  period: 12, // 借款期限（月）
  borrowYearRate: '', // 年化利率
  returnMethod: 1, // 还款方式 1-等额本息，2-等额本金，3-每月还息一次还本，4-一次还本
  moneyUse: '', // 资金用途
  description: '' // 借款描述
})

// 错误信息
const errorMessage = ref('')
const fieldErrors = reactive({
  title: '',
  amount: '',
  period: '',
  borrowYearRate: '',
  moneyUse: ''
})

// 加载状态
const isLoading = ref(false)

// 借款额度信息
const borrowAmount = ref<string>('0')

// 还款方式选项
const returnMethodOptions = [
  { value: 1, label: '等额本息' },
  { value: 2, label: '等额本金' },
  { value: 3, label: '每月还息一次还本' },
  { value: 4, label: '一次还本' }
]

// 初始化页面
const initPage = async () => {
  // 检查登录状态
  if (!userStore.isLoggedIn) {
    router.push('/login')
    return
  }
  
  // 获取借款额度
  await fetchBorrowAmount()
}

// 获取借款额度
const fetchBorrowAmount = async () => {
  try {
    const response = await apiClient.get('/borrowInfo/getBorrowAmount')
    borrowAmount.value = response.data.data.toString()
  } catch (error: any) {
    console.error('获取借款额度失败:', error)
    errorMessage.value = error.response?.data?.msg || '获取借款额度失败'
  }
}

// 表单验证
const validateForm = () => {
  let isValid = true
  
  // 重置错误信息
  Object.keys(fieldErrors).forEach(key => {
    fieldErrors[key as keyof typeof fieldErrors] = ''
  })
  
  // 标题验证
  if (!formData.title.trim()) {
    fieldErrors.title = '请输入借款标题'
    isValid = false
  }
  
  // 金额验证
  if (!formData.amount) {
    fieldErrors.amount = '请输入借款金额'
    isValid = false
  } else if (isNaN(Number(formData.amount)) || Number(formData.amount) <= 0) {
    fieldErrors.amount = '请输入有效的借款金额'
    isValid = false
  } else if (Number(formData.amount) > Number(borrowAmount.value)) {
    fieldErrors.amount = `借款金额不能超过可借额度${borrowAmount.value}元`
    isValid = false
  }
  
  // 期限验证
  if (formData.period <= 0) {
    fieldErrors.period = '请选择有效的借款期限'
    isValid = false
  }
  
  // 利率验证
  if (!formData.borrowYearRate) {
    fieldErrors.borrowYearRate = '请输入年化利率'
    isValid = false
  } else if (isNaN(Number(formData.borrowYearRate)) || Number(formData.borrowYearRate) <= 0) {
    fieldErrors.borrowYearRate = '请输入有效的年化利率'
    isValid = false
  }
  
  // 资金用途验证
  if (!formData.moneyUse.trim()) {
    fieldErrors.moneyUse = '请输入资金用途'
    isValid = false
  }
  
  return isValid
}

// 提交借款申请
const submitBorrowApply = async () => {
  // 重置错误信息
  errorMessage.value = ''
  
  // 表单验证
  if (!validateForm()) {
    return
  }
  
  try {
    // 设置加载状态
    isLoading.value = true
    
    // 发送借款申请
    const response = await apiClient.post('/borrowInfo/save', {
      title: formData.title,
      amount: formData.amount,
      period: formData.period,
      borrowYearRate: formData.borrowYearRate,
      returnMethod: formData.returnMethod,
      moneyUse: formData.moneyUse,
      description: formData.description
    })
    
    console.log('借款申请提交成功:', response)
    
    // 显示成功提示
    alert('借款申请提交成功，请等待审核')
    
    // 跳转到借款列表页面
    router.push('/borrow-list')
  } catch (error: any) {
    // 处理提交失败
    console.error('借款申请提交失败:', error)
    errorMessage.value = error.response?.data?.msg || '借款申请提交失败，请稍后再试'
  } finally {
    // 重置加载状态
    isLoading.value = false
  }
}

// 初始化页面
initPage()
</script>

<template>
  <div class="borrow-apply-page">
    <div class="container">
      <div class="page-header">
        <h1>借款申请</h1>
        <p>请填写以下信息提交借款申请</p>
      </div>
      
      <!-- 借款额度信息 -->
      <div class="borrow-limit-info card">
        <div class="limit-label">可用借款额度</div>
        <div class="limit-amount">{{ borrowAmount }} 元</div>
        <div class="limit-tip">
          <span class="icon">💡</span>
          <span>借款金额不能超过可用额度</span>
        </div>
      </div>
      
      <!-- 错误提示 -->
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
      
      <!-- 借款申请表单 -->
      <form class="borrow-form card" @submit.prevent="submitBorrowApply">
        <div class="form-section">
          <h2>基本信息</h2>
          
          <!-- 借款标题 -->
          <div class="form-group">
            <label for="title">借款标题</label>
            <input 
              type="text" 
              id="title" 
              v-model="formData.title"
              placeholder="请输入借款标题"
              :disabled="isLoading"
            />
            <div v-if="fieldErrors.title" class="field-error">{{ fieldErrors.title }}</div>
          </div>
          
          <!-- 借款金额 -->
          <div class="form-group">
            <label for="amount">借款金额 (元)</label>
            <div class="input-wrapper">
              <span class="input-prefix">¥</span>
              <input 
                type="number" 
                id="amount" 
                v-model="formData.amount"
                placeholder="请输入借款金额"
                :disabled="isLoading"
                min="1"
                step="100"
              />
            </div>
            <div v-if="fieldErrors.amount" class="field-error">{{ fieldErrors.amount }}</div>
          </div>
          
          <!-- 借款期限 -->
          <div class="form-row">
            <div class="form-group w-50 pr-2">
              <label for="period">借款期限 (月)</label>
              <input 
                type="number" 
                id="period" 
                v-model="formData.period"
                placeholder="请输入借款期限"
                :disabled="isLoading"
                min="1"
                max="36"
              />
              <div v-if="fieldErrors.period" class="field-error">{{ fieldErrors.period }}</div>
            </div>
            
            <!-- 年化利率 -->
            <div class="form-group w-50 pl-2">
              <label for="borrowYearRate">年化利率 (%)</label>
              <div class="input-wrapper">
                <input 
                  type="number" 
                  id="borrowYearRate" 
                  v-model="formData.borrowYearRate"
                  placeholder="请输入年化利率"
                  :disabled="isLoading"
                  min="0.1"
                  max="36"
                  step="0.1"
                />
                <span class="input-suffix">%</span>
              </div>
              <div v-if="fieldErrors.borrowYearRate" class="field-error">{{ fieldErrors.borrowYearRate }}</div>
            </div>
          </div>
          
          <!-- 还款方式 -->
          <div class="form-group">
            <label for="returnMethod">还款方式</label>
            <select 
              id="returnMethod" 
              v-model="formData.returnMethod"
              :disabled="isLoading"
            >
              <option v-for="option in returnMethodOptions" :key="option.value" :value="option.value">
                {{ option.label }}
              </option>
            </select>
          </div>
        </div>
        
        <div class="form-section">
          <h2>借款详情</h2>
          
          <!-- 资金用途 -->
          <div class="form-group">
            <label for="moneyUse">资金用途</label>
            <textarea 
              id="moneyUse" 
              v-model="formData.moneyUse"
              placeholder="请详细描述借款用途"
              :disabled="isLoading"
              rows="3"
            ></textarea>
            <div v-if="fieldErrors.moneyUse" class="field-error">{{ fieldErrors.moneyUse }}</div>
          </div>
          
          <!-- 借款描述 -->
          <div class="form-group">
            <label for="description">借款描述 (可选)</label>
            <textarea 
              id="description" 
              v-model="formData.description"
              placeholder="请描述借款项目的详细情况"
              :disabled="isLoading"
              rows="5"
            ></textarea>
          </div>
        </div>
        
        <!-- 提交按钮 -->
        <div class="form-actions">
          <button type="button" class="btn btn-secondary" @click="router.back()" :disabled="isLoading">
            返回
          </button>
          <button type="submit" class="btn btn-primary" :disabled="isLoading">
            <span v-if="isLoading" class="loading-spinner"></span>
            <span v-else>提交申请</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.borrow-apply-page {
  padding: 20px 0;
  min-height: calc(100vh - 64px);
  background-color: #f5f5f5;
}

.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
}

.page-header {
  text-align: center;
  margin-bottom: 32px;
}

.page-header h1 {
  font-size: 28px;
  color: #333;
  margin-bottom: 8px;
}

.page-header p {
  color: #666;
  font-size: 16px;
}

/* 借款额度信息 */
.borrow-limit-info {
  margin-bottom: 24px;
  padding: 20px;
  text-align: center;
  background-color: #e6f7ff;
  border: 1px solid #91d5ff;
}

.limit-label {
  font-size: 16px;
  color: #1890ff;
  margin-bottom: 8px;
}

.limit-amount {
  font-size: 36px;
  font-weight: bold;
  color: #1890ff;
  margin-bottom: 16px;
}

.limit-tip {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  color: #595959;
}

.limit-tip .icon {
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

/* 表单样式 */
.borrow-form {
  padding: 24px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.form-section {
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid #f0f0f0;
}

.form-section:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.form-section h2 {
  font-size: 18px;
  color: #333;
  margin-bottom: 16px;
}

.form-group {
  margin-bottom: 20px;
}

.form-row {
  display: flex;
  margin-bottom: 20px;
}

.w-50 {
  width: 50%;
}

.pr-2 {
  padding-right: 8px;
}

.pl-2 {
  padding-left: 8px;
}

.form-group label {
  display: block;
  font-size: 14px;
  color: #333;
  margin-bottom: 8px;
  font-weight: 500;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 12px 16px;
  font-size: 16px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  transition: all 0.3s;
  background-color: #fff;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

.form-group input:disabled,
.form-group select:disabled,
.form-group textarea:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-prefix {
  position: absolute;
  left: 16px;
  color: #999;
  z-index: 1;
}

.input-wrapper input {
  padding-left: 32px;
}

.input-suffix {
  position: absolute;
  right: 16px;
  color: #999;
  z-index: 1;
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
}

.field-error {
  color: #ff4d4f;
  font-size: 12px;
  margin-top: 4px;
}

/* 表单操作按钮 */
.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #f0f0f0;
}

/* 加载动画 */
.loading-spinner {
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top: 2px solid white;
  width: 16px;
  height: 16px;
  animation: spin 1s linear infinite;
  margin-right: 8px;
  display: inline-block;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .container {
    padding: 0 16px;
  }
  
  .form-row {
    flex-direction: column;
  }
  
  .w-50 {
    width: 100%;
  }
  
  .pr-2,
  .pl-2 {
    padding-right: 0;
    padding-left: 0;
  }
  
  .form-group {
    margin-bottom: 16px;
  }
  
  .form-actions {
    flex-direction: column;
  }
}
</style>