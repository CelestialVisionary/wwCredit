<script setup lang="ts">
import { ref, reactive, computed, watch } from 'vue'
import { useUserStore } from '../stores/user'

const userStore = useUserStore()

// 获取当前用户信息
const userInfo = computed(() => userStore.userInfo)

// 标签页切换
const activeTab = ref('avatar') // 'avatar' 或 'password'

// 修改头像部分
const avatarPreview = ref<string>('')
const avatarFile = ref<File | null>(null)
const isUploading = ref(false)
const uploadMessage = ref('')
const uploadProgress = ref(0)

// 修改密码部分
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})
const passwordMessage = ref('')
const isUpdatingPassword = ref(false)
const passwordStrength = ref('weak') // 'weak', 'medium', 'strong'

// 预览头像
const handleAvatarPreview = (event: Event) => {
  const input = event.target as HTMLInputElement
  if (input.files && input.files[0]) {
    avatarFile.value = input.files[0]
    const reader = new FileReader()
    reader.onload = e => {
      avatarPreview.value = e.target?.result as string
    }
    reader.readAsDataURL(input.files[0])
  }
}

// 上传头像
const handleAvatarUpload = async () => {
  if (!avatarFile.value) {
    uploadMessage.value = '请选择要上传的头像'
    return
  }
  
  isUploading.value = true
  uploadMessage.value = '上传中...'
  uploadProgress.value = 0
  
  try {
    // 模拟上传进度
    const progressInterval = setInterval(() => {
      uploadProgress.value += 10
      if (uploadProgress.value >= 100) {
        clearInterval(progressInterval)
        uploadProgress.value = 100
        
        // 上传成功
        setTimeout(() => {
          isUploading.value = false
          uploadMessage.value = '头像上传成功'
          // 更新用户信息中的头像
          if (userInfo.value) {
            userStore.setUserInfo({
              ...userInfo.value,
              headImg: avatarPreview.value
            })
          }
        }, 500)
      }
    }, 200)
  } catch (error) {
    isUploading.value = false
    uploadMessage.value = '头像上传失败'
    uploadProgress.value = 0
    console.error('Upload avatar failed:', error)
  }
}

// 验证密码表单
const validatePasswordForm = () => {
  if (!passwordForm.oldPassword) {
    passwordMessage.value = '请输入旧密码'
    return false
  }
  
  if (!passwordForm.newPassword) {
    passwordMessage.value = '请输入新密码'
    return false
  }
  
  if (passwordForm.newPassword.length < 6) {
    passwordMessage.value = '新密码长度不能少于6位'
    return false
  }
  
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    passwordMessage.value = '两次输入的新密码不一致'
    return false
  }
  
  return true
}

// 更新密码强度
const updatePasswordStrength = () => {
  const password = passwordForm.newPassword
  if (!password) {
    passwordStrength.value = 'weak'
    return
  }
  
  if (password.length < 6) {
    passwordStrength.value = 'weak'
  } else if (password.length >= 6 && password.length < 10) {
    passwordStrength.value = 'medium'
  } else {
    passwordStrength.value = 'strong'
  }
}

// 监听新密码变化，更新密码强度
watch(
  () => passwordForm.newPassword,
  () => updatePasswordStrength()
)

// 更新密码
const handleUpdatePassword = async () => {
  passwordMessage.value = ''
  
  if (!validatePasswordForm()) {
    return
  }
  
  isUpdatingPassword.value = true
  passwordMessage.value = '密码更新中...'
  
  try {
    // 这里应该实现真实的密码更新逻辑
    // 暂时模拟更新成功
    setTimeout(() => {
      isUpdatingPassword.value = false
      passwordMessage.value = '密码更新成功'
      // 重置表单
      passwordForm.oldPassword = ''
      passwordForm.newPassword = ''
      passwordForm.confirmPassword = ''
      passwordStrength.value = 'weak'
    }, 1500)
  } catch (error) {
    isUpdatingPassword.value = false
    passwordMessage.value = '密码更新失败'
    console.error('Update password failed:', error)
  }
}
</script>

<template>
  <div class="profile-view">
    <div class="profile-container">
      <!-- 左侧导航栏 -->
      <aside class="sidebar">
        <div class="sidebar-content">
          <div class="sidebar-header">
            <h2>用户中心</h2>
          </div>
          
          <nav class="sidebar-nav">
            <ul class="nav-list">
              <!-- 投资管理 -->
              <li class="nav-item">
                <h3 class="nav-title">投资管理</h3>
                <ul class="nav-submenu">
                  <li class="nav-subitem">
                    <a href="/invest" class="nav-link">我要投资</a>
                  </li>
                  <li class="nav-subitem">
                    <a href="/invest/records" class="nav-link">投资记录</a>
                  </li>
                </ul>
              </li>
              
              <!-- 借款管理 -->
              <li class="nav-item">
                <h3 class="nav-title">借款管理</h3>
                <ul class="nav-submenu">
                  <li class="nav-subitem">
                    <a href="/borrow-apply" class="nav-link">我要借款</a>
                  </li>
                  <li class="nav-subitem">
                    <a href="/borrow/records" class="nav-link">借款记录</a>
                  </li>
                </ul>
              </li>
              
              <!-- 资金管理 -->
              <li class="nav-item">
                <h3 class="nav-title">资金管理</h3>
                <ul class="nav-submenu">
                  <li class="nav-subitem">
                    <a href="/fund/records" class="nav-link">资金记录</a>
                  </li>
                  <li class="nav-subitem">
                    <a href="/fund/recharge" class="nav-link">充值</a>
                  </li>
                  <li class="nav-subitem">
                    <a href="/fund/withdraw" class="nav-link">提现</a>
                  </li>
                </ul>
              </li>
              
              <!-- 个人中心 -->
              <li class="nav-item">
                <h3 class="nav-title">个人中心</h3>
                <ul class="nav-submenu">
                  <li class="nav-subitem">
                    <a href="/profile" class="nav-link active">修改头像和密码</a>
                  </li>
                </ul>
              </li>
              
              <!-- 还款计划 -->
              <li class="nav-item">
                <a href="/repayment/plans" class="nav-link">还款计划</a>
              </li>
            </ul>
          </nav>
        </div>
      </aside>
      
      <!-- 右侧内容区域 -->
      <main class="main-content">
        <!-- 面包屑导航 -->
        <div class="breadcrumb">
          <a href="/home" class="breadcrumb-link">首页</a>
          <span class="breadcrumb-separator">></span>
          <a href="/profile" class="breadcrumb-link active">个人中心</a>
        </div>
        
        <h1>个人中心</h1>
        
        <!-- 个人信息展示 -->
        <section class="profile-section">
          <h2>个人信息</h2>
          <div class="profile-info">
            <div class="profile-item">
              <label>用户名：</label>
              <span>{{ userInfo?.name || '用户' }}</span>
            </div>
            <div class="profile-item">
              <label>手机号：</label>
              <span>{{ userInfo?.mobile || '' }}</span>
            </div>
            <div class="profile-item">
              <label>邮箱：</label>
              <span>{{ userInfo?.email || '未设置' }}</span>
            </div>
            <div class="profile-item">
              <label>用户类型：</label>
              <span>{{ userInfo?.userType === 1 ? '出借人' : '借款人' }}</span>
            </div>
            <div class="profile-item">
              <label>积分：</label>
              <span>{{ userInfo?.integral || 0 }}</span>
            </div>
            <div class="profile-item">
              <label>绑定状态：</label>
              <span>{{ userInfo?.bindStatus === 0 ? '未绑定' : '已绑定' }}</span>
            </div>
            <div class="profile-item">
              <label>借款授权：</label>
              <span>{{ userInfo?.borrowAuthStatus === 0 ? '未授权' : '已授权' }}</span>
            </div>
            <div class="profile-item">
              <label>注册时间：</label>
              <span>{{ userInfo?.createTime ? new Date(userInfo.createTime).toLocaleDateString() : '未知' }}</span>
            </div>
          </div>
        </section>
        
        <!-- 标签页导航 -->
        <div class="tabs">
          <button 
            class="tab-btn" 
            :class="{ active: activeTab === 'avatar' }" 
            @click="activeTab = 'avatar'"
          >
            修改头像
          </button>
          <button 
            class="tab-btn" 
            :class="{ active: activeTab === 'password' }" 
            @click="activeTab = 'password'"
          >
            修改密码
          </button>
        </div>
        
        <!-- 标签页内容 -->
        <div class="tab-content">
          <!-- 修改头像 -->
          <div v-if="activeTab === 'avatar'" class="tab-pane">
            <div class="avatar-section">
              <div class="avatar-preview">
                <img 
                  :src="avatarPreview || userInfo?.headImg || 'https://via.placeholder.com/150'" 
                  alt="头像" 
                  class="avatar-img"
                >
              </div>
              <div class="avatar-upload">
                <input 
                  type="file" 
                  id="avatar" 
                  accept="image/*" 
                  @change="handleAvatarPreview"
                  class="avatar-input"
                >
                <label for="avatar" class="avatar-label">选择头像</label>
                <button 
                  @click="handleAvatarUpload" 
                  class="btn btn-primary" 
                  :disabled="isUploading"
                >
                  {{ isUploading ? '上传中...' : '上传头像' }}
                </button>
                
                <!-- 上传进度条 -->
                <div v-if="isUploading" class="progress-container">
                  <div class="progress-bar">
                    <div class="progress" :style="{ width: `${uploadProgress}%` }"></div>
                  </div>
                  <span class="progress-text">{{ uploadProgress }}%</span>
                </div>
                
                <p class="message" :class="{ success: uploadMessage.includes('成功'), error: uploadMessage.includes('失败') || uploadMessage.includes('请选择') }">
                  {{ uploadMessage }}
                </p>
              </div>
            </div>
          </div>
          
          <!-- 修改密码 -->
          <div v-if="activeTab === 'password'" class="tab-pane">
            <form class="password-form" @submit.prevent="handleUpdatePassword">
              <div class="form-group">
                <label for="oldPassword">旧密码</label>
                <input 
                  type="password" 
                  id="oldPassword" 
                  v-model="passwordForm.oldPassword" 
                  placeholder="请输入旧密码"
                >
              </div>
              <div class="form-group">
                <label for="newPassword">新密码</label>
                <input 
                  type="password" 
                  id="newPassword" 
                  v-model="passwordForm.newPassword" 
                  placeholder="请输入新密码"
                >
                
                <!-- 密码强度提示 -->
                <div class="password-strength">
                  <div class="strength-label">密码强度：</div>
                  <div class="strength-indicator">
                    <div 
                      class="strength-bar"
                      :class="passwordStrength"
                    ></div>
                  </div>
                  <div class="strength-text">
                    {{ 
                      passwordStrength === 'weak' ? '弱' : 
                      passwordStrength === 'medium' ? '中' : '强' 
                    }}
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label for="confirmPassword">确认新密码</label>
                <input 
                  type="password" 
                  id="confirmPassword" 
                  v-model="passwordForm.confirmPassword" 
                  placeholder="请再次输入新密码"
                >
                <div v-if="passwordForm.oldPassword && passwordForm.newPassword && passwordForm.confirmPassword" class="password-tip">
                  <div v-if="passwordForm.newPassword !== passwordForm.confirmPassword" class="tip error">
                    两次输入的新密码不一致
                  </div>
                  <div v-else class="tip success">
                    两次输入的新密码一致
                  </div>
                </div>
              </div>
              <button 
                type="submit" 
                class="btn btn-primary" 
                :disabled="isUpdatingPassword"
              >
                {{ isUpdatingPassword ? '更新中...' : '更新密码' }}
              </button>
              <p class="message" :class="{ success: passwordMessage.includes('成功'), error: passwordMessage.includes('失败') || passwordMessage.includes('请输入') }">
                {{ passwordMessage }}
              </p>
            </form>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<style scoped>
.profile-view {
  background-color: #f5f7fa;
  min-height: calc(100vh - 64px - 200px);
}

.profile-container {
  display: flex;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/* 左侧导航栏，复用HomeView.vue的样式 */
.sidebar {
  width: 240px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-right: 20px;
  flex-shrink: 0;
}

.sidebar-content {
  padding: 20px;
}

.sidebar-header h2 {
  font-size: 18px;
  color: #333;
  margin-bottom: 20px;
  text-align: center;
}

.nav-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.nav-item {
  margin-bottom: 16px;
}

.nav-title {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin-bottom: 12px;
  padding-left: 8px;
  border-left: 3px solid #1890ff;
}

.nav-submenu {
  list-style: none;
  padding-left: 24px;
  margin: 0;
}

.nav-subitem {
  margin-bottom: 8px;
}

.nav-link {
  display: block;
  padding: 8px 12px;
  color: #666;
  text-decoration: none;
  border-radius: 4px;
  transition: all 0.3s;
  font-size: 14px;
}

.nav-link:hover, .nav-link.active {
  color: #1890ff;
  background-color: #e6f7ff;
}

/* 右侧内容区域 */
.main-content {
  flex: 1;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  overflow: auto;
}

.main-content h1 {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
  border-bottom: 1px solid #e8e8e8;
  padding-bottom: 10px;
}

.main-content h2 {
  font-size: 20px;
  color: #333;
  margin: 20px 0 15px;
}

/* 个人信息展示 */
.profile-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.profile-item {
  display: flex;
  align-items: center;
}

.profile-item label {
  font-weight: 500;
  color: #666;
  margin-right: 10px;
  width: 80px;
}

.profile-item span {
  color: #333;
}

/* 修改头像 */
.avatar-section {
  display: flex;
  align-items: center;
  gap: 30px;
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.avatar-preview {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  overflow: hidden;
  border: 3px solid #e8e8e8;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-upload {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.avatar-input {
  display: none;
}

.avatar-label {
  padding: 8px 16px;
  background-color: #f0f0f0;
  color: #333;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  text-align: center;
  width: 100px;
}

.avatar-label:hover {
  background-color: #e6f7ff;
  color: #1890ff;
}

/* 修改密码 */
.password-form {
  display: flex;
  flex-direction: column;
  gap: 15px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 8px;
  max-width: 500px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.form-group label {
  font-weight: 500;
  color: #666;
}

.form-group input {
  padding: 10px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  font-size: 14px;
}

.form-group input:focus {
  outline: none;
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

/* 按钮样式 */
.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
  text-align: center;
}

.btn-primary {
  background-color: #1890ff;
  color: white;
}

.btn-primary:hover {
  background-color: #40a9ff;
}

.btn:disabled {
  background-color: #d9d9d9;
  cursor: not-allowed;
}

/* 消息提示 */
.message {
  margin: 10px 0 0;
  font-size: 14px;
  line-height: 1.4;
}

.message.success {
  color: #52c41a;
}

.message.error {
  color: #ff4d4f;
}

/* 面包屑导航 */
.breadcrumb {
  margin-bottom: 16px;
  font-size: 14px;
  color: #666;
}

.breadcrumb-link {
  color: #1890ff;
  text-decoration: none;
  margin: 0 5px;
}

.breadcrumb-link:hover {
  text-decoration: underline;
}

.breadcrumb-link.active {
  color: #666;
  cursor: default;
}

.breadcrumb-link.active:hover {
  text-decoration: none;
}

.breadcrumb-separator {
  margin: 0 5px;
  color: #999;
}

/* 标签页样式 */
.tabs {
  display: flex;
  margin: 20px 0;
  border-bottom: 1px solid #e8e8e8;
}

.tab-btn {
  padding: 12px 24px;
  background: none;
  border: none;
  font-size: 16px;
  color: #666;
  cursor: pointer;
  transition: all 0.3s;
  border-bottom: 2px solid transparent;
  margin-right: 20px;
}

.tab-btn:hover {
  color: #1890ff;
  border-bottom-color: #e6f7ff;
}

.tab-btn.active {
  color: #1890ff;
  border-bottom-color: #1890ff;
  font-weight: 500;
}

/* 标签页内容 */
.tab-content {
  background-color: #f5f7fa;
  padding: 20px;
  border-radius: 8px;
}

.tab-pane {
  display: block;
}

/* 上传进度条 */
.progress-container {
  display: flex;
  align-items: center;
  margin: 10px 0;
  gap: 10px;
}

.progress-bar {
  flex: 1;
  height: 8px;
  background-color: #e8e8e8;
  border-radius: 4px;
  overflow: hidden;
}

.progress {
  height: 100%;
  background-color: #1890ff;
  border-radius: 4px;
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 12px;
  color: #666;
  min-width: 40px;
  text-align: right;
}

/* 密码强度指示器 */
.password-strength {
  display: flex;
  align-items: center;
  margin: 8px 0;
  gap: 10px;
}

.strength-label {
  font-size: 14px;
  color: #666;
  width: 70px;
}

.strength-indicator {
  flex: 1;
  height: 6px;
  background-color: #e8e8e8;
  border-radius: 3px;
  overflow: hidden;
}

.strength-bar {
  height: 100%;
  border-radius: 3px;
  transition: all 0.3s;
}

.strength-bar.weak {
  width: 33%;
  background-color: #ff4d4f;
}

.strength-bar.medium {
  width: 66%;
  background-color: #faad14;
}

.strength-bar.strong {
  width: 100%;
  background-color: #52c41a;
}

.strength-text {
  font-size: 14px;
  min-width: 40px;
  text-align: right;
}

.strength-text.weak {
  color: #ff4d4f;
}

.strength-text.medium {
  color: #faad14;
}

.strength-text.strong {
  color: #52c41a;
}

/* 密码提示 */
.password-tip {
  margin-top: 8px;
  font-size: 14px;
}

.tip {
  padding: 8px 12px;
  border-radius: 4px;
}

.tip.success {
  background-color: #f6ffed;
  color: #52c41a;
  border: 1px solid #b7eb8f;
}

.tip.error {
  background-color: #fff2f0;
  color: #ff4d4f;
  border: 1px solid #ffccc7;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .profile-container {
    flex-direction: column;
    padding: 10px;
  }
  
  .sidebar {
    width: 100%;
    margin-right: 0;
    margin-bottom: 20px;
  }
  
  .main-content {
    padding: 16px;
  }
  
  .avatar-section {
    flex-direction: column;
    align-items: flex-start;
    gap: 20px;
  }
  
  .profile-info {
    grid-template-columns: 1fr;
  }
  
  .tabs {
    flex-direction: column;
    border-bottom: none;
  }
  
  .tab-btn {
    margin-right: 0;
    margin-bottom: 10px;
    border-bottom: 1px solid #e8e8e8;
  }
  
  .tab-btn.active {
    border-bottom-color: #1890ff;
  }
}

</style>
