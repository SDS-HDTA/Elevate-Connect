<template>
  <div class="community-management">
    <div class="mb-3 w-100 flex justify-content-end">
      <el-button class="btn-icon-primary" @click="addDialogVisible = true"
        ><el-icon><Plus class="me-1" /></el-icon>Create Community</el-button
      >
    </div>
    <el-table
      v-loading="loading"
      :data="communities"
      style="width: 100%"
      border
    >
      <el-table-column prop="id" label="ID" width="80" sortable />
      <el-table-column prop="name" label="Name" sortable />
      <el-table-column prop="country" label="Country" sortable />
      <el-table-column prop="memberCount" label="Members" sortable />
      <el-table-column prop="projectCount" label="Projects" sortable />
      <el-table-column width="40">
        <template #default="{ row }">
          <el-tooltip content="Edit Community" placement="top">
            <el-button @click="handleEdit(row)" class="btn-icon-info">
              <el-icon><Edit /></el-icon>
            </el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <el-dialog
    title="Create New Community"
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

      <el-form-item label="Country" prop="country">
        <el-input v-model="addForm.country" />
      </el-form-item>

      <el-form-item label="Short Description" prop="shortDescription">
        <el-input
          type="textarea"
          v-model="addForm.shortDescription"
          :maxlength="150"
          placeholder="Briefly describe community (max 150 characters)"
          resize="vertical"
        />
      </el-form-item>

      <!-- TODO: make a dropdown for country instead -->
      <!-- <el-form-item label="Status" prop="status">
        <el-select v-model="addForm.status" placeholder="Select status">
          <el-option label="Empathise" :value="0" />
          <el-option label="Discover" :value="1" />
          <el-option label="Define" :value="2" />
          <el-option label="Ideate" :value="3" />
          <el-option label="Prototype" :value="4" />
          <el-option label="Completed" :value="5" />
        </el-select>
      </el-form-item> -->
    </el-form>

    <template #footer>
      <el-button class="btn-secondary" @click="handleAddDialogClose">
        Cancel
      </el-button>
      <el-button class="btn-primary" @click="submitAdd">Create</el-button>
    </template>
  </el-dialog>

  <el-dialog
    :before-close="handleEditDialogClose"
    title="Edit Community"
    v-model="editDialogVisible"
    width="500px"
  >
    <el-form
      :model="editForm"
      :rules="editRules"
      v-loading="editDialogLoading"
      ref="editFormRef"
      label-width="120px"
    >
      <el-form-item label="Name" prop="name">
        <el-input v-model="editForm.name" />
      </el-form-item>
      <el-form-item label="Short Description" prop="shortDescription">
        <el-input
          type="textarea"
          v-model="editForm.shortDescription"
          :maxlength="150"
          placeholder="Briefly describe community (max 150 characters)"
          resize="vertical"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button class="btn-secondary" @click="handleEditDialogClose"
        >Cancel</el-button
      >
      <el-button class="btn-primary" @click="submitEdit(editingCommunity.id)"
        >Save</el-button
      >
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted, computed, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import request from '@/utils/request';
import { Plus, Edit } from '@element-plus/icons-vue';
import { useUserStore } from '@/stores/userStore';

const communities = ref([]);
const loading = ref(false);
const addDialogVisible = ref(false);
const editDialogLoading = ref(false);
const addDialogLoading = ref(false);
const editDialogVisible = ref(false);
const userStore = useUserStore();
const addFormRef = ref(null);
const editingCommunity = ref(null);
const addForm = ref({
  name: '',
  country: '',
  shortDescription: '',
});
const editFormRef = ref(null);
const editForm = reactive({
  name: '',
  shortDescription: '',
});
const userId = computed(() => userStore.userInfo?.id || null);

const addRules = {
  name: [{ required: true, message: 'Required field', trigger: 'blur' }],
  country: [{ required: true, message: 'Required field', trigger: 'blur' }],
  shortDescription: [
    { required: true, message: 'Required field', trigger: 'blur' },
  ],
};

const editRules = {
  name: [{ required: true, message: 'Required field', trigger: 'blur' }],
  shortDescription: [
    { required: true, message: 'Required field', trigger: 'blur' },
  ],
};

const fetchCommunities = async () => {
  loading.value = true;
  try {
    const params = new URLSearchParams();
    params.append('userId', userId.value);
    const response = await request.get('/community', {
      params: params,
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
  } finally {
    loading.value = false;
  }
};

const submitAdd = async () => {
  if (!addFormRef.value) return;

  const validForm = await addFormRef.value.validate();
  if (!validForm) return;

  try {
    addDialogLoading.value = true;

    const res = await request.post(
      '/community/create',
      {
        name: addForm.value.name,
        country: addForm.value.country,
        short_description: addForm.value.shortDescription,
      },
      {
        headers: { 'Content-Type': 'application/json' },
      }
    );

    if (res.code === 1) {
      ElMessage.success('Community created successfully');

      addDialogVisible.value = false;
      addFormRef.value.resetFields();
      fetchCommunities();
    } else {
      ElMessage.error('An error occurred: ' + res.message);
    }
  } catch (error) {
    ElMessage.error(
      'An error occurred: ' + (error.response?.data?.message || error.message)
    );
  } finally {
    addDialogLoading.value = false;
  }
};

const submitEdit = async (editingCommunityId) => {
  if (!editFormRef.value) return;

  const validForm = await editFormRef.value.validate();
  if (!validForm) return;

  try {
    editDialogLoading.value = true;

    await request.put(
      '/community',
      {
        id: editingCommunityId,
        name: editForm.name,
        shortDescription: editForm.shortDescription,
      },
      {
        headers: { 'Content-Type': 'application/json' },
      }
    );

    ElMessage.success('Community updated successfully');

    editDialogVisible.value = false;
    editingCommunity.value = null;
    editFormRef.value.resetFields();
    fetchCommunities();
  } catch (error) {
    ElMessage.error('An error occurred: ' + error.message);
  } finally {
    editDialogLoading.value = false;
  }
};

const handleEditDialogClose = (done) => {
  editDialogVisible.value = false;
  if (editFormRef.value) editFormRef.value.resetFields();
  done();
};

const handleAddDialogClose = (done) => {
  addDialogVisible.value = false;
  if (addFormRef.value) addFormRef.value.resetFields();
  done();
};

const handleEdit = (row) => {
  editDialogVisible.value = true;

  editForm.name = row.name;
  editForm.country = row.country;
  editForm.shortDescription = row.shortDescription;

  // keep a snapshot of original values
  editingCommunity.value = { ...row };
};

// Get community list when component is mounted
onMounted(() => {
  fetchCommunities();
});
</script>

<style scoped>
.community-management {
  padding: 1rem;
}
</style>
