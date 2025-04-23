<template>
  <div class="home-page">
    <Header class="header" />
    <div class="main-content">
      <Sidebar class="sidebar" />
      <div class="content">
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

          <div class="pagination-container">
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :page-sizes="[5, 10, 20, 50]"
              :total="total"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import Header from '@/components/Header.vue'
import Sidebar from '@/components/Sidebar.vue'
import request from '@/utils/request'

const searchQuery = ref('')
const projects = ref([])
const currentPage = ref(1)
const pageSize = ref(5)
const total = ref(0)

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
    const res = await request.get(`/projects/all?page=${currentPage.value}&size=${pageSize.value}`)
    if (res.code === 1) {
      projects.value = res.data.records
      total.value = res.data.total
    }
  } catch (error) {
    console.error('Failed to fetch projects:', error)
    // 使用示例数据作为后备方案
    projects.value = mockProjects
    total.value = mockProjects.length
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
  // 重置到第一页
  currentPage.value = 1
  fetchProjects()
}

// 处理页码改变
const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchProjects()
}

// 处理每页条数改变
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  fetchProjects()
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
  // 可以在这里添加全局的错误处理
  window.addEventListener('unhandledrejection', (event) => {
    ElMessage.error('An error occurred. Please try again later.')
    console.error('Unhandled promise rejection:', event.reason)
  })
})
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
  min-height: calc(100vh - 60px);
}

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

.pagination-container {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}
</style>