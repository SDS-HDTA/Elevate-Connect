<template>
  <div class="community-management">
    <div class="mb-3 mt-2 w-100 flex justify-content-between">
      <el-input
        v-model="searchQuery"
        placeholder="Search by name or country"
        clearable
        style="width: 300px"
      />
      <el-button class="btn-icon-primary" @click="addDialogVisible = true"
        ><el-icon><Plus class="me-1" /></el-icon>Create Community</el-button
      >
    </div>
    <el-table
      height="calc(100vh - 234px)"
      v-loading="loading"
      :data="filteredCommunities"
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

  <CreateCommunity
    :communities="communities"
    :countries="countries"
    :model-value="addDialogVisible"
    @update:model-value="(val) => (addDialogVisible = val)"
    @submit="fetchCommunities"
  />

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
          placeholder="Briefly describe the community (max 150 characters)"
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
import CreateCommunity from '../dialogs/CreateCommunity.vue';

const communities = ref([]);
const countries = ref([]);
const loading = ref(false);
const addDialogVisible = ref(false);
const editDialogLoading = ref(false);
const editDialogVisible = ref(false);
const userStore = useUserStore();
const editingCommunity = ref(null);
const searchQuery = ref('');

const filteredCommunities = computed(() => {
  if (!searchQuery.value.trim()) return communities.value;

  const query = searchQuery.value.toLowerCase();
  return communities.value.filter((community) => {
    const name = community.name.toLowerCase();
    const country = community.country.toLowerCase();
    return name.includes(query) || country.includes(query);
  });
});

const editFormRef = ref(null);
const editForm = reactive({
  name: '',
  shortDescription: '',
});
const userId = computed(() => userStore.userInfo?.id || null);

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

const handleEditDialogClose = () => {
  editDialogVisible.value = false;
  if (editFormRef.value) editFormRef.value.resetFields();
};

const handleEdit = (row) => {
  editDialogVisible.value = true;

  editForm.name = row.name;
  editForm.country = row.country;
  editForm.shortDescription = row.shortDescription;

  // keep a snapshot of original values
  editingCommunity.value = { ...row };
};

const fetchCountries = async () => {
  try {
    const response = await request.get('/countries', {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });

    if (response) {
      countries.value = response;
    }
  } catch (error) {
    ElMessage.error('An error occurred: ' + error);
  }
};

// Get community list when component is mounted
onMounted(() => {
  loading.value = true;
  fetchCountries();
  fetchCommunities();
});
</script>

<style scoped>
.community-management {
  overflow-x: hidden;
  padding: 0 1rem 1rem 1rem;
}
</style>
