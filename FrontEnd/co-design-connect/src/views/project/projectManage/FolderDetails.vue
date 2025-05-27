<template>
  

        <div class="folder-container">
          <div class="back-section">
            <el-button 
              class="back-button" 
              @click="handleBack"
              :icon="ArrowLeft"
            >
              返回
            </el-button>
          </div>
          <div class="folder-content">
            <div v-if="!isAuthenticated" class="auth-message">
              <p>Miro 认证未初始化</p>
              <el-button type="primary" @click="handleInitTokens">初始化 Token</el-button>
            </div>
            <iframe
              v-else-if="boardId"
              :src="`https://miro.com/app/board/${boardId}/`"
              class="miro-board"
              frameborder="0"
              allowfullscreen
            ></iframe>
          </div>
        </div>

</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { miroApi } from '@/utils/mirorequest'
import { ElMessage } from 'element-plus'

const router = useRouter()
const boardId = ref('')
const isAuthenticated = ref(false)

// 检查认证状态
const checkAuthStatus = () => {
  isAuthenticated.value = miroApi.hasMiroTokens()
  console.log(isAuthenticated.value)
}

// 初始化 Token
const handleInitTokens = () => {
  // 这里填入您的 access token 和 refresh token
  const accessToken = 'eyJtaXJvLm9yaWdpbiI6ImV1MDEifQ_vNB5XrQB-_Uw1kv9md8S_tOHi8s'
  const refreshToken = 'eyJtaXJvLm9yaWdpbiI6ImV1MDEifQ_dskiu7KSDZfAh3oCjqPwVqbWTTU'
  
  try {
    miroApi.initMiroTokens(accessToken, refreshToken)
    isAuthenticated.value = true
    ElMessage.success('Token 初始化成功')
    getMiroBoards()
  } catch (error) {
    console.error('Token 初始化失败:', error)
    ElMessage.error('Token 初始化失败')
  }
}

// 获取 Miro boards
const getMiroBoards = async () => {
  try {
    const res = await miroApi.getBoards()
    const data = res.data
    console.log('Miro Boards:', data)
    boardId.value = data[0].id
    console.log(data[0].id)
    console.log(boardId.value)
  } catch (error) {
    console.error('获取 Miro boards 失败:', error)
    if (error.response && error.response.status === 401) {
      isAuthenticated.value = false
      ElMessage.error('Token 已失效，请重新初始化')
    }
  }
}

onMounted(() => {
  checkAuthStatus()
  if (isAuthenticated.value) {
    getMiroBoards()
  }
})

// 返回上一页
const handleBack = () => {
  router.back()
}
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
}

.main-content {
  display: flex;
  flex-direction: row;
  margin-top: 60px;
  flex: 1;
}

.sidebar {
  position: fixed;
  left: 0;
  top: 60px;
  bottom: 0;
}

.content {
  flex: 1;
  margin-left: 200px;
  background-color: #f5f7fa;
  height: calc(100vh - 60px);
}

.folder-container {
  height: 100%;
  width: 100%;
  margin: 0 auto;
  padding: 20px;
}

.back-section {
  margin-bottom: 20px;
}

.back-button {
  display: flex;
  align-items: center;
  gap: 5px;
}

.folder-content {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  min-height: calc(100% - 80px);
}

.miro-board {
  width: 100%;
  height: 100%;
  min-height: 600px;
  border: none;
}

.auth-message {
  text-align: center;
  padding: 40px;
}

.auth-message p {
  margin-bottom: 20px;
  font-size: 16px;
  color: #606266;
}
</style> 