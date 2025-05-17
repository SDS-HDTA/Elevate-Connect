<template>
  <div class="project-detail">
    <!-- 左侧项目信息 -->
    <div class="left-panel">
      <div class="back-button" @click="$router.push('/my-projects')">
        <el-icon><ArrowLeft /></el-icon>
        <span>Back</span>
      </div>
      <div class="project-header">
        <h1>{{ project.name }}</h1>
        <el-tag :type="getStateType(project.status)" size="large">
          {{ getStateText(project.status) }}
        </el-tag>
      </div>

      <div class="project-image" v-if="project.imageUrl">
        <el-image
          :src="project.imageUrl"
          fit="fill"
        />
      </div>
      <div v-else class="project-image-placeholder">
        <el-empty
          description="No image"
          :image-size="100"
        >
          <template #image>
            <el-icon :size="60" style="color: #909399;"><Picture /></el-icon>
          </template>
        </el-empty>
      </div>

      <div class="project-info">
        <div class="info-item">
          <h3>Project Owner</h3>
          <p>{{ project.person_in_charge }}</p>
        </div>
        <div class="info-item">
          <h3>Start Date</h3>
          <p>{{ project.start_date }}</p>
        </div>
        <div class="info-item">
          <h3>Area</h3>
          <p>{{ project.area }}</p>
        </div>
        <div class="info-item">
          <h3>Category</h3>
          <p>{{ project.category }}</p>
        </div>
        <div class="info-item">
          <h3>Description</h3>
          <p>{{ project.description }}</p>
        </div>
      </div>
    </div>

    <!-- 右侧功能区 -->
    <div class="right-panel">
      <div class="nav-links">
        <router-link to="channel" class="nav-link">Channel</router-link>
        <router-link to="files" class="nav-link">Files</router-link>
        <router-link to="backlog" class="nav-link">Backlog</router-link>
        <router-link to="timeline" class="nav-link">WorkPiece</router-link>
        <router-link to="member" class="nav-link">Member</router-link>
      </div>
      
      <!-- 预留的内容区域 -->
      <div class="content-area">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft, Picture } from '@element-plus/icons-vue'
import request from '@/utils/request'

const route = useRoute()
const project = ref({})

// 获取项目详情
const fetchProjectDetail = async () => {
  try {
    const projectId = route.params.id
    const res = await request.get(`/project/${projectId}`)
    if (res.code === 1) {
      project.value = res.data
    }
  } catch (error) {
    console.error('Failed to fetch project details:', error)
    // 使用示例数据
    project.value = {
      id: '3',
      name: 'School Meals and Nutrition Program',
      status: 0,
      area: 'Tigray, Ethiopia',
      category: 'Food Security / Child Welfare',
      image_url: '/images/project3.jpg',
      person_in_charge: 'Bing Xia',
      start_date: '2024-06-01',
      description: 'The aim is to provide school-aged children with nutritious meals, often free or at a reduced cost, to improve their health and well-being.'
    }
  }
}

// 获取状态对应的标签类型
const getStateType = (status) => {
  const types = {
    0: 'info',    // Planned
    1: 'warning', // Ongoing
    2: 'success'  // Completed
  }
  return types[status] || 'info'
}

// 获取状态显示文本
const getStateText = (status) => {
  const texts = {
    0: 'Planned',
    1: 'Ongoing',
    2: 'Completed'
  }
  return texts[status] || 'Unknown'
}

onMounted(() => {
  fetchProjectDetail()
})
</script>

<style scoped>
.project-detail {
  display: flex;
  width: 100%;
  height: 100%;
  background-color: #fff;
}

.left-panel {
  flex: 0 0 300px;
  padding: 20px;
  border-right: 1px solid #e4e7ed;
}

.project-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e4e7ed;
}

.project-header h1 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

.project-image {
  width: 100%;
  height: 200px;
  margin-bottom: 20px;
  border-radius: 4px;
  overflow: hidden;
}

.project-image .el-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.project-image-placeholder {
  width: 100%;
  height: 200px;
  margin-bottom: 20px;
  border-radius: 4px;
  background-color: #f5f7fa;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px dashed #dcdfe6;
}

.project-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-item h3 {
  margin: 0 0 4px 0;
  font-size: 14px;
  color: #909399;
}

.info-item p {
  margin: 0;
  color: #333;
  line-height: 1.5;
}

.right-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.nav-links {
  display: flex;
  align-items: center;
  padding: 0 20px;
  height: 50px;
  border-bottom: 1px solid #e4e7ed;
}

.nav-link {
  padding: 0 20px;
  line-height: 50px;
  color: #606266;
  text-decoration: none;
  transition: all 0.3s;
  position: relative;
}

.nav-link:hover {
  color: #409eff;
}

.nav-link.router-link-active {
  color: #409eff;
}

.nav-link.router-link-active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 20px;
  right: 20px;
  height: 2px;
  background-color: #409eff;
}

.content-area {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.back-button {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #909399;
  cursor: pointer;
  padding: 8px 0;
  margin-bottom: 16px;
  transition: color 0.3s;
}

.back-button:hover {
  color: #409eff;
}

.back-button .el-icon {
  font-size: 16px;
}
</style> 