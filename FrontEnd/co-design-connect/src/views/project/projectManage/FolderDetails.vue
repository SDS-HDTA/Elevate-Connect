<template>
  <div class="folder-container">
    <div class="header">
      <el-button 
        class="back-button" 
        @click="handleBack"
        :icon="ArrowLeft"
        text
      >
        <span class="title-text">Status-{{ statusId }} Iteration-{{ iterationId }}</span>
      </el-button>
    </div>
    <div class="content">
      <div class="section" v-for="(section, index) in sections" :key="section.title" :class="`section-${index % 4}`">
        <div class="section-header">
          <div class="section-title">
            <span>{{ section.title }}</span>
          </div>
          <div class="section-search">
            <el-input
              v-model="section.searchText"
              placeholder="Search..."
              :prefix-icon="Search"
              clearable
            />
          </div>
          <div class="section-action">
            <el-button type="primary" :icon="Plus">New</el-button>
          </div>
        </div>
        <div class="section-content">
          <!-- Content area to be implemented -->
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeft, Search, Plus } from '@element-plus/icons-vue'
import { miroApi } from '@/utils/mirorequest'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const isAuthenticated = ref(false)

const statusId = ref(route.params.statusId || '')
const iterationId = ref(route.params.iterationId || '')

const sections = ref([
  { title: 'Page', searchText: '' },
  { title: 'Whiteboard', searchText: '' },
  { title: 'Map', searchText: '' },
  { title: 'Picture', searchText: '' },
  { title: 'Video', searchText: '' }
])

// Check authentication status
const checkAuthStatus = async () => {
  try {
    const success = await miroApi.getMiroTokens()
    isAuthenticated.value = success
    if (!success) {
      ElMessage.error('Failed to get Miro authentication')
    }
  } catch (error) {
    console.error('Authentication check failed:', error)
    ElMessage.error('Authentication check failed')
  }
}

// Go back to previous page
const handleBack = () => {
  miroApi.clearMiroTokens()
  router.back()
}

onMounted(() => {
  checkAuthStatus()
})

onBeforeUnmount(() => {
  miroApi.clearMiroTokens()
})
</script>

<style scoped>
.folder-container {
  height: 100%;
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

.back-button:hover .title-text {
  color: #409EFF;
}

.back-button :deep(.el-icon) {
  font-size: 20px;
  color: #606266;
  transition: color 0.3s ease;
}

.title-text {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  transition: color 0.3s ease;
}

.content {
  flex: 1;
  padding: 20px;
  background-color: #f5f7fa;
  display: flex;
  flex-direction: column;
  gap: 20px;
  overflow-y: auto;
}

.section {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.section:nth-child(1) {
  background-color: #f0f9ff;
}

.section:nth-child(2) {
  background-color: #f0f9eb;
}

.section:nth-child(3) {
  background-color: #fdf6ec;
}

.section:nth-child(4) {
  background-color: #fef0f0;
}

.section:nth-child(5) {
  background-color: #f9f0ff;
}

.section:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.1);
}

.section-header {
  padding: 16px 20px;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  align-items: center;
  gap: 20px;
  border-radius: 8px 8px 0 0;
  position: relative;
}

.section-header::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 4px;
  background-color: #409EFF;
  border-radius: 4px 0 0 4px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  min-width: 120px;
  display: flex;
  align-items: center;
  gap: 8px;
  padding-left: 12px;
}

.section-title span {
  font-weight: 600;
}

.section-search {
  flex: 1;
  max-width: 300px;
}

.section-action {
  margin-left: auto;
}

.section-content {
  padding: 20px;
  min-height: 200px;
}

/* Custom scrollbar styles */
.content::-webkit-scrollbar {
  width: 6px;
}

.content::-webkit-scrollbar-thumb {
  background-color: #909399;
  border-radius: 3px;
}

.content::-webkit-scrollbar-track {
  background-color: #f5f7fa;
}
</style> 