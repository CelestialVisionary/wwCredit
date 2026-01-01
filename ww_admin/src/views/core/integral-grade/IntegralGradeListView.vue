<template>
  <div class="integral-grade-container">
    <div class="page-header">
      <h2>积分等级管理</h2>
      <button class="add-btn" @click="showAddForm = true">
        <span class="btn-icon">+</span>
        <span>新增积分等级</span>
      </button>
    </div>
    
    <!-- 积分等级列表 -->
    <div class="list-section">
      <div class="table-wrapper">
        <table class="data-table">
          <thead>
            <tr>
              <th>序号</th>
              <th>借款额度</th>
              <th>积分区间</th>
              <th>创建时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in filteredIntegralGrades" :key="item.id">
              <td>{{ index + 1 }}</td>
              <td>{{ item.borrowAmount }} 元</td>
              <td>{{ item.integralStart }} - {{ item.integralEnd }}</td>
              <td>{{ formatDate(item.createTime) }}</td>
              <td>
                <button class="btn-edit" @click="editItem(item)">编辑</button>
                <button class="btn-delete" @click="deleteItem(item.id)">删除</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      
      <!-- 暂无数据提示 -->
      <div v-if="filteredIntegralGrades.length === 0" class="empty-state">
        <p>暂无积分等级数据</p>
      </div>
    </div>
    
    <!-- 新增/编辑表单弹窗 -->
    <div v-if="showAddForm" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>{{ editingItem ? '编辑积分等级' : '新增积分等级' }}</h3>
          <button class="close-btn" @click="closeModal">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">

            <div class="form-group">
              <label for="integralStart">积分下限</label>
              <input 
                id="integralStart" 
                v-model.number="formData.integralStart" 
                type="number" 
                placeholder="请输入积分下限"
                required
              >
            </div>
            <div class="form-group">
              <label for="integralEnd">积分上限</label>
              <input 
                id="integralEnd" 
                v-model.number="formData.integralEnd" 
                type="number" 
                placeholder="请输入积分上限"
                required
              >
            </div>
            <div class="form-group">
              <label for="borrowAmount">借款额度</label>
              <input 
                id="borrowAmount" 
                v-model.number="formData.borrowAmount" 
                type="number" 
                placeholder="请输入借款额度"
                required
              >
            </div>
            <div class="form-actions">
              <button type="button" class="btn-cancel" @click="closeModal">取消</button>
              <button type="submit" class="btn-submit">保存</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

// 积分等级列表数据
const integralGrades = ref<Array<any>>([])
const showAddForm = ref(false)
const editingItem = ref<any>(null)
const statusFilter = ref('')
const filteredIntegralGrades = ref<any[]>([])

// 表单数据
const formData = reactive({
  id: undefined,
  integralStart: 0,
  integralEnd: 0,
  borrowAmount: 0
})

// 处理筛选条件变化
const handleFilterChange = () => {
  filterIntegralGrades()
}

// 重置筛选条件
const resetFilters = () => {
  statusFilter.value = ''
  filterIntegralGrades()
}

// 筛选积分等级列表
const filterIntegralGrades = () => {
  let filtered = [...integralGrades.value]
  // 状态筛选
  if (statusFilter.value) {
    filtered = filtered.filter(item => item.status === statusFilter.value)
  }
  filteredIntegralGrades.value = filtered
}

// 格式化日期
const formatDate = (dateString: string) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 打开新增表单
const openAddForm = () => {
  editingItem.value = null
  resetForm()
  showAddForm.value = true
}

// 编辑项目
const editItem = (item: any) => {
  editingItem.value = item
  formData.id = item.id
  formData.integralStart = item.integralStart
  formData.integralEnd = item.integralEnd
  formData.borrowAmount = item.borrowAmount
  showAddForm.value = true
}

// 获取积分等级列表
const fetchIntegralGrades = async () => {
  try {
    const response = await axios.get('/admin/core/integralGrade/list')
    integralGrades.value = response.data.data || []
    filterIntegralGrades()
  } catch (error) {
    console.error('获取积分等级列表失败:', error)
    ElMessage.error('获取积分等级列表失败，请稍后重试')
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    if (editingItem.value) {
      // 编辑操作
      await axios.post('/admin/core/integralGrade/update', formData)
      ElMessage.success('更新积分等级成功')
    } else {
      // 新增操作
      await axios.post('/admin/core/integralGrade/save', formData)
      ElMessage.success('新增积分等级成功')
    }
    closeModal()
    await fetchIntegralGrades() // 重新获取列表
  } catch (error: any) {
    console.error(editingItem.value ? '更新' : '新增', '积分等级失败:', error)
    ElMessage.error((error.response?.data?.msg || (editingItem.value ? '更新' : '新增') + '积分等级失败') + '，请稍后重试')
  }
}

// 删除项目
const deleteItem = async (id: number) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这个积分等级吗？',
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const response = await axios.post(`/admin/core/integralGrade/removeById?id=${id}`)
    if (response.data.code === 200) {
      ElMessage.success(response.data.msg || '删除成功')
      integralGrades.value = integralGrades.value.filter(item => item.id !== id)
      filterIntegralGrades()
    } else {
      ElMessage.error(response.data.msg || '删除失败')
    }
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('删除出错:', error)
      ElMessage.error('删除失败，请稍后重试')
    }
  }
}

// 关闭弹窗
const closeModal = () => {
  showAddForm.value = false
  resetForm()
  editingItem.value = null
}

// 重置表单
const resetForm = () => {
  // @ts-ignore
  formData.id = undefined
  formData.integralStart = 0
  formData.integralEnd = 0
  formData.borrowAmount = 0
}

// 组件挂载时获取数据
onMounted(() => {
  fetchIntegralGrades()
})
</script>

<style scoped>
.integral-grade-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-header h2 {
  margin: 0;
  color: #1e293b;
  font-size: 24px;
}

.add-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background-color: #3b82f6;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: background-color 0.3s;
}

.add-btn:hover {
  background-color: #2563eb;
}

.btn-icon {
  font-size: 16px;
  font-weight: bold;
}

.list-section {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.table-wrapper {
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #e2e8f0;
  color: #1e293b;
}

.data-table th {
  background-color: #f8fafc;
  font-weight: 600;
  color: #334155;
}

.data-table tr:hover {
  background-color: #f8fafc;
}

.btn-edit,
.btn-delete {
  padding: 6px 12px;
  margin-right: 8px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.btn-edit {
  background-color: #fbbf24;
  color: white;
}

.btn-edit:hover {
  background-color: #f59e0b;
}

.btn-delete {
  background-color: #ef4444;
  color: white;
}

.btn-delete:hover {
  background-color: #dc2626;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #64748b;
}

/* 弹窗样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #e2e8f0;
}

.modal-header h3 {
  margin: 0;
  color: #1e293b;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #64748b;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.close-btn:hover {
  background-color: #f1f5f9;
}

.modal-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  font-weight: 500;
  color: #334155;
}

.form-group input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.3s;
}

.form-group input:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.form-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 24px;
}

.btn-cancel,
.btn-submit {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: background-color 0.3s;
}

.btn-cancel {
  background-color: #f1f5f9;
  color: #64748b;
}

.btn-cancel:hover {
  background-color: #e2e8f0;
}

.btn-submit {
  background-color: #3b82f6;
  color: white;
}

.btn-submit:hover {
  background-color: #2563eb;
}
</style>