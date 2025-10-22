<template>
  <div class="project-management">
    <div class="mb-3 w-100 flex justify-content-end">
      <el-button class="btn-icon-primary" @click="addDialogVisible = true"
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
      <el-table-column width="70">
        <template #default="{ row }">
          <el-tooltip content="Add Users to Project" placement="top">
            <el-button
              class="btn-icon-info"
              @click="
                ((addUserDialogVisible = true), (selectedProjectId = row.id))
              "
            >
              <el-icon><Plus /></el-icon>
            </el-button>
          </el-tooltip>
          <el-tooltip content="Edit Project" placement="top">
            <el-button class="btn-icon-info" @click="openEditDialog(row)">
              <el-icon><Edit /></el-icon>
            </el-button>
          </el-tooltip>
          <!-- TODO: implement delete project functionality
          <el-tooltip content="Delete Project" placement="top">
            <el-button class="btn-icon-danger" @click="handleDelete(row)">
              <el-icon><Delete /></el-icon>
            </el-button>
          </el-tooltip> -->
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      title="Edit Project"
      v-model="editDialogVisible"
      class="custom-dialog"
      max-width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="editForm" label-width="130px">
        <el-form-item label="Project Name">
          <el-input v-model="editForm.name" placeholder="Enter project name" />
        </el-form-item>

        <el-form-item label="Community">
          <el-select
            v-model="editForm.communityId"
            placeholder="Select community"
          >
            <el-option
              v-for="community in communities"
              :key="community.id"
              :label="community.name"
              :value="community.id"
            />
          </el-select>
          <el-tip v-if="editForm.communityId" class="tip"
            >Country:
            {{
              communities.find((c) => c.id === editForm.communityId)?.country
            }}</el-tip
          >
        </el-form-item>

        <el-form-item label="Category">
          <el-select v-model="editForm.category" placeholder="Select category">
            <el-option
              v-for="(label, key) in projectCategories"
              :key="key"
              :label="label"
              :value="key"
            />
          </el-select>
        </el-form-item>
      </el-form>

      <el-form-item label="Description" prop="description">
        <el-input
          type="textarea"
          v-model="editForm.description"
          :maxlength="150"
          placeholder="Briefly describe the project (max 150 characters)"
          resize="vertical"
        />
      </el-form-item>

      <el-form-item label="Target Date" prop="deadline">
        <el-date-picker
          v-model="editForm.deadline"
          type="date"
          placeholder="Select target date"
          format="DD-MM-YYYY"
          value-format="YYYY-MM-DD"
          :disabled-date="disablePastDates"
        />
      </el-form-item>

      <el-form-item label="Image" prop="image">
        <div v-if="imagePreview" class="flex flex-column align-items-end">
          <el-button class="btn-icon-danger" @click="removeImage()"
            ><el-icon class="me-1"><Remove /></el-icon>Remove Image</el-button
          >
          <img class="w-100" :src="imagePreview" alt="Project Image" />
        </div>
        <el-upload
          v-else
          ref="uploadRef"
          :auto-upload="false"
          :show-file-list="true"
          :limit="1"
          :on-exceed="handleExceed"
          :on-change="handleImageChange"
        >
          <el-button class="btn-secondary">
            <el-icon><Upload /></el-icon>
            <span>Select Image</span>
          </el-button>
          <template #tip>
            <div class="el-upload__tip">Only one image can be uploaded</div>
          </template>
        </el-upload>
      </el-form-item>

      <template #footer>
        <el-button @click="editDialogVisible = false">Cancel</el-button>
        <el-button type="primary" :loading="saving" @click="saveEdit"
          >Save</el-button
        >
      </template>
    </el-dialog>
  </div>

  <CreateProject
    :projects="projects"
    :communities="communities"
    :model-value="addDialogVisible"
    @update:model-value="(val) => (addDialogVisible = val)"
    @submit="fetchProjects"
  />

  <AddUserToProject
    :model-value="addUserDialogVisible"
    :project-id="selectedProjectId"
    :users="users"
    @update:model-value="(val) => (addUserDialogVisible = val)"
    @submit="
      fetchProjects();
      selectedProjectId = null;
    "
  />
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import request from '@/utils/request';
import { Plus, Edit, Upload } from '@element-plus/icons-vue';
import { getStageType, getProjectStageText } from '@/utils/projectStageHelper';
import CreateProject from '../dialogs/CreateProject.vue';
import { projectCategories } from '@/utils/projectCategoryHelper';
import AddUserToProject from '../dialogs/AddUserToProject.vue';
import { disablePastDates } from '@/utils/disablePastDates';

const projects = ref([]);
const communities = ref([]);
const users = ref([]);
const loading = ref(false);
const addDialogVisible = ref(false);
const addUserDialogVisible = ref(false);
const selectedProjectId = ref(null);
const editDialogVisible = ref(false);
const saving = ref(false);
const editForm = ref({
  id: null,
  name: '',
  communityId: '',
  category: '',
  description: '',
  deadline: '',
  image: null,
});

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
  try {
    const response = await request.get('/community', {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });
    if (response.code === 1) {
      communities.value = response.data;
    } else {
      ElMessage.error('An error occurred: ' + response.message);
    }
  } catch (error) {
    ElMessage.error('An error occurred: ' + error.message);
  }
};

const fetchUsers = async () => {
  try {
    const response = await request.get('/manager/users', {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });
    if (response.code === 1) {
      users.value = response.data;
    } else {
      ElMessage.error('An error occurred: ' + response.message);
    }
  } catch (error) {
    ElMessage.error('An error occurred: ' + error.message);
  }
};

const openEditDialog = (project) => {
  editForm.value = {
    id: project.id,
    name: project.name,
    communityId: project.communityId,
    category: project.category,
    description: project.description,
    deadline: project.targetDate,
    image: project.image,
  };
  editDialogVisible.value = true;
};

const saveEdit = async () => {
  if (!editForm.value.name || !editForm.value.communityId) {
    ElMessage.warning('Please fill in all required fields.');
    return;
  }
  saving.value = true;
  try {
    const response = await request.put(
      `/projects/${editForm.value.id}`,
      editForm.value,
      {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      }
    );
    if (response.code === 1) {
      ElMessage.success('Project updated successfully.');
      editDialogVisible.value = false;
      await fetchProjects();
    } else {
      ElMessage.error('Failed to update project: ' + response.message);
    }
  } catch (error) {
    ElMessage.error(
      'Failed to update project: ' +
        (error.response?.data?.message || error.message)
    );
  } finally {
    saving.value = false;
  }
};

// Get project list when component is mounted
onMounted(() => {
  loading.value = true;
  fetchUsers();
  fetchCommunities();
  fetchProjects(); // fetchProjects sets loading to false when done
});
</script>

<style scoped>
.project-management {
  overflow-x: hidden;
  padding: 0 1rem 1rem 1rem;
}
</style>
