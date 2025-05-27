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
              <p>需要先进行 Miro 认证</p>
              <el-button type="primary" @click="handleMiroAuth">进行认证</el-button>
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
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'

import { miroApi } from '@/utils/mirorequest'

const router = useRouter()
const route = useRoute()
const boardId = ref('')
const isAuthenticated = ref(false)

// 检查认证状态
const checkAuthStatus = () => {
  const accessToken = localStorage.getItem('miro_access_token')
  isAuthenticated.value = !!accessToken
}

// 处理 Miro 认证
const handleMiroAuth = () => {
  const authUrl = miroApi.getAuthUrl()
  window.location.href = authUrl
}

// 处理认证回调
const handleAuthCallback = async () => {
  const code = route.query.code
  if (code) {
    try {
      await miroApi.getInitialAccessToken(code)
      isAuthenticated.value = true
      // 清除 URL 中的 code 参数
      router.replace({ path: route.path })
      // 获取 boards
      await getMiroBoards()
    } catch (error) {
      console.error('认证失败:', error)
    }
  }
}

// 获取 Miro boards
const getMiroBoards = async () => {
  try {
    const data = await miroApi.getBoards()
    console.log('Miro Boards:', data)
    if (data && data.length > 0) {
      boardId.value = data[0].id
    }
  } catch (error) {
    console.error('获取 Miro boards 失败:', error)
    if (error.response && error.response.status === 401) {
      isAuthenticated.value = false
    }
  }
}

onMounted(async () => {
  checkAuthStatus()
  if (isAuthenticated.value) {
    await getMiroBoards()
  } else {
    // 检查是否有认证回调
    await handleAuthCallback()
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