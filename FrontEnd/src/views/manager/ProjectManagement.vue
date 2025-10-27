<template>
  <div class="project-management">
    <div class="mb-3 mt-2 w-100 flex justify-content-between">
      <el-input
        v-model="searchQuery"
        placeholder="Search by name or community"
        clearable
        style="width: 300px"
      />
      <el-button class="btn-icon-primary" @click="addDialogVisible = true"
        ><el-icon><Plus class="me-1" /></el-icon>Create Project</el-button
      >
    </div>
    <el-table
      height="calc(100vh - 234px)"
      v-loading="loading"
      :data="filteredProjects"
      style="width: 100%"
      border
    >
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
      :before-close="handleEditClose"
      title="Edit Project"
      v-model="editDialogVisible"
      class="custom-dialog"
      max-width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        v-loading="saving"
        :rules="rules"
        ref="editFormRef"
        :model="editForm"
        label-width="120px"
      >
        <el-form-item label="Project Name" prop="name">
          <el-input v-model="editForm.name" placeholder="Enter project name" />
        </el-form-item>

        <el-form-item label="Category" prop="category">
          <el-select v-model="editForm.category" placeholder="Select category">
            <el-option
              v-for="(label, value) in projectCategories"
              :key="value"
              :label="label"
              :value="value"
            />
          </el-select>
        </el-form-item>

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
            :file-list="uploadFiles"
            :limit="1"
            :on-exceed="handleExceed"
            :on-change="handleImageChange"
          >
            <el-button class="btn-secondary">
              <el-icon><Plus /></el-icon>
              <span>Select Image</span>
            </el-button>
            <template #tip>
              <div class="el-upload__tip">
                Only one image can be uploaded. Recommended dimensions: 700x400
              </div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button class="btn-secondary" @click="handleEditClose"
          >Cancel</el-button
        >
        <el-button class="btn-primary" :loading="saving" @click="saveEdit"
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
import { ref, onMounted, computed } from 'vue';
import { ElMessage } from 'element-plus';
import request from '@/utils/request';
import { Plus, Edit, Remove } from '@element-plus/icons-vue';
import { getStageType, getProjectStageText } from '@/utils/projectStageHelper';
import CreateProject from '../dialogs/CreateProject.vue';
import { projectCategories } from '@/utils/projectCategoryHelper';
import AddUserToProject from '../dialogs/AddUserToProject.vue';
import { disablePastDates } from '@/utils/disablePastDates';
import { getLegibleFileSize } from '@/utils/imageHelper';

const projects = ref([]);
const communities = ref([]);
const users = ref([]);
const loading = ref(false);
const addDialogVisible = ref(false);
const addUserDialogVisible = ref(false);
const selectedProjectId = ref(null);
const editDialogVisible = ref(false);
const saving = ref(false);
const uploadRef = ref(null);
const imagePreview = ref(null);
const editFormRef = ref(null);
const uploadFiles = ref([]);
const editForm = ref({
  id: null,
  name: '',
  category: null,
  description: '',
  deadline: '',
  image: null,
});
const searchQuery = ref('');

const filteredProjects = computed(() => {
  if (!searchQuery.value.trim()) return projects.value;

  const query = searchQuery.value.toLowerCase();
  return projects.value.filter((project) => {
    const name = project.name.toLowerCase();
    const community = project.communityName.toLowerCase();
    return name.includes(query) || community.includes(query);
  });
});

const rules = {
  name: [{ required: true, message: 'Required field', trigger: 'blur' }],
  description: [{ required: true, message: 'Required field', trigger: 'blur' }],
  category: [{ required: true, message: 'Required field', trigger: 'blur' }],
  deadline: [{ required: true, message: 'Required field', trigger: 'change' }],
  image: [{ required: true, message: 'Required field', trigger: 'change' }],
};

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
        imageSrc: data.projectImageSrc,
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
    category: String(project.category), // v-for expects keys as a string
    description: project.description,
    deadline: project.targetDate,
    image: project.imageSrc,
  };

  imagePreview.value = project.imageSrc || null;
  editDialogVisible.value = true;
};

const saveEdit = async () => {
  if (!editFormRef.value) return;

  const validForm = await editFormRef.value.validate();
  if (!validForm) return;

  saving.value = true;

  try {
    const formData = new FormData();
    formData.append('name', editForm.value.name);
    formData.append('description', editForm.value.description);
    formData.append('category', editForm.value.category);
    formData.append('targetDate', editForm.value.deadline);

    // it can be both string (url) and file
    if (editForm.value.image && editForm.value.image instanceof File) {
      formData.append('projectImage', editForm.value.image);
    }

    const res = await request({
      url: `/projects/${editForm.value.id}`,
      method: 'put',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    if (res.code === 1) {
      ElMessage.success('Project updated successfully');

      // Clear uploaded files
      if (uploadRef.value) {
        uploadRef.value.clearFiles();
      }

      handleEditClose();
      await fetchProjects();
    } else {
      ElMessage.error(res.message || 'Project update failed');
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

const handleExceed = () => {
  ElMessage.warning('Only one picture can be uploaded');
};

const handleImageChange = (file) => {
  if (!file.raw.type.startsWith('image/')) {
    ElMessage.error('Only image files are allowed!');
    editForm.value.image = null;
    uploadFiles.value = [];
    return;
  }

  if (file.size > MAX_FILE_SIZE_BYTES) {
    ElMessage.error(
      `File is too large, the max size is ${getLegibleFileSize()}MB`
    );
    editForm.value.image = null;
    uploadFiles.value = [];
    return;
  }

  const rawFile = file.raw;
  editForm.value.image = rawFile;
  imagePreview.value = URL.createObjectURL(rawFile);

  // Manually trigger validation for the image field
  if (editFormRef.value) {
    editFormRef.value.validateField('image');
  }
};

// Get project list when component is mounted
onMounted(() => {
  loading.value = true;
  fetchUsers();
  fetchCommunities();
  fetchProjects(); // fetchProjects sets loading to false when done
});

function handleEditClose() {
  editDialogVisible.value = false;
  removeImage();

  if (uploadRef.value) {
    uploadRef.value.clearFiles();
  }

  editFormRef.value.resetFields();
}

function removeImage() {
  URL.revokeObjectURL(imagePreview.value);
  imagePreview.value = null;
  editForm.value.image = null;
}
</script>

<style scoped>
.project-management {
  overflow-x: hidden;
  padding: 0 1rem 1rem 1rem;
}
</style>
