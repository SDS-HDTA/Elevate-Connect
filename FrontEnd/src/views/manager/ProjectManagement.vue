<template>
  <div class="project-management">
    <div class="mb-3 w-100 flex justify-content-end">
      <el-button class="btn-icon-primary" @click="fetchCommunities()"
        ><el-icon><Plus class="me-1" /></el-icon>Create Project</el-button
      >
    </div>
    <el-table v-loading="loading" :data="projects" style="width: 100%" border>
      <el-table-column prop="id" label="ID" width="80" sortable />
      <el-table-column prop="name" label="Project Name" />
      <el-table-column prop="communityName" label="Community Name" />
      <el-table-column prop="category" label="Category" sortable>
        <template #default="{ row }">
          {{ projectCategories[row.category] || 'Unknown' }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="Stage">
        <template #default="{ row }">
          <el-tag :type="getStageType(row.currentStage)">{{
            getProjectStageText(row.currentStage)
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="Create Time">
        <template #default="{ row }">
          {{ new Date(row.createTime).toLocaleString() }}
        </template>
      </el-table-column>
      <!-- <el-table-column width="100">
        <template #default="{ row }">
          TODO: Edit project functionality
          <el-tooltip content="Edit Project" placement="top">
            <el-button class="btn-icon-info">
              <el-icon><Edit /></el-icon>
            </el-button>
          </el-tooltip>
          TODO: implement delete project functionality
          <el-tooltip content="Delete Project" placement="top">
            <el-button class="btn-icon-danger" @click="handleDelete(row)">
              <el-icon><Delete /></el-icon>
            </el-button>
          </el-tooltip>
        </template>
      </el-table-column> -->
    </el-table>
  </div>

  <!-- TODO: implement editing project once requirements are defined -->
  <CreateProject
    :projects="projects"
    :communities="[]"
    :model-value="addDialogVisible"
    @update:model-value="(val) => (addDialogVisible = val)"
    @submit="fetchProjects"
  />
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import request from '@/utils/request';
import { Plus, Delete, Edit } from '@element-plus/icons-vue';
import { getStageType, getProjectStageText } from '@/utils/projectStageHelper';
import CreateProject from '../dialogs/CreateProject.vue';
import { projectCategories } from '@/utils/projectCategoryHelper';

const projects = ref([]);
const communities = ref([]);
const loading = ref(false);
const addDialogVisible = ref(false);

const fetchProjects = async () => {
  loading.value = true;
  try {
    const response = await request.get('/manager/projects', {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });
    if (response.code === 1) {
      projects.value = response.data.map((data) => ({
        ...data.project,
        communityName: data.community.name,
      }));
    } else {
      ElMessage.error('Failed to get project list: ' + response.message);
    }
  } catch (error) {
    ElMessage.error(
      'Failed to get project list: ' +
        (error.response?.data?.message || error.message)
    );
  } finally {
    loading.value = false;
  }
};

const fetchCommunities = async () => {
  loading.value = true;
  try {
    const response = await request.get('/community', {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });
    if (response.code === 1) {
      communities.value = response.data;
      addDialogVisible.value = true;
    } else {
      ElMessage.error('An error occurred: ' + response.message);
    }
  } catch (error) {
    ElMessage.error('An error occurred: ' + error.message);
  } finally {
    loading.value = false;
  }
};

// Get project list when component is mounted
onMounted(() => {
  fetchProjects();
});
</script>

<style scoped>
.project-management {
  padding: 0 1rem 1rem 1rem;
}

.image-upload {
  display: block;
  max-width: fit-content;
}

.upload-btn {
  width: 100%;
  border-radius: 6px;
  border: 1px dashed #d9d9d9 !important;
}

.upload-btn:hover {
  border-color: #138366;
  color: #138366;
}

.upload-btn .el-icon {
  margin-right: 0.5rem;
}
</style>
