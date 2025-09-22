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
      <el-table-column prop="category" label="Category" sortable />
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
      <el-table-column width="100">
        <template #default="{ row }">
          <el-tooltip content="Edit Project" placement="top">
            <el-button class="btn-icon-info">
              <el-icon><Edit /></el-icon>
            </el-button>
          </el-tooltip>
          <el-tooltip content="Delete Project" placement="top">
            <el-button class="btn-icon-danger" @click="handleDelete(row)">
              <el-icon><Delete /></el-icon>
            </el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <!-- TODO: implement editing project once requirements are defined -->
  <el-dialog
    title="Create New Project"
    v-model="addDialogVisible"
    width="500px"
    :before-close="handleAddDialogClose"
  >
    <el-form
      ref="addFormRef"
      :model="addForm"
      :rules="addRules"
      v-loading="addDialogLoading"
      label-width="120px"
    >
      <el-form-item label="Name" prop="name">
        <el-input v-model="addForm.name" />
      </el-form-item>

      <el-form-item label="Area" prop="area">
        <el-input v-model="addForm.area" />
      </el-form-item>

      <el-form-item label="Category" prop="category">
        <el-input v-model="addForm.category" />
        <!-- TODO: Include helper category function getProjectCategoryText here -->
      </el-form-item>

      <el-form-item label="Description" prop="description">
        <el-input
          type="textarea"
          v-model="addForm.description"
          :maxlength="150"
          placeholder="Briefly describe project (max 150 characters)"
          resize="vertical"
        />
      </el-form-item>

      <el-form-item label="Status" prop="status">
        <el-select v-model="addForm.status" placeholder="Select status">
          <el-option label="Empathise" :value="0" />
          <el-option label="Discover" :value="1" />
          <el-option label="Define" :value="2" />
          <el-option label="Ideate" :value="3" />
          <el-option label="Prototype" :value="4" />
          <el-option label="Completed" :value="5" />
        </el-select>
      </el-form-item>

      <el-form-item label="Target Date" prop="deadline">
        <el-date-picker
          v-model="addForm.deadline"
          type="date"
          placeholder="Select target date"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>

      <el-form-item label="Image" prop="image">
        <el-upload
          ref="uploadRef"
          class="image-upload"
          :auto-upload="false"
          :show-file-list="true"
          :limit="1"
          :on-exceed="handleExceed"
          :on-change="handleImageChange"
        >
          <el-button class="upload-btn btn-secondary">
            <el-icon><Upload /></el-icon>
            <span>Select Image</span>
          </el-button>
          <template #tip>
            <div class="el-upload__tip">Only one image can be uploaded</div>
          </template>
        </el-upload>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button class="btn-secondary" @click="handleAddDialogClose">
        Cancel
      </el-button>
      <el-button class="btn-primary" @click="submitAdd">Add</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import request from '@/utils/request';
import { Plus, Upload, Delete, Edit } from '@element-plus/icons-vue';
import { getStageType, getProjectStageText } from '@/utils/projectStageHelper';
import { useUserStore } from '@/stores/userStore';

const projects = ref([]);
const loading = ref(false);
const addDialogVisible = ref(false);
const addDialogLoading = ref(false);
const userStore = useUserStore();
const addFormRef = ref(null);
const uploadRef = ref(null);
const addForm = ref({
  name: '',
  area: '',
  category: '',
  description: '',
  status: null,
  deadline: '',
  image: null,
});
const userId = computed(() => userStore.userInfo?.id || null);

const handleExceed = () => {
  ElMessage.warning('Only one picture can be uploaded');
};

const addRules = {
  name: [{ required: true, message: 'Required field', trigger: 'blur' }],
  area: [{ required: true, message: 'Required field', trigger: 'blur' }],
  description: [{ required: true, message: 'Required field', trigger: 'blur' }],
  category: [{ required: true, message: 'Required field', trigger: 'blur' }],
  status: [{ required: true, message: 'Required field', trigger: 'change' }],
  deadline: [{ required: true, message: 'Required field', trigger: 'change' }],
  image: [{ required: true, message: 'Required field', trigger: 'change' }],
};

const handleAddDialogClose = (done) => {
  if (addFormRef.value) addFormRef.value.resetFields();

  // Clear uploaded files
  if (uploadRef.value) {
    uploadRef.value.clearFiles();
  }

  addDialogVisible.value = false;
  done();
};

const handleImageChange = (file, fileList) => {
  // Only allow image files
  const validFiles = fileList.filter((f) => f.raw.type.startsWith('image/'));

  if (validFiles.length < fileList.length) {
    ElMessage.error('Only image files are allowed!');
  }

  // Update the form model
  addForm.value.image = validFiles[0]?.raw || null;

  // Update the upload component's file list to remove invalid files
  fileList.splice(0, fileList.length, ...validFiles);
};

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

// Delete project
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `Are you sure you want to delete the project "${row.name}"? This action cannot be undone.`,
    'Confirm',
    {
      confirmButtonClass: 'btn-danger',
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
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

const submitAdd = async () => {
  if (!addFormRef.value) return;

  const validForm = await addFormRef.value.validate();
  if (!validForm) return;

  try {
    addDialogLoading.value = true;

    // TODO: Change this to interact with a new community dropdown
    const requestData = {
      name: addForm.value.name,
      communityId: 1,
      creatorId: userId.value,
      area: addForm.value.area,
      category: addForm.value.category,
      description: addForm.value.description,
      status: addForm.value.status,
      targetDate: addForm.value.deadline,
      image: addForm.value.image,
    };

    /* TODO: Fix with image uploading */
    // if (addForm.value.image) {
    //   formData.append('image', addForm.value.image);
    // }

    const res = await request({
      url: '/projects/create',
      method: 'post',
      data: requestData,
      processData: false,
    });

    if (res.code === 1) {
      ElMessage.success('Project created successfully');

      // Reset the form and close the dialog
      addFormRef.value.resetFields();

      // Clear uploaded files
      if (uploadRef.value) {
        uploadRef.value.clearFiles();
      }

      addDialogVisible.value = false;
      fetchProjects();
    } else {
      ElMessage.error(res.message || 'Project creation failed');
    }
  } catch (error) {
    console.error('Error creating project:', error);
    ElMessage.error(
      'Error creating project: ' +
        (error.response?.data?.message || error.message)
    );
  } finally {
    addDialogLoading.value = false;
  }
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
