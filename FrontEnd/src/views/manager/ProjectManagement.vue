<template>
  <div class="project-management">
    <div class="mb-3 w-100 flex justify-content-end">
      <el-button
        class="btn-primary"
        @click="$router.push('/manager/create-project')"
        >Create Project</el-button
      >
    </div>
    <el-table v-loading="loading" :data="projects" style="width: 100%" border>
      <el-table-column prop="id" label="ID" width="80" sortable />
      <el-table-column prop="name" label="Project Name" />
      <el-table-column prop="category" label="Category" sortable />
      <el-table-column prop="creator" label="Creator" />
      <el-table-column prop="status" label="Status">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">{{
            getStatusText(row.status)
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="Create Time">
        <template #default="{ row }">
          {{ new Date(row.createTime).toLocaleString() }}
        </template>
      </el-table-column>
      <el-table-column width="100">
        <template #default="{ row }">
          <el-tooltip content="Delete Project" placement="top">
            <el-button class="btn-icon-danger" @click="handleDelete(row)">
              <el-icon><Delete /></el-icon>
            </el-button>
          </el-tooltip>
          <el-tooltip content="Edit Project" placement="top">
            <el-button class="btn-icon-primary">
              <el-icon><Edit /></el-icon>
            </el-button>
          </el-tooltip>
          <el-tooltip content="Add Users to Project" placement="top">
            <el-button class="btn-icon-primary">
              <el-icon><Plus /></el-icon>
            </el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import request from '@/utils/request';
import { Plus, Delete, Edit } from '@element-plus/icons-vue';

const projects = ref([]);
const loading = ref(false);

// Get all projects
const fetchProjects = async () => {
  loading.value = true;
  try {
    const params = new URLSearchParams();
    params.append('userId', localStorage.getItem('userId'));
    const response = await request.get('/manager/projects', {
      params: params,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });
    if (response.code === 1) {
      projects.value = response.data.map((data) => ({
        ...data.project,
        creator: data.creatorName,
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

// Status mapping
const statusMap = {
  0: 'Empathise',
  1: 'Discover',
  2: 'Define',
  3: 'Ideate',
  4: 'Prototype',
  5: 'Feedback',
};

// Return different tag types based on status
const getStatusType = (status) => {
  const typeMap = {
    0: 'info',
    1: 'primary',
    2: 'success',
    3: 'warning',
    4: 'danger',
    5: 'success',
  };
  return typeMap[status] || 'info';
};

// Get status display text
const getStatusText = (status) => {
  return statusMap[status] || 'Unknown status';
};

// Delete project
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `Are you sure you want to delete the project "${row.name}"?`,
    'Warning',
    {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  )
    .then(async () => {
      try {
        const res = await request.delete(`/projects/${row.id}/dismiss`, {
          params: {
            userId: localStorage.getItem('userId'),
          },
        });
        if (res.code === 1) {
          ElMessage.success('Delete successfully');
          // Refresh project list
          projects.value = projects.value.filter(
            (project) => project.id !== row.id
          );
        } else {
          ElMessage.error('Delete failed: ' + res.message);
        }
      } catch (error) {
        ElMessage.error(
          'Delete failed: ' + (error.response?.data?.message || error.message)
        );
      }
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: 'Delete cancelled',
      });
    });
};

// Get project list when component is mounted
onMounted(() => {
  fetchProjects();
});
</script>

<style scoped>
.project-management {
  padding: 1rem;
}
</style>
