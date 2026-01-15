<script setup lang="ts">
import { ref, computed } from 'vue'
import { useUserStore } from '../stores/user'

const userStore = useUserStore()

// 获取当前用户信息
const userInfo = computed(() => userStore.userInfo)

// 修改头像部分
const avatarPreview = ref<string>('')
const avatarFile = ref<File | null>(null)
const isUploading = ref(false)
const uploadMessage = ref('')
const uploadProgress = ref(0)

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
          uploadMessage.value = '上传成功'
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
    uploadMessage.value = '上传失败'
    console.error('Upload avatar failed:', error)
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
                    <a href="/profile/avatar" class="nav-link active">修改头像</a>
                  </li>
                  <li class="nav-subitem">
                    <a href="/profile/password" class="nav-link">修改密码</a>
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
          <a href="/profile/avatar" class="breadcrumb-link active">修改头像</a>
        </div>
        
        <h1>修改头像</h1>
        
        <!-- 修改头像 -->
        <section class="avatar-section">
          <div class="avatar-preview-container">
            <img 
              :src="avatarPreview || userInfo?.headImg || 'https://via.placeholder.com/150'" 
              alt="头像" 
              class="avatar-img"
            >
          </div>
          <div class="avatar-upload-form">
            <input 
              type="file" 
              id="avatar" 
              accept="image/*" 
              @change="handleAvatarPreview"
              class="avatar-input"
              style="display: none"
            >
            <label for="avatar" class="btn btn-secondary">选择头像</label>
            <button 
              @click="handleAvatarUpload" 
              class="btn btn-primary" 
              :disabled="isUploading"
            >
              {{ isUploading ? '上传中...' : '上传头像' }}
            </button>
            <p class="upload-message" :class="{ success: uploadMessage.includes('成功'), error: uploadMessage.includes('失败') || uploadMessage.includes('请选择') }">
              {{ uploadMessage }}
            </p>
            
            <!-- 上传进度条 -->
            <div v-if="isUploading" class="progress-container">
              <div class="progress-bar">
                <div class="progress" :style="{ width: `${uploadProgress}%` }"></div>
              </div>
            </div>
          </div>
        </section>
      </main>
    </div>
  </div>
</template>

<style scoped>
.profile-view {
  display: flex;
  background-color: #f5f7fa;
  min-height: calc(100vh - 64px - 200px);
}

.profile-container {
  display: flex;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
  padding: 20px;
}

/* 左侧导航栏 */
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
  padding: 0;
  margin: 0;
  padding-left: 24px;
}

.nav-subitem {
  margin-bottom: 8px;
}

.nav-link {
  display: block;
  padding: 8px 12px;
  color: #666;
  text-decoration: none;
  transition: all 0.3s;
  border-radius: 4px;
}

.nav-link:hover {
  color: #1890ff;
  background-color: #e6f7ff;
}

.nav-link.active {
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

/* 标题 */
h1 {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
  border-bottom: 1px solid #e8e8e8;
  padding-bottom: 10px;
}

/* 修改头像部分 */
.avatar-section {
  display: flex;
  align-items: center;
  gap: 30px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.avatar-preview-container {
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

.avatar-upload-form {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-primary {
  background-color: #1890ff;
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background-color: #40a9ff;
}

.btn-secondary {
  background-color: #f0f0f0;
  color: #333;
}

.btn-secondary:hover {
  background-color: #e6f7ff;
  color: #1890ff;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.upload-message {
  margin-top: 10px;
  font-size: 14px;
  line-height: 1.4;
}

.upload-message.success {
  color: #52c41a;
}

.upload-message.error {
  color: #ff4d4f;
}

/* 进度条 */
.progress-container {
  margin-top: 10px;
  width: 100%;
  height: 8px;
  background-color: #e8e8e8;
  border-radius: 4px;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  background-color: #1890ff;
  transition: width 0.3s ease;
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
  
  .avatar-section {
    flex-direction: column;
    align-items: center;
    gap: 20px;
  }
  
  .avatar-upload-form {
    align-items: center;
  }
}
</style>
