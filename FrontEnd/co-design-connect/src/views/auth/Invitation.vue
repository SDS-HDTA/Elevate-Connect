<template>
  <div class="manager-page-content">
    <div class="invitation-container">
      <h2>Send Invitation</h2>
      <div class="invitation-form">
        <el-input
          v-model="email"
          placeholder="Please enter email address"
          class="email-input"
          size="large"
        >
          <template #prefix>
            <el-icon><Message /></el-icon>
          </template>
        </el-input>
        
        <el-radio-group v-model="userType" class="user-type-group">
          <el-radio :label="0">Local Partner</el-radio>
          <el-radio :label="1">Organization Partner</el-radio>
        </el-radio-group>

        <el-button 
          type="primary" 
          size="large" 
          class="send-button"
          :loading="loading"
          @click="handleSendInvitation"
        >
          Send Invitation Code
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Message } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const email = ref('')
const loading = ref(false)
const userType = ref(0) // 默认为 Local Partner

const handleSendInvitation = async () => {
  if (!email.value) {
    ElMessage.warning('Please enter email address')
    return
  }
  
  // Simple email format validation
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(email.value)) {
    ElMessage.warning('Please enter a valid email address')
    return
  }

  try {
    loading.value = true
    await request.post('/invitation/send', { 
      email: email.value,
      type: userType.value 
    })
    ElMessage.success('Invitation code sent')
    email.value = ''
  } catch (error) {
    ElMessage.error('Failed to send, please try again later')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.manager-page-content {
  background: #fff;
  padding: 24px;
  border-radius: 8px;
  min-height: 300px;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}

.invitation-container {
  width: 100%;
  max-width: 600px;
  text-align: center;
}

.invitation-container h2 {
  margin-bottom: 32px;
  color: #303133;
  font-size: 28px;
}

.invitation-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.email-input {
  width: 100%;
}

.send-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
}

.user-type-group {
  width: 100%;
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 10px;
}
</style>

<style>
html, body {
  overflow: hidden;
}
</style> 