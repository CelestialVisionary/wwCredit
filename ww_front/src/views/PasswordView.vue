<template>
  <div class="password-view">
    <!-- 面包屑导航 -->
    <div class="breadcrumb">
      <router-link to="/">首页</router-link>
      <span class="separator">/</span>
      <router-link to="/profile">个人中心</router-link>
      <span class="separator">/</span>
      <span class="current">修改密码</span>
    </div>

    <!-- 密码修改表单 -->
    <div class="container">
      <h2 class="title">修改密码</h2>
      
      <div class="user-info">
        <div class="user-avatar">
          <img :src="userInfo?.headImg || 'https://via.placeholder.com/80'" alt="用户头像">
        </div>
        <div class="user-details">
          <h3 class="username">{{ userInfo?.name || '未知用户' }}</h3>
          <p class="user-role">{{ userInfo?.userType === 1 ? '出借人' : '借款人' }}</p>
        </div>
      </div>

      <form @submit.prevent="handleSubmit" class="password-form">
        <div class="form-group">
          <label for="oldPassword" class="form-label">原密码</label>
          <input 
            type="password" 
            id="oldPassword" 
            v-model="form.oldPassword" 
            placeholder="请输入原密码" 
            class="form-input" 
            :class="{ 'is-error': errors.oldPassword }"
          >
          <div v-if="errors.oldPassword" class="error-message">{{ errors.oldPassword }}</div>
        </div>

        <div class="form-group">
          <label for="newPassword" class="form-label">新密码</label>
          <input 
            type="password" 
            id="newPassword" 
            v-model="form.newPassword" 
            placeholder="请输入新密码" 
            class="form-input" 
            :class="{ 'is-error': errors.newPassword }"
          >
          <div v-if="errors.newPassword" class="error-message">{{ errors.newPassword }}</div>
        </div>

        <div class="form-group">
          <label for="confirmPassword" class="form-label">确认新密码</label>
          <input 
            type="password" 
            id="confirmPassword" 
            v-model="form.confirmPassword" 
            placeholder="请再次输入新密码" 
            class="form-input" 
            :class="{ 'is-error': errors.confirmPassword }"
          >
          <div v-if="errors.confirmPassword" class="error-message">{{ errors.confirmPassword }}</div>
        </div>

        <!-- 反馈消息 -->
        <div v-if="message" class="message" :class="messageType">
          {{ message }}
        </div>

        <div class="form-actions">
          <button 
            type="submit" 
            class="submit-btn" 
            :disabled="isSubmitting"
          >
            <span v-if="isSubmitting">提交中...</span>
            <span v-else>确认修改</span>
          </button>
          <button 
            type="button" 
            class="cancel-btn" 
            @click="resetForm"
          >
            重置
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue';
import { useUserStore } from '../stores/user';

// 从Pinia store获取用户信息
const userStore = useUserStore();
const userInfo = computed(() => userStore.userInfo);

// 表单数据
const form = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
});

// 表单验证错误
const errors = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
});

// 提交状态和反馈消息
const isSubmitting = ref(false);
const message = ref('');
const messageType = ref('');

// 表单验证
const validateForm = (): boolean => {
  let isValid = true;
  
  // 重置错误
  Object.keys(errors).forEach(key => {
    errors[key as keyof typeof errors] = '';
  });
  
  // 验证原密码
  if (!form.oldPassword) {
    errors.oldPassword = '请输入原密码';
    isValid = false;
  }
  
  // 验证新密码
  if (!form.newPassword) {
    errors.newPassword = '请输入新密码';
    isValid = false;
  } else if (form.newPassword.length < 6) {
    errors.newPassword = '新密码长度不能少于6位';
    isValid = false;
  } else if (form.newPassword === form.oldPassword) {
    errors.newPassword = '新密码不能与原密码相同';
    isValid = false;
  }
  
  // 验证确认新密码
  if (!form.confirmPassword) {
    errors.confirmPassword = '请确认新密码';
    isValid = false;
  } else if (form.confirmPassword !== form.newPassword) {
    errors.confirmPassword = '两次输入的新密码不一致';
    isValid = false;
  }
  
  return isValid;
};

// 提交表单
const handleSubmit = async () => {
  if (!validateForm()) {
    return;
  }
  
  isSubmitting.value = true;
  message.value = '';
  
  try {
    // 这里应该调用真实的API来修改密码
    // await userStore.changePassword(form.oldPassword, form.newPassword);
    
    // 模拟API请求
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    message.value = '密码修改成功';
    messageType.value = 'success';
    
    // 重置表单
    resetForm();
  } catch (error) {
    message.value = error instanceof Error ? error.message : '密码修改失败，请稍后重试';
    messageType.value = 'error';
  } finally {
    isSubmitting.value = false;
  }
};

// 重置表单
const resetForm = () => {
  form.oldPassword = '';
  form.newPassword = '';
  form.confirmPassword = '';
  
  Object.keys(errors).forEach(key => {
    errors[key as keyof typeof errors] = '';
  });
  
  message.value = '';
};
</script>

<style scoped>
.password-view {
  min-height: 100vh;
  background-color: #f5f7fa;
  padding: 20px;
}

.breadcrumb {
  margin-bottom: 20px;
  font-size: 14px;
  color: #666;
}

.breadcrumb a {
  color: #409eff;
  text-decoration: none;
}

.breadcrumb a:hover {
  text-decoration: underline;
}

.separator {
  margin: 0 8px;
  color: #ccc;
}

.current {
  color: #333;
  font-weight: 500;
}

.container {
  max-width: 600px;
  margin: 0 auto;
  background-color: #fff;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 24px;
}

.user-info {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.user-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 20px;
  border: 2px solid #eee;
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-details {
  flex: 1;
}

.username {
  margin: 0 0 8px 0;
  color: #333;
  font-size: 18px;
}

.user-role {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.password-form {
  width: 100%;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  margin-bottom: 8px;
  color: #333;
  font-weight: 500;
  font-size: 14px;
}

.form-input {
  width: 100%;
  padding: 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
  transition: border-color 0.2s;
  box-sizing: border-box;
}

.form-input:focus {
  outline: none;
  border-color: #409eff;
}

.form-input.is-error {
  border-color: #f56c6c;
}

.error-message {
  margin-top: 5px;
  color: #f56c6c;
  font-size: 12px;
}

.message {
  margin-bottom: 20px;
  padding: 12px;
  border-radius: 4px;
  font-size: 14px;
  text-align: center;
}

.message.success {
  background-color: #f0f9eb;
  color: #67c23a;
  border: 1px solid #e1f3d8;
}

.message.error {
  background-color: #fef0f0;
  color: #f56c6c;
  border: 1px solid #fbc4c4;
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 30px;
}

.submit-btn, .cancel-btn {
  padding: 12px 30px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.submit-btn {
  background-color: #409eff;
  color: #fff;
}

.submit-btn:hover:not(:disabled) {
  background-color: #66b1ff;
}

.submit-btn:disabled {
  background-color: #a0cfff;
  cursor: not-allowed;
}

.cancel-btn {
  background-color: #f5f7fa;
  color: #606266;
  border: 1px solid #dcdfe6;
}

.cancel-btn:hover {
  background-color: #e4e7ed;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .container {
    padding: 20px;
  }
  
  .user-info {
    flex-direction: column;
    text-align: center;
  }
  
  .user-avatar {
    margin-right: 0;
    margin-bottom: 15px;
  }
  
  .form-actions {
    flex-direction: column;
  }
  
  .submit-btn, .cancel-btn {
    width: 100%;
  }
}
</style>
