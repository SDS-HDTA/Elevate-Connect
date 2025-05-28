<template>
  <div class="miro-board-container">
    <div class="header">
      <el-button 
        class="back-button" 
        @click="handleBack"
        :icon="ArrowLeft"
        text
      >
        返回
      </el-button>
    </div>
    <div class="content">
      <div v-if="loading" class="loading-container">
        <el-spinner />
        <span>加载中...</span>
      </div>
      <iframe
        v-else
        :src="boardUrl"
        frameborder="0"
        allowfullscreen
        class="miro-frame"
      ></iframe>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const boardId = ref(route.params.boardId)
const loading = ref(true)
const boardUrl = ref('')

// 返回上一页
const handleBack = () => {
  router.back()
}

// 初始化白板
const initBoard = async () => {
  try {
    loading.value = true

    // 构建带有编辑权限的白板URL
    boardUrl.value = `https://miro.com/app/board/${boardId.value}`
  } catch (error) {
    console.error('初始化白板失败:', error)
    ElMessage.error('初始化白板失败，请重试')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  initBoard()
})
</script>

<style scoped>
.miro-board-container {
  height: 100vh;
  width: 100%;
  display: flex;
  flex-direction: column;
}

.header {
  padding: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.back-button {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 0;
  height: auto;
  transition: all 0.3s ease;
}

.back-button:hover {
  transform: translateX(-4px);
}

.back-button:hover :deep(.el-icon) {
  color: #409EFF;
}

.content {
  flex: 1;
  position: relative;
  background-color: #f5f7fa;
}

.miro-frame {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border: none;
}

.loading-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16px;
  background-color: #f5f7fa;
}

.loading-container span {
  color: #606266;
  font-size: 14px;
}
</style> 