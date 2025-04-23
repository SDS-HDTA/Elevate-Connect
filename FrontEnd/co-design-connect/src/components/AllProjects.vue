<template>
  <div class="project-list-vertical">
    <div class="search-container">
      <el-input
        v-model="searchQuery"
        placeholder="Search projects..."
        :prefix-icon="Search"
        @input="handleSearch"
        clearable
        class="custom-search"
      />
    </div>
    
    <div class="projects-list">
      <el-card v-for="project in filteredProjects" :key="project.id" class="project-card">
        <div class="project-content">
          <div class="project-info">
            <div class="project-header">
              <h2>{{ project.title }}</h2>
              <el-tag :type="getStateType(project.state)">{{ project.state }}</el-tag>
            </div>
            <div class="project-details">
              <p><strong>Area:</strong> {{ project.area }}</p>
              <p><strong>Subject:</strong> {{ project.catagory }}</p>
            </div>
          </div>
          <div class="project-image" v-if="project.image">
            <el-image
              :src="project.image"
              fit="cover"
              :preview-src-list="[project.image]"
            />
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import request from '@/utils/request'

const searchQuery = ref('')
const projects = ref([])

// 示例数据
const mockProjects = [
  {
    id: '1',
    title: 'Emergency Shelter and Relief Distribution for Flood Victims',
    state: 'Completed',
    area: 'Assam, India',
    subject: 'Disaster Relief / Shelter and Basic Needs',
    image: '/images/project1.jpg'
  },
  {
    id: '2',
    title: 'Mobile Health Clinics for Displaced Communities',
    state: 'Ongoing',
    area: 'Gaziantep, Türkiye',
    subject: 'Healthcare Access / Conflict Response',
    image: '/images/project2.jpg'
  },
  {
    id: '3',
    title: 'School Meals and Nutrition Program',
    state: 'Planned',
    area: 'Tigray, Ethiopia',
    subject: 'Food Security / Child Welfare',
    image: '/images/project3.jpg'
  }
]

// 获取项目列表
const fetchProjects = async () => {
  try {
    const res = await request.get('/projects')
    if (res.code === 200) {
      projects.value = res.data
    }
  } catch (error) {
    console.error('Failed to fetch projects:', error)
    // 使用示例数据作为后备方案
    projects.value = mockProjects
  }
}

// 根据搜索关键词过滤项目
const filteredProjects = computed(() => {
  if (!searchQuery.value) return projects.value
  
  const query = searchQuery.value.toLowerCase()
  return projects.value.filter(project => 
    project.title.toLowerCase().includes(query) ||
    project.area.toLowerCase().includes(query) ||
    project.subject.toLowerCase().includes(query)
  )
})

// 处理搜索输入
const handleSearch = () => {
  // 可以在这里添加防抖处理
}

// 获取状态对应的标签类型
const getStateType = (state) => {
  const types = {
    'Completed': 'success',
    'Ongoing': 'warning',
    'Planned': 'info'
  }
  return types[state] || 'info'
}

onMounted(() => {
  fetchProjects()
})
</script>

<style scoped>
.project-list-vertical {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.search-container {
  margin-bottom: 30px;
}

.custom-search :deep(.el-input__wrapper) {
  border-radius: 25px;
  background-color: #fff;
}

.custom-search :deep(.el-input__inner) {
  height: 45px;
}

.custom-search :deep(.el-input__inner::placeholder) {
  color: #999;
}

.custom-search :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #e74c3c inset;
}

.projects-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.project-card {
  transition: transform 0.3s;
}

.project-card:hover {
  transform: translateX(5px);
}

.project-content {
  display: flex;
  gap: 20px;
}

.project-info {
  flex: 1;
}

.project-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.project-header h2 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

.project-details p {
  margin: 8px 0;
  color: #666;
}

.project-image {
  width: 300px;
  height: 200px;
  overflow: hidden;
  border-radius: 8px;
}

.project-image .el-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style> 