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
              placeholder="搜索..."
              :prefix-icon="Search"
              clearable
            />
          </div>
          <div class="section-action">
            <el-button type="primary" :icon="Plus" @click="handleNewClick(index)">新建</el-button>
          </div>
        </div>
        <div class="section-content">
          <el-card 
            v-for="file in section.files" 
            :key="file.id"
            class="file-card"
            shadow="hover"
            @click="handleFileClick(file)"
          >
            <div class="file-content">
              <el-icon class="file-icon">
                <component :is="getFileIcon(file.type)" />
              </el-icon>
              <span class="file-name">{{ file.name }}</span>
            </div>
          </el-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeft, Search, Plus, Document, DataBoard, Picture, VideoPlay } from '@element-plus/icons-vue'
import { miroApi } from '@/utils/mirorequest'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const route = useRoute()
const isAuthenticated = ref(false)

const statusId = ref(route.params.statusId || '')
const iterationId = ref(route.params.iterationId || '')

const sections = ref([
  { title: 'Page', searchText: '', files: [] },
  { title: 'Whiteboard', searchText: '', files: [] },
  { title: 'Picture', searchText: '', files: [] },
  { title: 'Video', searchText: '', files: [] }
])

// 模拟文件数据
const mockFiles = [
  { id: 1, name: '设计文档', type: 0 ,source:'boardId:1234567890'},
  { id: 2, name: '产品规划', type: 0 ,source:'boardId:1234567890'},
  { id: 3, name: '白板会议记录', type: 1 ,source:'boardId:1234567890'},
  { id: 4, name: '用户画像', type: 1 ,source:'boardId:1234567890'},
  { id: 5, name: '产品截图1', type: 2 ,source:'boardId:1234567890'},
  { id: 6, name: '产品截图2', type: 2 ,source:'boardId:1234567890'},
  { id: 7, name: '产品演示视频', type: 3 ,source:'boardId:1234567890'},
  { id: 8, name: '用户反馈视频', type: 3 ,source:'boardId:1234567890'}
]

// 获取所有文件
const fetchAllFiles = async () => {
  try {
    // 这里应该是实际的API调用
    // const response = await miroApi.getAllFiles(statusId.value, iterationId.value)
    
    // 使用模拟数据
    const files = mockFiles
    
    // 根据type将文件分类到对应的section
    sections.value.forEach((section, index) => {
      section.files = files.filter(file => file.type === index)
    })
  } catch (error) {
    console.error('获取文件失败:', error)
    ElMessage.error('获取文件失败')
  }
}

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

// 处理文件点击事件
const handleFileClick = (file) => {
  console.log('点击文件:', file)
  // TODO: 实现文件点击后的处理逻辑
}

// 根据文件类型获取对应的图标
const getFileIcon = (type) => {
  const icons = {
    0: Document,
    1: DataBoard,
    2: Picture,
    3: VideoPlay
  }
  return icons[type] || Document
}

// 处理新建按钮点击
const handleNewClick = (sectionIndex) => {
  switch (sectionIndex) {
    case 0: // Page
      handleNewPage()
      break
    case 1: // Whiteboard
      handleNewWhiteboard()
      break
    case 2: // Picture
      handleNewPicture()
      break
    case 3: // Video
      handleNewVideo()
      break
  }
}

// 新建Page
const handleNewPage = () => {
  console.log('新建Page')
  // TODO: 实现新建Page的逻辑
}

// 新建Whiteboard
const handleNewWhiteboard = async () => {
  try {
    // 显示输入弹窗
    const { value: whiteboardName } = await ElMessageBox.prompt('请输入白板名称', '新建白板', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPattern: /\S+/,
      inputErrorMessage: '白板名称不能为空'
    })

    if (whiteboardName) {
      // 调用Miro API创建白板
      const miroResponse = await miroApi.post('/boards', {
        name: whiteboardName
      })

      if (miroResponse && miroResponse.id) {
        // 调用后端API保存白板信息
        const result = await request.post('/whiteboards', {
          name: whiteboardName,
          boardId: miroResponse.id,
          userId: localStorage.getItem('userId'),
          statusId: statusId.value,
          iterationId: iterationId.value
        })

        if (result.code === 1) {
          // 在前端添加新的白板卡片
          const newWhiteboard = {
            id: result.data.id,
            name: whiteboardName,
            type: 1, // Whiteboard类型
            source: miroResponse.id
          }
          
          // 添加到对应的section中
          sections.value[1].files.push(newWhiteboard)
          
          ElMessage.success('白板创建成功')
        } else {
          throw new Error(result.message || '保存白板信息失败')
        }
      } else {
        throw new Error('创建Miro白板失败')
      }
    }
  } catch (error) {
    console.error('创建白板失败:', error)
    ElMessage.error(error.message || '创建白板失败')
  }
}

// 新建Picture
const handleNewPicture = () => {
  console.log('新建Picture')
  // TODO: 实现新建Picture的逻辑
}

// 新建Video
const handleNewVideo = () => {
  console.log('新建Video')
  // TODO: 实现新建Video的逻辑
}

onMounted(() => {
  checkAuthStatus()
  fetchAllFiles()
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
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
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

.file-card {
  width: 200px;
  cursor: pointer;
  transition: all 0.3s ease;
  border-radius: 8px;
  overflow: hidden;
}

.file-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.file-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  padding: 10px;
}

.file-icon {
  font-size: 32px;
  color: #409EFF;
}

.file-name {
  font-size: 14px;
  color: #606266;
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 100%;
}
</style> 