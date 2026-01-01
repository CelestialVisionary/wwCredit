<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import apiClient from '../utils/api'

const router = useRouter()
const userStore = useUserStore()

// 表单数据
const formData = reactive({
  mobile: '',
  password: '',
  passwordto: '',
  userType: 1, // 1: 出借人, 2: 借款人
  agreeTerms: false
})

// 错误信息
const errorMessage = ref('')
const fieldErrors = reactive({
  mobile: '',
  password: '',
  passwordto: '',
  agreeTerms: ''
})

// 加载状态
const isLoading = ref(false)

// 密码强度
const passwordStrength = ref(0) // 0: 未输入, 1: 弱, 2: 中, 3: 强
const passwordStrengthText = ref('')
const passwordStrengthColor = ref('#d9d9d9')

// 验证用户名已移除，后端无需此字段


// 验证邮箱已移除，后端无需此字段


// 检查手机号是否已注册
const checkMobile = async () => {
  try {
    const response = await apiClient.get(`/user/checkMobile/${formData.mobile}`)
    return response.data
  } catch (error) {
    console.error('手机号检查失败:', error)
    return false
  }
}

// 验证手机号
const validatePhone = async () => {
  const phone = formData.mobile.trim()
  if (!phone) {
    fieldErrors.mobile = '请输入手机号码'
    return false
  }
  if (!/^1[3-9]\d{9}$/.test(phone)) {
    fieldErrors.mobile = '请输入有效的手机号码'
    return false
  }
  // 检查手机号是否已注册
  const isAvailable = await checkMobile()
  if (!isAvailable) {
    fieldErrors.mobile = '该手机号已被注册'
    return false
  }
  fieldErrors.mobile = ''
  return true
}

// 验证密码
const validatePassword = () => {
  const password = formData.password
  if (!password) {
    fieldErrors.password = '请输入密码'
    passwordStrength.value = 0
    return false
  }
  if (password.length < 8) {
    fieldErrors.password = '密码长度不能少于8个字符'
    passwordStrength.value = 0
    return false
  }
  
  // 计算密码强度
  let strength = 0
  if (/[a-z]/.test(password)) strength++
  if (/[A-Z]/.test(password)) strength++
  if (/[0-9]/.test(password)) strength++
  if (/[^a-zA-Z0-9]/.test(password)) strength++
  
  passwordStrength.value = strength
  
  // 设置密码强度提示
  switch (strength) {
    case 1:
      passwordStrengthText.value = '弱'
      passwordStrengthColor.value = '#ff4d4f'
      break
    case 2:
      passwordStrengthText.value = '中'
      passwordStrengthColor.value = '#faad14'
      break
    case 3:
    case 4:
      passwordStrengthText.value = '强'
      passwordStrengthColor.value = '#52c41a'
      break
    default:
      passwordStrengthText.value = ''
      passwordStrengthColor.value = '#d9d9d9'
  }
  
  fieldErrors.password = ''
  return true
}

// 验证确认密码
const validateConfirmPassword = () => {
  if (!formData.passwordto) {
    fieldErrors.passwordto = '请确认密码'
    return false
  }
  if (formData.passwordto !== formData.password) {
    fieldErrors.passwordto = '两次输入的密码不一致'
    return false
  }
  fieldErrors.passwordto = ''
  return true
}

// 验证用户协议
const validateAgreeTerms = () => {
  if (!formData.agreeTerms) {
    fieldErrors.agreeTerms = '请阅读并同意用户协议和隐私政策'
    return false
  }
  fieldErrors.agreeTerms = ''
  return true
}

// 表单验证
const validateForm = async () => {
  const isPhoneValid = await validatePhone()
  const isPasswordValid = validatePassword()
  const isConfirmPasswordValid = validateConfirmPassword()
  const isAgreeTermsValid = validateAgreeTerms()
  
  return isPhoneValid && isPasswordValid && isConfirmPasswordValid && isAgreeTermsValid
}

// 注册处理函数
const handleRegister = async () => {
  // 重置错误信息
  errorMessage.value = ''
  
  // 表单验证
  if (!(await validateForm())) {
    return
  }
  
  try {
    // 设置加载状态
    isLoading.value = true
    
    // 发送注册请求
    const response = await apiClient.post('/user/register', formData)
    
    console.log('注册成功:', response)
    
    // 存储登录状态
    const token = response.data
    localStorage.setItem('token', token)
    userStore.setToken(token)
    
    // 显示注册成功提示
    alert('注册成功，请登录')
    
    // 注册成功后跳转到登录页面
    router.push('/login')
  } catch (error: any) {
    // 处理注册失败
    console.error('注册失败:', error)
    errorMessage.value = error.response?.data?.message || '注册失败，请稍后再试'
  } finally {
    // 重置加载状态
    isLoading.value = false
  }
}

// 跳转到登录页面
const goToLogin = () => {
  router.push('/login')
}

// 查看用户协议
const viewTerms = () => {
  alert('用户协议内容将在这里显示')
}

// 查看隐私政策
const viewPrivacy = () => {
  alert('隐私政策内容将在这里显示')
}
</script>

<template>
  <div class="register-page">
    <div class="register-container">
      <div class="register-header">
    <div class="logo">
      <h1>威武信贷</h1>
          <p>企业级金融服务平台</p>
        </div>
      </div>
      
      <div class="register-form card">
        <h2>用户注册</h2>
        
        <!-- 错误提示 -->
        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
        
        <!-- 注册表单 -->
        <form @submit.prevent="handleRegister">
          
          
          <!-- 手机号输入框 -->
          <div class="form-group">
            <label for="mobile">手机号码</label>
            <div class="input-wrapper">
              <span class="input-icon">📞</span>
              <input 
                type="tel" 
                id="mobile" 
                v-model="formData.mobile"
                placeholder="请输入手机号码"
                autocomplete="tel"
                :disabled="isLoading"
                @blur="validatePhone"
              />
            </div>
            <div v-if="fieldErrors.mobile" class="field-error">
              {{ fieldErrors.mobile }}
            </div>
          </div>
          
          <!-- 密码输入框 -->
          <div class="form-group">
            <label for="password">设置密码</label>
            <div class="input-wrapper">
              <span class="input-icon">🔒</span>
              <input 
                type="password" 
                id="password" 
                v-model="formData.password"
                placeholder="请设置密码（至少8个字符）"
                autocomplete="new-password"
                :disabled="isLoading"
                @blur="validatePassword"
                @input="validatePassword"
              />
            </div>
            <!-- 密码强度指示器 -->
            <div v-if="formData.password" class="password-strength">
              <div class="strength-meter">
                <div 
                  class="strength-bar" 
                  :style="{ width: `${(passwordStrength / 4) * 100}%`, backgroundColor: passwordStrengthColor }"
                ></div>
              </div>
              <div class="strength-text" :style="{ color: passwordStrengthColor }">
                密码强度: {{ passwordStrengthText }}
              </div>
            </div>
            <div v-if="fieldErrors.password" class="field-error">
              {{ fieldErrors.password }}
            </div>
          </div>
          
          <!-- 确认密码输入框 -->
          <div class="form-group">
            <label for="passwordto">确认密码</label>
            <div class="input-wrapper">
              <span class="input-icon">🔒</span>
              <input 
                type="password" 
                id="passwordto" 
                v-model="formData.passwordto"
                placeholder="请再次输入密码"
                autocomplete="new-password"
                :disabled="isLoading"
                @blur="validateConfirmPassword"
              />
            </div>
            <div v-if="fieldErrors.passwordto" class="field-error">
              {{ fieldErrors.passwordto }}
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
          
          <!-- 用户协议 -->
          <div class="form-group terms-agreement">
            <label class="terms-checkbox">
              <input 
                type="checkbox" 
                v-model="formData.agreeTerms"
                :disabled="isLoading"
                @change="validateAgreeTerms"
              />
              <span class="checkmark"></span>
              <span class="terms-text">
                我已阅读并同意
                <button type="button" class="terms-link" @click="viewTerms">用户协议</button>
                和
                <button type="button" class="terms-link" @click="viewPrivacy">隐私政策</button>
              </span>
            </label>
            <div v-if="fieldErrors.agreeTerms" class="field-error">
              {{ fieldErrors.agreeTerms }}
            </div>
          </div>
          
          <!-- 注册按钮 -->
          <button 
            type="submit" 
            class="register-button" 
            :disabled="isLoading"
          >
            <span v-if="isLoading" class="loading-spinner"></span>
            <span v-else>注册</span>
          </button>
          
          <!-- 登录链接 -->
          <div class="login-link">
            已有账号？
            <button 
              type="button" 
              class="login-button" 
              @click="goToLogin"
              :disabled="isLoading"
            >
              立即登录
            </button>
          </div>
        </form>
      </div>
      
      <!-- 注册提示 -->
      <div class="register-tips">
        <h3>注册提示</h3>
        <ul>
          <li>请使用真实有效的邮箱和手机号码进行注册</li>
          <li>密码请包含字母、数字和特殊字符，确保安全性</li>
          <li>注册即表示您同意我们的用户协议和隐私政策</li>
          <li>如有任何问题，请联系客服 400-123-4567</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<style scoped>
.register-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.register-container {
  width: 100%;
  max-width: 1000px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
  align-items: center;
}

/* 注册头部 */
.register-header {
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

/* 注册表单 */
.register-form {
  padding: 40px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.register-form h2 {
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

.input-wrapper input,
.input-wrapper select {
  width: 100%;
  padding: 12px 16px 12px 45px;
  font-size: 16px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  transition: all 0.3s;
  background-color: #fff;
  appearance: none;
  cursor: pointer;
}

.input-wrapper input:focus,
.input-wrapper select:focus {
  outline: none;
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

.input-wrapper input:disabled,
.input-wrapper select:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
}

/* 字段错误提示 */
.field-error {
  color: #ff4d4f;
  font-size: 12px;
  margin-top: 4px;
}

/* 密码强度指示器 */
.password-strength {
  margin-top: 8px;
}

.strength-meter {
  height: 4px;
  background-color: #f0f0f0;
  border-radius: 2px;
  overflow: hidden;
  margin-bottom: 4px;
}

.strength-bar {
  height: 100%;
  border-radius: 2px;
  transition: width 0.3s, background-color 0.3s;
}

.strength-text {
  font-size: 12px;
}

/* 用户协议 */
.terms-agreement {
  margin-bottom: 30px;
}

.terms-checkbox {
  display: flex;
  align-items: flex-start;
  cursor: pointer;
  font-size: 14px;
  color: #666;
}

.terms-checkbox input {
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
  margin-top: 2px;
  transition: all 0.3s;
}

.terms-checkbox:hover input ~ .checkmark {
  border-color: #1890ff;
}

.terms-checkbox input:checked ~ .checkmark {
  background-color: #1890ff;
  border-color: #1890ff;
}

.checkmark:after {
  content: "";
  position: absolute;
  display: none;
}

.terms-checkbox input:checked ~ .checkmark:after {
  display: block;
}

.terms-checkbox .checkmark:after {
  left: 6px;
  top: 3px;
  width: 5px;
  height: 10px;
  border: solid white;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}

.terms-text {
  flex: 1;
  line-height: 1.6;
}

.terms-link {
  background: none;
  border: none;
  color: #1890ff;
  font-size: 14px;
  cursor: pointer;
  transition: color 0.3s;
  padding: 0;
  margin: 0 4px;
}

.terms-link:hover {
  color: #40a9ff;
  text-decoration: underline;
}

/* 注册按钮 */
.register-button {
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

.register-button:hover {
  background-color: #40a9ff;
}

.register-button:disabled {
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

/* 登录链接 */
.login-link {
  margin-top: 20px;
  text-align: center;
  font-size: 14px;
  color: #666;
}

.login-button {
  background: none;
  border: none;
  color: #1890ff;
  font-size: 14px;
  cursor: pointer;
  transition: color 0.3s;
  padding: 0 4px;
}

.login-button:hover {
  color: #40a9ff;
  text-decoration: underline;
}

.login-button:disabled {
  color: #d9d9d9;
  cursor: not-allowed;
  text-decoration: none;
}

/* 注册提示 */
.register-tips {
  background-color: rgba(255, 255, 255, 0.9);
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.register-tips h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 16px;
}

.register-tips ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.register-tips li {
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
  position: relative;
  padding-left: 24px;
  line-height: 1.6;
}

.register-tips li:last-child {
  margin-bottom: 0;
}

.register-tips li::before {
  content: '💡';
  position: absolute;
  left: 0;
  top: 0;
  font-size: 16px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .register-container {
    grid-template-columns: 1fr;
    max-width: 500px;
  }
  
  .register-header {
    display: none;
  }
  
  .register-form {
    padding: 30px;
  }
  
  .register-tips {
    padding: 20px;
  }
}

@media (max-width: 480px) {
  .register-page {
    padding: 10px;
  }
  
  .register-form {
    padding: 24px;
  }
  
  .logo h1 {
    font-size: 36px;
  }
  
  .register-form h2 {
    font-size: 20px;
  }
}
</style>