<template>
  <div class="settings-view">
    <div class="page-header">
      <h2 class="page-title">系统设置</h2>
    </div>
    
    <div class="settings-tabs">
      <button 
        class="tab-btn" 
        :class="{ active: activeTab === 'basic' }"
        @click="activeTab = 'basic'"
      >
        基本设置
      </button>
      <button 
        class="tab-btn" 
        :class="{ active: activeTab === 'limits' }"
        @click="activeTab = 'limits'"
      >
        贷款限制
      </button>
      <button 
        class="tab-btn" 
        :class="{ active: activeTab === 'notifications' }"
        @click="activeTab = 'notifications'"
      >
        通知设置
      </button>
      <button 
        class="tab-btn" 
        :class="{ active: activeTab === 'security' }"
        @click="activeTab = 'security'"
      >
        安全设置
      </button>
    </div>
    
    <!-- 基本设置 -->
    <div v-if="activeTab === 'basic'" class="settings-content">
      <div class="settings-card">
        <h3 class="card-title">平台信息</h3>
        <div class="form-row">
          <div class="form-group">
            <label>平台名称</label>
            <input type="text" v-model="platformName" placeholder="请输入平台名称">
          </div>
          <div class="form-group">
            <label>平台Logo</label>
            <div class="file-upload">
              <input type="file" ref="logoUpload" @change="handleLogoUpload">
              <button type="button" class="file-upload-btn">上传Logo</button>
            </div>
          </div>
        </div>
        <div class="form-row">
          <div class="form-group">
            <label>客服热线</label>
            <input type="text" v-model="customerServiceNumber" placeholder="请输入客服热线">
          </div>
          <div class="form-group">
            <label>客服邮箱</label>
            <input type="email" v-model="customerServiceEmail" placeholder="请输入客服邮箱">
          </div>
        </div>
      </div>
      
      <div class="settings-card">
        <h3 class="card-title">运营设置</h3>
        <div class="form-row">
          <div class="form-group">
            <label>注册奖励</label>
            <input type="number" v-model="registrationReward" min="0" step="0.01" placeholder="请输入注册奖励金额">
          </div>
          <div class="form-group">
            <label>邀请奖励</label>
            <input type="number" v-model="inviteReward" min="0" step="0.01" placeholder="请输入邀请奖励金额">
          </div>
        </div>
        <div class="form-group">
          <label>首页推荐标语</label>
          <textarea v-model="homePageSlogan" rows="3" placeholder="请输入首页推荐标语"></textarea>
        </div>
      </div>
    </div>
    
    <!-- 贷款限制 -->
    <div v-if="activeTab === 'limits'" class="settings-content">
      <div class="settings-card">
        <h3 class="card-title">额度限制</h3>
        <div class="form-row">
          <div class="form-group">
            <label>最低贷款额度</label>
            <input type="number" v-model="minLoanAmount" min="0" step="100" placeholder="请输入最低贷款额度">
          </div>
          <div class="form-group">
            <label>最高贷款额度</label>
            <input type="number" v-model="maxLoanAmount" min="0" step="100" placeholder="请输入最高贷款额度">
          </div>
        </div>
        <div class="form-row">
          <div class="form-group">
            <label>最低单笔借款</label>
            <input type="number" v-model="minSingleLoan" min="0" step="100" placeholder="请输入最低单笔借款金额">
          </div>
          <div class="form-group">
            <label>最高单笔借款</label>
            <input type="number" v-model="maxSingleLoan" min="0" step="100" placeholder="请输入最高单笔借款金额">
          </div>
        </div>
      </div>
      
      <div class="settings-card">
        <h3 class="card-title">期限设置</h3>
        <div class="form-row">
          <div class="form-group">
            <label>最短借款期限</label>
            <input type="number" v-model="minLoanTerm" min="1" max="365" placeholder="请输入最短借款期限（天）">
          </div>
          <div class="form-group">
            <label>最长借款期限</label>
            <input type="number" v-model="maxLoanTerm" min="1" max="365" placeholder="请输入最长借款期限（天）">
          </div>
        </div>
        <div class="form-group">
          <label>可选项期限</label>
          <div class="checkbox-group">
            <label v-for="term in availableTerms" :key="term" class="checkbox-item">
              <input 
                type="checkbox" 
                :value="term" 
                v-model="selectedTerms"
              >
              {{ term }}天
            </label>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 通知设置 -->
    <div v-if="activeTab === 'notifications'" class="settings-content">
      <div class="settings-card">
        <h3 class="card-title">系统通知</h3>
        <div class="form-group checkbox-option">
          <label class="checkbox-label">
            <input type="checkbox" v-model="systemNotificationEnabled">
            <span>启用系统通知</span>
          </label>
          <p class="help-text">开启后系统将推送平台更新、维护等通知</p>
        </div>
        <div class="form-group checkbox-option">
          <label class="checkbox-label">
            <input type="checkbox" v-model="marketingNotificationEnabled">
            <span>启用营销通知</span>
          </label>
          <p class="help-text">开启后系统将推送活动、优惠等营销信息</p>
        </div>
      </div>
      
      <div class="settings-card">
        <h3 class="card-title">模板设置</h3>
        <div class="form-group">
          <label>短信验证码模板</label>
          <textarea v-model="smsVerificationTemplate" rows="3" placeholder="请输入短信验证码模板，使用{code}作为验证码占位符"></textarea>
        </div>
        <div class="form-group">
          <label>借款成功通知模板</label>
          <textarea v-model="loanSuccessTemplate" rows="3" placeholder="请输入借款成功通知模板"></textarea>
        </div>
      </div>
    </div>
    
    <!-- 安全设置 -->
    <div v-if="activeTab === 'security'" class="settings-content">
      <div class="settings-card">
        <h3 class="card-title">认证设置</h3>
        <div class="form-group checkbox-option">
          <label class="checkbox-label">
            <input type="checkbox" v-model="enableFaceVerification">
            <span>启用人脸识别认证</span>
          </label>
        </div>
        <div class="form-group checkbox-option">
          <label class="checkbox-label">
            <input type="checkbox" v-model="enableBankCardVerification">
            <span>启用银行卡认证</span>
          </label>
        </div>
        <div class="form-group checkbox-option">
          <label class="checkbox-label">
            <input type="checkbox" v-model="enablePhoneVerification">
            <span>启用手机号认证</span>
          </label>
        </div>
      </div>
      
      <div class="settings-card">
        <h3 class="card-title">风控设置</h3>
        <div class="form-row">
          <div class="form-group">
            <label>风控分数阈值</label>
            <input type="number" v-model="riskScoreThreshold" min="0" max="100" placeholder="请输入风控分数阈值">
          </div>
          <div class="form-group">
            <label>信用评级更新频率</label>
            <select v-model="creditUpdateFrequency">
              <option value="daily">每天</option>
              <option value="weekly">每周</option>
              <option value="monthly">每月</option>
            </select>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 操作按钮 -->
    <div class="settings-actions">
      <button class="btn btn-secondary" @click="cancelChanges">取消</button>
      <button class="btn btn-primary" @click="saveSettings">保存设置</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

// 标签页状态
const activeTab = ref('basic')

// 文件上传引用
const logoUpload = ref<HTMLInputElement | null>(null)

// 基本设置
const platformName = ref('威武信贷')
const customerServiceNumber = ref('400-123-4567')
const customerServiceEmail = ref('service@wwcredit.com')
const registrationReward = ref(10.0)
const inviteReward = ref(50.0)
const homePageSlogan = ref('安全快捷的个人信贷服务平台')

// 贷款限制
const minLoanAmount = ref(1000)
const maxLoanAmount = ref(50000)
const minSingleLoan = ref(500)
const maxSingleLoan = ref(50000)
const minLoanTerm = ref(7)
const maxLoanTerm = ref(90)
const availableTerms = [7, 15, 30, 45, 60, 90]
const selectedTerms = ref([7, 15, 30])

// 通知设置
const systemNotificationEnabled = ref(true)
const marketingNotificationEnabled = ref(true)
const smsVerificationTemplate = ref('您的验证码是：{code}，有效期为5分钟，请勿泄露给他人。')
const loanSuccessTemplate = ref('尊敬的用户，您的借款已成功放款，金额为{amount}元，期限为{term}天，到期日为{dueDate}。感谢您的信任与支持！')

// 安全设置
const enableFaceVerification = ref(true)
const enableBankCardVerification = ref(true)
const enablePhoneVerification = ref(true)
const riskScoreThreshold = ref(60)
const creditUpdateFrequency = ref('monthly')

// 方法
const handleLogoUpload = () => {
  if (logoUpload.value?.files?.[0]) {
    console.log('Logo文件已选择:', logoUpload.value.files[0].name)
    // 这里可以实现文件上传的逻辑
  }
}

const cancelChanges = () => {
  if (confirm('确定要取消所有更改吗？')) {
    console.log('取消更改')
    // 这里可以实现重置表单的逻辑
  }
}

const saveSettings = () => {
  console.log('保存设置:', {
    [activeTab.value]: {
      // 根据当前标签页获取对应的设置
      ...(activeTab.value === 'basic' && {
        platformName: platformName.value,
        customerServiceNumber: customerServiceNumber.value,
        customerServiceEmail: customerServiceEmail.value,
        registrationReward: registrationReward.value,
        inviteReward: inviteReward.value,
        homePageSlogan: homePageSlogan.value
      }),
      ...(activeTab.value === 'limits' && {
        minLoanAmount: minLoanAmount.value,
        maxLoanAmount: maxLoanAmount.value,
        minSingleLoan: minSingleLoan.value,
        maxSingleLoan: maxSingleLoan.value,
        minLoanTerm: minLoanTerm.value,
        maxLoanTerm: maxLoanTerm.value,
        selectedTerms: selectedTerms.value
      }),
      ...(activeTab.value === 'notifications' && {
        systemNotificationEnabled: systemNotificationEnabled.value,
        marketingNotificationEnabled: marketingNotificationEnabled.value,
        smsVerificationTemplate: smsVerificationTemplate.value,
        loanSuccessTemplate: loanSuccessTemplate.value
      }),
      ...(activeTab.value === 'security' && {
        enableFaceVerification: enableFaceVerification.value,
        enableBankCardVerification: enableBankCardVerification.value,
        enablePhoneVerification: enablePhoneVerification.value,
        riskScoreThreshold: riskScoreThreshold.value,
        creditUpdateFrequency: creditUpdateFrequency.value
      })
    }
  })
  // 这里可以实现保存设置的逻辑
  alert('设置保存成功！')
}
</script>

<style scoped>
.settings-view {
  padding: 20px 0;
}

.page-header {
  margin-bottom: 24px;
}

.page-title {
  font-size: 24px;
  color: #1e293b;
}

.settings-tabs {
  display: flex;
  border-bottom: 1px solid #e2e8f0;
  margin-bottom: 24px;
}

.tab-btn {
  padding: 12px 24px;
  background: none;
  border: none;
  border-bottom: 3px solid transparent;
  font-size: 16px;
  color: #64748b;
  cursor: pointer;
  transition: all 0.3s;
}

.tab-btn:hover {
  color: #3b82f6;
}

.tab-btn.active {
  color: #3b82f6;
  border-bottom-color: #3b82f6;
}

.settings-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.settings-card {
  background-color: white;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.card-title {
  font-size: 18px;
  color: #1e293b;
  margin-bottom: 20px;
}

.form-row {
  display: flex;
  gap: 24px;
  margin-bottom: 20px;
}

.form-group {
  flex: 1;
}

.form-group label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #374151;
  margin-bottom: 8px;
}

.form-group input[type="text"],
.form-group input[type="email"],
.form-group input[type="number"],
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 10px 16px;
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.3s;
}

.form-group input[type="text"]:focus,
.form-group input[type="email"]:focus,
.form-group input[type="number"]:focus,
.form-group textarea:focus,
.form-group select:focus {
  outline: none;
  border-color: #3b82f6;
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
}

.file-upload {
  position: relative;
}

.file-upload input[type="file"] {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  cursor: pointer;
}

.file-upload-btn {
  width: 100%;
  padding: 10px 16px;
  border: 1px dashed #e2e8f0;
  background-color: #f8fafc;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  color: #64748b;
  transition: all 0.3s;
}

.file-upload-btn:hover {
  background-color: #eff6ff;
  border-color: #3b82f6;
  color: #3b82f6;
}

.checkbox-group {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.checkbox-item {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #374151;
  cursor: pointer;
}

.checkbox-item input[type="checkbox"] {
  margin-right: 8px;
}

.checkbox-option {
  margin-bottom: 16px;
}

.checkbox-label {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #374151;
  cursor: pointer;
}

.checkbox-label input[type="checkbox"] {
  margin-right: 8px;
}

.help-text {
  margin-top: 4px;
  font-size: 12px;
  color: #64748b;
}

.settings-actions {
  display: flex;
  gap: 16px;
  margin-top: 32px;
  justify-content: flex-end;
}

.btn {
  padding: 10px 24px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-primary {
  background-color: #3b82f6;
  color: white;
  border: none;
}

.btn-primary:hover {
  background-color: #2563eb;
}

.btn-secondary {
  background-color: white;
  color: #64748b;
  border: 1px solid #e2e8f0;
}

.btn-secondary:hover {
  background-color: #f8fafc;
}

@media (max-width: 768px) {
  .settings-tabs {
    flex-wrap: wrap;
  }
  
  .form-row {
    flex-direction: column;
  }
  
  .checkbox-group {
    flex-direction: column;
    gap: 8px;
  }
  
  .settings-actions {
    flex-direction: column;
  }
}
</style>