<template>
  <div class="create-project-container">
    <div class="header">
      <router-link to="/dashboard" class="back-link">
        <el-icon><ArrowLeft /></el-icon>
        <span>Back</span>
      </router-link>
      <h1>Create New Project</h1>
    </div>

    <div class="form-container">
      <div class="form-group">
        <label>Project Name</label>
        <el-input 
          v-model="projectName" 
          placeholder="Enter project name"
        />
      </div>

      <div class="form-group">
        <label>Area</label>
        <el-input 
          v-model="area" 
          placeholder="Enter project area"
        />
      </div>

      <div class="form-group">
        <label>Subject</label>
        <el-input 
          v-model="subject" 
          placeholder="Enter project subject"
        />
      </div>

      <div class="form-group">
        <label>Status</label>
        <el-select v-model="status" placeholder="Select status" class="status-select">
          <el-option label="Planned" value="planned" />
          <el-option label="In Progress" value="in-progress" />
          <el-option label="Completed" value="completed" />
        </el-select>
      </div>

      <div class="form-group">
        <label>Project Image</label>
        <el-upload
          class="image-upload"
          action="#"
          :auto-upload="false"
          :show-file-list="false"
          :on-change="handleImageChange"
        >
          <el-button type="primary" class="upload-btn">
            <el-icon><Upload /></el-icon>
            <span>Select Image</span>
          </el-button>
        </el-upload>
      </div>

      <el-button type="primary" class="submit-btn" @click="createProject">
        Create Project
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ArrowLeft, Upload } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const projectName = ref('')
const area = ref('')
const subject = ref('')
const status = ref('planned')
const projectImage = ref(null)

const handleImageChange = (file) => {
  projectImage.value = file.raw
}

const createProject = async () => {
  // 处理项目创建逻辑
  console.log('Creating project...')
}
</script>

<style scoped>
.create-project-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
  min-height: 100vh;
  background-color: #f5f7fa;
}

.header {
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
}

.form-group {
  margin-bottom: 1.5rem;
}

label {
  display: block;
  color: #5e6d82;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.el-input {
  width: 100%;
}

:deep(.el-input__wrapper) {
  background-color: #f5f7fa;
  border-radius: 6px;
  border: 1px solid #e4e7ed;
}

:deep(.el-input__wrapper:hover) {
  border-color: #409eff;
}

.status-select {
  width: 100%;
}

.image-upload {
  display: block;
}

.upload-btn {
  width: 100%;
  border-radius: 6px;
  background-color: #f5f7fa;
  border: 1px dashed #d9d9d9;
  color: #5e6d82;
}

.upload-btn:hover {
  border-color: #409eff;
  color: #409eff;
}

.upload-btn .el-icon {
  margin-right: 0.5rem;
}

.submit-btn {
  width: 100%;
  margin-top: 2rem;
  height: 40px;
  background-color: #409eff;
  border: none;
  border-radius: 6px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.submit-btn:hover {
  background-color: #66b1ff;
  transform: translateY(-1px);
}

@media (max-width: 768px) {
  .create-project-container {
    padding: 1rem;
  }

  .form-container {
    padding: 1.5rem;
  }
}
</style> 