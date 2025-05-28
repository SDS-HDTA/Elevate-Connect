<template>
  <div class="folder-container">
    <div class="header">
      <el-button 
        class="back-button" 
        @click="handleBack"
        :icon="ArrowLeft"
        text
      >
        <span class="title-text">Status-{{ statusMap[projectStatus] || projectStatus }} Iteration-{{ iterationId }}</span>
      </el-button>
    </div>
    <div class="content">
      <div class="section" v-for="(section, index) in filteredSections" :key="section.title" :class="`section-${index % 4}`">
        <div class="section-header">
          <div class="section-title">
            <span>{{ section.title }}</span>
          </div>
          <div class="section-search">
            <el-input
              v-model="searchTexts[index]"
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
            v-for="file in section.filteredFiles" 
            :key="file.id"
            class="file-card"
            :class="{ 'highlighted': file.isHighlighted }"
            shadow="hover"
            @click="handleFileClick(file)"
          >
            <div class="file-content">
              <el-icon class="file-icon">
                <component :is="getFileIcon(file.type)" />
              </el-icon>
              <span class="file-name">{{ file.name }}</span>
            </div>
            <div class="delete-button" @click.stop="handleDeleteFile(file)">
              <el-icon><Close /></el-icon>
            </div>
          </el-card>
        </div>
      </div>
    </div>
    <!-- 文件上传对话框 -->
    <el-dialog
      v-model="fileDialogVisible"
      :title="dialogTitle"
      width="500px"
      :close-on-click-modal="false"
      @close="handleFileDialogClose"
    >
      <div class="file-upload-form">
        <div class="form-item">
          <label>文件名称</label>
          <el-input v-model="fileForm.name" placeholder="请输入文件名称" />
        </div>
        <div class="form-item">
          <label>选择文件</label>
          <el-upload
            class="file-uploader"
            :auto-upload="false"
            :show-file-list="true"
            :limit="1"
            :accept="fileAccept"
            :on-change="handleFileChange"
            :on-remove="handleFileRemove"
            :file-list="fileList"
          >
            <el-button type="primary" :disabled="!!fileForm.file">选择文件</el-button>
          </el-upload>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleFileDialogClose">取消</el-button>
          <el-button type="primary" @click="handleFileSubmit" :loading="uploading" :disabled="!fileForm.name || !fileForm.file">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeft, Search, Plus, Document, DataBoard, Picture, VideoPlay, Close } from '@element-plus/icons-vue'
import { miroApi } from '@/utils/mirorequest'
import { docApi } from '@/utils/docrequest'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const route = useRoute()
const isAuthenticated = ref(false)

const projectStatus = ref(route.params.statusId || '')
const iterationId = ref(route.params.iterationId || '')
const projectId = ref(route.params.projectId || '')

// status映射
const statusMap = {
  0: 'Empathise',
  1: 'Discover',
  2: 'Define',
  3: 'Ideate',
  4: 'Prototype',
  5: 'Feedback'
}

const sections = ref([
  { title: 'Page', searchText: '', files: [] },
  { title: 'Whiteboard', searchText: '', files: [] },
  { title: 'Picture', searchText: '', files: [] },
  { title: 'Video', searchText: '', files: [] }
])

// 添加搜索文本的响应式引用
const searchTexts = ref(sections.value.map(() => ''))

// 修改过滤后的文件计算属性
const filteredSections = computed(() => {
  // 遍历每个部分（Page、Whiteboard、Picture、Video）
  return sections.value.map((section, index) => {
    // 获取当前部分的搜索文本并转为小写
    const searchText = searchTexts.value[index].toLowerCase()
    
    // 如果没有搜索文本，返回原始文件列表
    if (!searchText) {
      return {
        ...section,
        filteredFiles: section.files,  // 返回原始文件列表
        hasHighlight: false           // 没有高亮
      }
    }
    
    // 如果有搜索文本，处理文件列表
    const filteredFiles = section.files.map(file => ({
      ...file,
      isHighlighted: file.name.toLowerCase().includes(searchText)  // 标记是否高亮
    }))
    
    return {
      ...section,
      filteredFiles,  // 返回处理后的文件列表
      hasHighlight: filteredFiles.some(file => file.isHighlighted)  // 是否有高亮文件
    }
  })
})

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
const files = ref([])

// 获取所有文件
const fetchAllFiles = async () => {
  try {
    const res = await request.get(`/projects/${projectId.value}/files`,{
      params: {
        projectStatus: projectStatus.value,
        iterationId: iterationId.value
      }
    })
    if (res.code === 1) {
      files.value = res.data
      sections.value.forEach((section, index) => {
        section.files = files.value.filter(file => file.type === index)
      })
    } else {
      files.value = mockFiles
      sections.value.forEach((section, index) => {
        section.files = files.value.filter(file => file.type === index)
      })
      throw new Error(res.message || '获取文件失败')
    }
  } catch (error) {
    console.error('获取文件失败:', error)
    ElMessage.error('获取文件失败')
  }
}

// Check authentication status
const checkAuthStatus = async () => {
  try {
    const [miroSuccess, docSuccess] = await Promise.all([
      miroApi.getMiroTokens(),
      docApi.getDocTokens()
    ])
    isAuthenticated.value = miroSuccess && docSuccess
    if (!miroSuccess || !docSuccess) {
      ElMessage.error('Failed to get authentication')
    }
  } catch (error) {
    console.error('Authentication check failed:', error)
    ElMessage.error('Authentication check failed')
  }
}

// Go back to previous page
const handleBack = () => {
  miroApi.clearMiroTokens()
  docApi.clearDocTokens()
  router.back()
}

// 处理文件点击事件
const handleFileClick = (file) => {
  if (file.type === 0) { // Page类型
    // 从source中提取docId
    const docId = file.source
    // 跳转到Google Docs编辑页面
    window.open(`https://docs.google.com/document/d/${docId}/edit`, '_blank')
  } else if (file.type === 1) { // Whiteboard类型
    // 从source中提取boardId
    const boardId = file.source.split(':')[1]
    // 跳转到白板页面
    router.push({
      name: 'miro-board',
      params: { boardId }
    })
  } else if (file.type === 2) { // Picture类型
    // 直接在新标签页打开图片
    window.open(file.source, '_blank')
  } else if (file.type === 3) { // Video类型
    // 直接在新标签页打开视频
    window.open(file.source, '_blank')
  }
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
const handleNewPage = async () => {
  try {
    // 显示输入弹窗
    const { value: docName } = await ElMessageBox.prompt('请输入文档名称', '新建文档', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPattern: /\S+/,
      inputErrorMessage: '文档名称不能为空'
    })

    if (docName) {
      // 调用Google Docs API创建文档
      const docResponse = await docApi.createDoc({
        name: docName
      })

      if (docResponse && docResponse.id) {
        // 调用后端API保存文档信息
        const result = await request.post('/projects/files/documents', {
          name: docName,
          source: docResponse.id,
          userId: localStorage.getItem('userId'),
          projectStatus: projectStatus.value,
          iterationId: iterationId.value,
          projectId: projectId.value,
          type: 0
        })

        if (result.code === 1) {
          // 在前端添加新的文档卡片
          const newDoc = {
            id: result.data.id,
            name: docName,
            type: 0, // Page类型
            source: docResponse.id,
            creator: result.data.creator,
            creatorId: result.data.creatorId,
            projectStatus: result.data.projectStatus,
            iterationId: result.data.iterationId,
            projectId: result.data.projectId
          }
          
          // 添加到对应的section中
          sections.value[0].files.push(newDoc)
          
          ElMessage.success('文档创建成功')
        } else {
          throw new Error(result.message || '保存文档信息失败')
        }
      } else {
        throw new Error('创建Google文档失败')
      }
    }
  } catch (error) {
    console.error('创建文档失败:', error)
    ElMessage.error(error.message || '创建文档失败')
  }
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
        const result = await request.post('/projects/files/whiteboard', {
          name: whiteboardName,
          source: miroResponse.id,
          userId: localStorage.getItem('userId'),
          projectStatus: projectStatus.value,
          iterationId: iterationId.value,
          projectId: projectId.value,
          type: 1
        })

        if (result.code === 1) {
          // 在前端添加新的白板卡片
          const newWhiteboard = {
            id: result.data.id,
            name: whiteboardName,
            type: 1, // Whiteboard类型
            source: miroResponse.id,
            creator: result.data.creator,
            creatorId: result.data.creatorId,
            projectStatus: result.data.projectStatus,
            iterationId: result.data.iterationId,
            projectId: result.data.projectId
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

// 文件上传相关
const fileDialogVisible = ref(false)
const fileForm = ref({
  name: '',
  file: null,
  type: null
})
const fileList = ref([])
const uploading = ref(false)

// 计算属性：对话框标题
const dialogTitle = computed(() => {
  return fileForm.value.type === 2 ? '新建图片' : '新建视频'
})

// 计算属性：文件接受类型
const fileAccept = computed(() => {
  return fileForm.value.type === 2 ? 'image/*' : 'video/*'
})

// 处理文件选择
const handleFileChange = (file) => {
  fileForm.value.file = file.raw
  fileList.value = [file]
}

// 处理文件移除
const handleFileRemove = () => {
  fileForm.value.file = null
  fileList.value = []
}

// 处理对话框关闭
const handleFileDialogClose = () => {
  fileForm.value = {
    name: '',
    file: null,
    type: null
  }
  fileList.value = []
  fileDialogVisible.value = false
}

// 处理文件提交
const handleFileSubmit = async () => {
  if (!fileForm.value.name) {
    ElMessage.warning('请输入文件名称')
    return
  }
  if (!fileForm.value.file) {
    ElMessage.warning('请选择文件')
    return
  }

  try {
    uploading.value = true
    // 创建FormData对象用于文件上传
    const uploadData = new FormData()
    uploadData.append('name', fileForm.value.name)
    uploadData.append('source', null)
    uploadData.append('userId', localStorage.getItem('userId'))
    uploadData.append('projectStatus', projectStatus.value)
    uploadData.append('iterationId', iterationId.value)
    uploadData.append('type', fileForm.value.type)
    uploadData.append('projectId', projectId.value)
    uploadData.append('multipartFile', fileForm.value.file)

    // 调用后端API上传文件
    const result = await request.post(`/projects/files/${fileForm.value.type === 2 ? 'pictures' : 'videos'}`, uploadData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    if (result.code === 1) {
      // 在前端添加新的文件卡片
      const newFile = {
        id: result.data.id,
        name: result.data.name,
        type: fileForm.value.type,
        creator: result.data.creator,
        creatorId: result.data.creatorId,
        projectStatus: result.data.projectStatus,
        iterationId: result.data.iterationId,
        projectId: result.data.projectId,
        source: result.data.source
      }
      
      // 添加到对应的section中
      sections.value[fileForm.value.type].files.push(newFile)
      
      ElMessage.success(`${fileForm.value.type === 2 ? '图片' : '视频'}上传成功`)
      handleFileDialogClose()
    } else {
      throw new Error(result.message || '上传失败')
    }
  } catch (error) {
    console.error('上传失败:', error)
    ElMessage.error(error.message || '上传失败')
  } finally {
    uploading.value = false
  }
}

// 新建Picture
const handleNewPicture = () => {
  fileForm.value.type = 2
  fileDialogVisible.value = true
}

// 新建Video
const handleNewVideo = () => {
  fileForm.value.type = 3
  fileDialogVisible.value = true
}

// 检查删除权限
const checkDeletePermission = (file) => {
  const userId = localStorage.getItem('userId')
  const isProjectOwner = localStorage.getItem(`project_${route.params.projectId}_creatorId`) === userId
  const isFileCreator = file.creatorId === userId

  if (!isProjectOwner && !isFileCreator) {
    ElMessage.error('您没有权限删除此文件')
    return false
  }
  return true
}

// 显示删除确认弹框
const showDeleteConfirm = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要删除此文件吗？删除后无法恢复。',
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    return true
  } catch (error) {
    return false
  }
}

// 从列表中移除文件
const removeFileFromList = (file) => {
  const sectionIndex = file.type
  sections.value[sectionIndex].files = sections.value[sectionIndex].files.filter(f => f.id !== file.id)
}

// 删除文件的基础方法
const deleteFileBase = async (file) => {
  try {
    const result = await request.delete(`/projects/files/${file.id}`)
    if (result.code === 1) {
      removeFileFromList(file)
      ElMessage.success('文件删除成功')
      return true
    } else {
      throw new Error(result.message || '删除失败')
    }
  } catch (error) {
    console.error('删除文件失败:', error)
    ElMessage.error(error.message || '删除文件失败')
    return false
  }
}

// 删除Google Docs文档
const deleteGoogleDoc = async (file) => {
  try {
    // 调用Google Docs API删除文档
    await docApi.deleteDoc(file.source)
    // 删除成功后，删除数据库记录
    return await deleteFileBase(file)
  } catch (error) {
    console.error('删除Google Docs文档失败:', error)
    ElMessage.error('删除Google Docs文档失败')
    return false
  }
}

// 删除Miro白板
const deleteMiroWhiteboard = async (file) => {
  try {
    // 从source中提取boardId
    const boardId = file.source.split(':')[1]
    // 调用Miro API删除白板
    await miroApi.delete(`/boards/${boardId}`)
    // 删除成功后，删除数据库记录
    return await deleteFileBase(file)
  } catch (error) {
    console.error('删除Miro白板失败:', error)
    ElMessage.error('删除Miro白板失败')
    return false
  }
}

// 处理文件删除
const handleDeleteFile = async (file) => {
  // 阻止事件冒泡
  event.stopPropagation()
  
  try {
    // 检查权限
    if (!checkDeletePermission(file)) {
      return
    }

    // 显示确认弹框
    if (!await showDeleteConfirm()) {
      return
    }

    // 根据文件类型调用不同的删除方法
    switch (file.type) {
      case 0: // Page
        await deleteGoogleDoc(file)
        break
      case 1: // Whiteboard
        await deleteMiroWhiteboard(file)
        break
      case 2: // Picture
        await deleteFileBase(file)
        break
      case 3: // Video
        await deleteFileBase(file)
        break
    }
  } catch (error) {
    console.error('删除文件失败:', error)
    ElMessage.error(error.message || '删除文件失败')
  }
}

onMounted(async () => {
  checkAuthStatus()
  fetchAllFiles()
})

onBeforeUnmount(() => {
  miroApi.clearMiroTokens()
  docApi.clearDocTokens()
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
  position: relative;
}

.file-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.file-card:hover .delete-button {
  opacity: 1;
}

.delete-button {
  position: absolute;
  top: 8px;
  right: 8px;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: #f56c6c;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  opacity: 0;
  transition: all 0.3s ease;
  z-index: 1;
}

.delete-button:hover {
  transform: scale(1.1);
  background-color: #e64242;
}

.delete-button .el-icon {
  font-size: 14px;
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

.file-upload-form {
  padding: 20px;
}

.file-uploader {
  width: 100%;
}

.file-card.highlighted {
  background-color: #fff7e6;
  border: 2px solid #ffa940;
  animation: highlight-pulse 1.5s infinite;
}

@keyframes highlight-pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(255, 169, 64, 0.4);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(255, 169, 64, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(255, 169, 64, 0);
  }
}
</style> 