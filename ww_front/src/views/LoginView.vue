<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import apiClient from '../utils/api'
import { useUserStore } from '../stores/user'

const router = useRouter()
const userStore = useUserStore()

// 表单数据
const formData = reactive({
  mobile: '',
  password: '',
  userType: 1, // 1: 出借人, 2: 借款人
  remember: false
})

// 错误信息
const errorMessage = ref('')

// 加载状态
const isLoading = ref(false)

// 表单验证
const validateForm = () => {
  if (!formData.mobile.trim()) {
    errorMessage.value = '请输入手机号'
    return false
  }
  // 简单的手机号格式验证
  const mobileRegex = /^1[3-9]\d{9}$/
  if (!mobileRegex.test(formData.mobile.trim())) {
    errorMessage.value = '请输入有效的手机号'
    return false
  }
  if (!formData.password) {
    errorMessage.value = '请输入密码'
    return false
  }
  return true
}

// 登录处理函数
const handleLogin = async () => {
  // 重置错误信息
  errorMessage.value = ''
  
  // 表单验证
  if (!validateForm()) {
    return
  }
  
  try {
      // 设置加载状态
      isLoading.value = true
      
      // 发送登录请求
      const response = await apiClient.post('/user/login', {
        mobile: formData.mobile,
        password: formData.password,
        userType: formData.userType
      })
      
      console.log('登录成功:', response)
      
      // 解析响应
      const responseData = response.data
      if (responseData.code === 200) {
        // 存储登录状态
        const token = responseData.data
        localStorage.setItem('token', token)
        userStore.setToken(token)
        
        // 获取用户信息
        await userStore.fetchUserInfo()
        
        // 登录成功后跳转到首页
        router.push('/')
        
        // 显示成功提示
        alert('登录成功')
      } else {
        // 显示登录失败信息
        errorMessage.value = responseData.msg || '登录失败'
      }
    } catch (error: any) {
      // 处理登录失败
      console.error('登录失败:', error)
      errorMessage.value = error.response?.data?.msg || '登录失败，请检查手机号和密码是否正确'
    } finally {
      // 重置加载状态
      isLoading.value = false
    }
}

// 跳转到注册页面
const goToRegister = () => {
  router.push('/register')
}

// 忘记密码
const forgotPassword = () => {
  // 实际项目中应该跳转到忘记密码页面
  alert('忘记密码功能即将上线')
}
</script>

<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-header">
        <div class="logo">
          <h1>威武信贷</h1>
          <p>企业级金融服务平台</p>
        </div>
      </div>
      
      <div class="login-form card">
        <h2>用户登录</h2>
        
        <!-- 错误提示 -->
        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
        
        <!-- 登录表单 -->
        <form @submit.prevent="handleLogin">
          <!-- 手机号输入框 -->
          <div class="form-group">
            <label for="mobile">手机号</label>
            <div class="input-wrapper">
              <span class="input-icon">📱</span>
              <input 
                type="text" 
                id="mobile" 
                v-model="formData.mobile"
                placeholder="请输入手机号"
                autocomplete="mobile"
                :disabled="isLoading"
              />
            </div>
          </div>
          
          <!-- 密码输入框 -->
          <div class="form-group">
            <label for="password">密码</label>
            <div class="input-wrapper">
              <span class="input-icon">🔒</span>
              <input 
                type="password" 
                id="password" 
                v-model="formData.password"
                placeholder="请输入密码"
                autocomplete="current-password"
                :disabled="isLoading"
              />
            </div>
          </div>

          <!-- 用户类型选择 -->
          <div class="form-group">
            <label for="userType">用户类型</label>
            <div class="input-wrapper">
              <span class="input-icon">👤</span>
              <select 
                id="userType" 
                v-model="formData.userType"
                :disabled="isLoading"
                class="user-type-select"
              >
                <option value="1">出借人</option>
                <option value="2">借款人</option>
              </select>
            </div>
          </div>
          
          <!-- 记住密码和忘记密码 -->
          <div class="form-options">
            <label class="remember-checkbox">
              <input 
                type="checkbox" 
                v-model="formData.remember"
                :disabled="isLoading"
              />
              <span class="checkmark"></span>
              <span>记住密码</span>
            </label>
            <button 
              type="button" 
              class="forgot-password" 
              @click="forgotPassword"
              :disabled="isLoading"
            >
              忘记密码？
            </button>
          </div>
          
          <!-- 登录按钮 -->
          <button 
            type="submit" 
            class="login-button" 
            :disabled="isLoading"
          >
            <span v-if="isLoading" class="loading-spinner"></span>
            <span v-else>登录</span>
          </button>
          
          <!-- 注册链接 -->
          <div class="register-link">
            还没有账号？
            <button 
              type="button" 
              class="register-button" 
              @click="goToRegister"
              :disabled="isLoading"
            >
              立即注册
            </button>
          </div>
        </form>
      </div>
      
      <!-- 安全提示 -->
      <div class="security-tips">
        <h3>安全提示</h3>
        <ul>
          <li>请确认访问的网址是否为官方网址</li>
          <li>请妥善保管您的账号和密码信息</li>
          <li>请勿在公共设备上选择"记住密码"</li>
          <li>如遇异常情况，请立即联系客服</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-container {
  width: 100%;
  max-width: 1000px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
  align-items: center;
}

/* 登录头部 */
.login-header {
  text-align: center;
}

.logo h1 {
  font-size: 48px;
  color: #1890ff;
  margin-bottom: 10px;
  font-weight: bold;
}

.logo p {
  font-size: 18px;
  color: #666;
}

/* 登录表单 */
.login-form {
  padding: 40px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.login-form h2 {
  font-size: 24px;
  color: #333;
  margin-bottom: 30px;
  text-align: center;
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

/* 表单组 */
.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-size: 14px;
  color: #333;
  margin-bottom: 8px;
  font-weight: 500;
}

.input-wrapper {
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
  padding: 12px 16px 12px 45px;
  font-size: 16px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  transition: all 0.3s;
  background-color: #fff;
}

.input-wrapper input:focus {
  outline: none;
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

.input-wrapper input:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
}

/* 表单选项 */
.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

/* 记住密码复选框 */
.remember-checkbox {
  display: flex;
  align-items: center;
  cursor: pointer;
  font-size: 14px;
  color: #666;
}

.remember-checkbox input {
  position: absolute;
  opacity: 0;
  cursor: pointer;
  height: 0;
  width: 0;
}

.checkmark {
  position: relative;
  display: inline-block;
  height: 18px;
  width: 18px;
  background-color: #fff;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  margin-right: 8px;
  transition: all 0.3s;
}

.remember-checkbox:hover input ~ .checkmark {
  border-color: #1890ff;
}

.remember-checkbox input:checked ~ .checkmark {
  background-color: #1890ff;
  border-color: #1890ff;
}

.checkmark:after {
  content: "";
  position: absolute;
  display: none;
}

.remember-checkbox input:checked ~ .checkmark:after {
  display: block;
}

.remember-checkbox .checkmark:after {
  left: 6px;
  top: 3px;
  width: 5px;
  height: 10px;
  border: solid white;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}

/* 忘记密码链接 */
.forgot-password {
  background: none;
  border: none;
  color: #1890ff;
  font-size: 14px;
  cursor: pointer;
  transition: color 0.3s;
  padding: 4px 0;
}

.forgot-password:hover {
  color: #40a9ff;
  text-decoration: underline;
}

.forgot-password:disabled {
  color: #d9d9d9;
  cursor: not-allowed;
  text-decoration: none;
}

/* 登录按钮 */
.login-button {
  width: 100%;
  background-color: #1890ff;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 12px 0;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.login-button:hover {
  background-color: #40a9ff;
}

.login-button:disabled {
  background-color: #d9d9d9;
  cursor: not-allowed;
}

/* 加载动画 */
.loading-spinner {
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top: 2px solid white;
  width: 16px;
  height: 16px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 注册链接 */
.register-link {
  margin-top: 20px;
  text-align: center;
  font-size: 14px;
  color: #666;
}

.register-button {
  background: none;
  border: none;
  color: #1890ff;
  font-size: 14px;
  cursor: pointer;
  transition: color 0.3s;
  padding: 4px 0;
}

.register-button:hover {
  color: #40a9ff;
  text-decoration: underline;
}

.register-button:disabled {
  color: #d9d9d9;
  cursor: not-allowed;
  text-decoration: none;
}

/* 安全提示 */
.security-tips {
  background-color: rgba(255, 255, 255, 0.9);
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.security-tips h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 16px;
}

.security-tips ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.security-tips li {
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
  position: relative;
  padding-left: 24px;
  line-height: 1.6;
}

.security-tips li:last-child {
  margin-bottom: 0;
}

.security-tips li::before {
  content: '⚠️';
  position: absolute;
  left: 0;
  top: 0;
  font-size: 16px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-container {
    grid-template-columns: 1fr;
    max-width: 500px;
  }
  
  .login-header {
    display: none;
  }
  
  .login-form {
    padding: 30px;
  }
  
  .security-tips {
    padding: 20px;
  }
}

@media (max-width: 480px) {
  .login-page {
    padding: 10px;
  }
  
  .login-form {
    padding: 24px;
  }
  
  .logo h1 {
    font-size: 36px;
  }
  
  .login-form h2 {
    font-size: 20px;
  }
}
</style>