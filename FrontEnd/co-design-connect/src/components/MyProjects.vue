<template>
  <div class="project-container">
    <div v-if="!showNewProject" class="project-list">
      <div class="header-container">
        <h2>My Projects</h2>
        <el-button type="primary" @click="showNewProject = true">Create Project</el-button>
      </div>

      <div class="search-container">
        <el-input
          v-model="searchQuery"
          placeholder="Search projects..."
          :prefix-icon="Search"
          @input="handleSearch"
          clearable
        />
      </div>
      
      <div class="projects-grid">
        <el-card v-for="project in filteredProjects" :key="project.id" class="project-card">
          <div class="project-header">
            <h2>{{ project.title }}</h2>
            <el-tag :type="getStateType(project.state)">{{ project.state }}</el-tag>
          </div>
          
          <div class="project-info">
            <p><strong>Area:</strong> {{ project.area }}</p>
            <p><strong>Subject:</strong> {{ project.catagory }}</p>
          </div>
          
          <div class="project-image" v-if="project.image">
            <el-image
              :src="project.image"
              fit="cover"
              :preview-src-list="[project.image]"
            />
          </div>
        </el-card>
      </div>
    </div>

    <CreateProject
      v-else
      @back="showNewProject = false"
      @created="handleProjectCreated"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import request from '@/utils/request'
import CreateProject from './CreateProject.vue'

const searchQuery = ref('')
const projects = ref([])
const showNewProject = ref(false)

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
    const res = await request.get('/projects/my')
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

const handleProjectCreated = async () => {
  showNewProject.value = false
  await fetchProjects()
}

onMounted(() => {
  fetchProjects()
})
</script>

<style scoped>
.project-container {
  height: 100%;
  padding: 20px;
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-container h2 {
  margin: 0;
}

.search-container {
  max-width: 600px;
  margin: 0 auto 30px;
}

.projects-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.project-card {
  transition: transform 0.3s;
}

.project-card:hover {
  transform: translateY(-5px);
}

.project-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.project-header h2 {
  margin: 0;
  font-size: 18px;
}

.project-info {
  margin-bottom: 15px;
}

.project-info p {
  margin: 5px 0;
}

.project-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
  border-radius: 4px;
}

.project-image .el-image {
  width: 100%;
  height: 100%;
}
</style> 