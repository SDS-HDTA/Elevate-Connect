<template>
  <div class="content">
    <div class="join-project-container">
      <div class="form-header">
        <router-link to="/my-projects" class="back-link">
          <el-icon>
            <ArrowLeft />
          </el-icon>
          <span>Back</span>
        </router-link>
        <h1>Join Project</h1>
      </div>

      <div class="form-container">
        <div class="search-section">
            <el-input v-model="searchQuery"
              placeholder="Enter project name..."
              style="width: 300px; margin-right: 10px;" @keyup.enter="handleSearch" clearable />
            <el-button type="primary" @click="handleSearch">Search</el-button>
        </div>

        <div class="projects-grid" v-if="availableProjects.length > 0">
          <el-card v-for="project in availableProjects" :key="project.id" class="project-card">
            <div class="project-header">
              <h2>{{ project.name }}</h2>
              <el-tag :type="getStatusType(project.status)">{{ getStatusText(project.status) }}</el-tag>
            </div>

            <div class="project-info">
              <p><strong>Area:</strong> {{ project.area }}</p>
              <p><strong>Category:</strong> {{ project.category }}</p>
              <p><strong>Description:</strong> {{ project.description }}</p>
            </div>

            <div class="project-actions">
              <el-button type="success" @click="handleJoinProject(project.id)">Join Project</el-button>
            </div>
          </el-card>
        </div>

        <div class="no-results" v-else-if="hasSearched">
          <el-empty description="No projects found" />
        </div>

        <div class="join-button-container">
          <el-button type="success" :disabled="availableProjects.length === 0" @click="handleJoinSelectedProject">
            Join Selected Project
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ArrowLeft } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
const searchQuery = ref('')
const availableProjects = ref([])
const hasSearched = ref(false)

const fetchAvailableProjects = async () => {
  try {
    const res = await request.get('/projects/available', {
      params: {
        searchQuery: searchQuery.value
      }
    })
    if (res.code === 1) {
      availableProjects.value = res.data
      hasSearched.value = true
    }
  } catch (error) {
    console.error('Failed to fetch available projects:', error)
    ElMessage.error('Failed to load projects')
  }
}

const handleSearch = () => {
  if (!searchQuery.value) {
    ElMessage.warning('Please enter search content')
    return
  }
  fetchAvailableProjects()
}

const handleJoinProject = async (projectId) => {
  try {
    const userId = localStorage.getItem('userId')
    const res = await request.post('/projects/join', {
      projectId,
      userId
    })
    if (res.code === 1) {
      ElMessage.success('Successfully joined the project')
      router.push('/my-projects')
    }
  } catch (error) {
    console.error('Failed to join project:', error)
    ElMessage.error('Failed to join project')
  }
}

const handleJoinSelectedProject = () => {
  if (availableProjects.value.length === 1) {
    handleJoinProject(availableProjects.value[0].id)
  } else {
    ElMessage.warning('Please select a project first')
  }
}

const getStatusType = (status) => {
  const types = {
    0: 'info',     // Empathise
    1: 'warning',  // Discover
    2: 'success',  // Define
    3: 'primary',  // Ideate
    4: 'danger',   // Prototype
    5: 'success'   // Feedback
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    0: 'Empathise',
    1: 'Discover',
    2: 'Define',
    3: 'Ideate',
    4: 'Prototype',
    5: 'Feedback'
  }
  return texts[status] || 'Unknown'
}
</script>

<style scoped>
.content {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  min-height: 100vh;
  padding: 40px 20px;
  background-color: #f5f7fa;
  margin: 0 auto;
}

.join-project-container {
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
}

.form-header {
  margin-bottom: 2rem;
}

.back-link {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #5e6d82;
  font-size: 0.9rem;
  margin-bottom: 1rem;
  transition: color 0.3s ease;
}

.back-link:hover {
  color: #409eff;
}

.back-link .el-icon {
  margin-right: 0.5rem;
}

h1 {
  color: #2c3e50;
  font-size: 1.8rem;
  font-weight: 600;
  margin: 0;
}

.form-container {
  background-color: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  margin-top: 1rem;
}

.search-section {
  margin-bottom: 2rem;
  display: flex;
  justify-content: center;
}


.projects-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 2rem;
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

.project-actions {
  display: flex;
  justify-content: flex-end;
}

.no-results {
  margin: 2rem 0;
  text-align: center;
}

.join-button-container {
  display: flex;
  justify-content: center;
  margin-top: 2rem;
}

.join-button-container .el-button {
  min-width: 200px;
}

@media (max-width: 768px) {
  .join-project-container {
    padding: 1rem;
  }

  .form-container {
    padding: 1.5rem;
  }

  .search-header {
    flex-direction: column;
    gap: 10px;
  }

  .search-header .el-select,
  .search-header .el-input {
    width: 100% !important;
    margin-right: 0 !important;
  }
}
</style>