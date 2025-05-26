<template>
  <div class="workpiece-container">
    <div class="header">
      <div class="search-section">
        <el-input
          v-model="searchQuery"
          placeholder="搜索文件夹"
          prefix-icon="Search"
          class="search-input"
        />
        <el-button
          class="add-folder-btn"
          type="primary"
          circle
          @click="dialogVisible = true"
        >
          <el-icon><Plus /></el-icon>
        </el-button>
      </div>
    </div>
    
    <div class="folder-list">
      <el-row :gutter="20">
        <el-col :span="6" v-for="folder in filteredFolders" :key="folder.id">
          <el-card class="folder-card" shadow="hover">
            <div class="folder-content" @click="openFolder(folder)">
              <el-icon class="folder-icon"><Folder /></el-icon>
              <div class="folder-name">{{ folder.name }}</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <el-dialog
      v-model="dialogVisible"
      title="新建文件夹"
      width="30%"
    >
      <el-form :model="newFolder" label-width="80px">
        <el-form-item label="文件夹名称">
          <el-input v-model="newFolder.name" placeholder="请输入文件夹名称" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="createFolder">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Folder, Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const searchQuery = ref('')
const dialogVisible = ref(false)
const folders = ref([
  { id: 1, name: '项目文档' },
  { id: 2, name: '设计资源' },
  { id: 3, name: '会议记录' }
])

const newFolder = ref({
  name: ''
})

const filteredFolders = computed(() => {
  return folders.value.filter(folder => 
    folder.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
})

const openFolder = (folder) => {
  // TODO: 实现打开文件夹的逻辑
  console.log('打开文件夹:', folder)
}

const createFolder = () => {
  if (!newFolder.value.name.trim()) {
    ElMessage.warning('请输入文件夹名称')
    return
  }
  
  const newId = folders.value.length + 1
  folders.value.push({
    id: newId,
    name: newFolder.value.name
  })
  
  newFolder.value.name = ''
  dialogVisible.value = false
  ElMessage.success('文件夹创建成功')
}
</script>

<style scoped>
.workpiece-container {
  padding: 20px;
  min-height: 100vh;
  position: relative;
}

.header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 30px;
}

.search-section {
  display: flex;
  align-items: center;
  gap: 10px;
}

.search-input {
  width: 300px;
}

.title {
  color: #606266;
  font-size: 20px;
}

.folder-list {
  margin-top: 20px;
}

.folder-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s;
}

.folder-card:hover {
  transform: translateY(-5px);
}

.folder-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 0;
}

.folder-icon {
  font-size: 48px;
  color: #409EFF;
  margin-bottom: 10px;
}

.folder-name {
  font-size: 16px;
  color: #606266;
  text-align: center;
  word-break: break-all;
}

.add-folder-btn {
  width: 40px;
  height: 40px;
  font-size: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style> 