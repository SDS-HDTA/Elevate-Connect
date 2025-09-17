<template>
  <div class="user-management">
    <div class="mb-3 w-100 flex justify-content-end">
      <el-button class="btn-primary" @click="inviteDialogVisible = true"
        >Invite User</el-button
      >
    </div>
    <el-table :data="users" style="width: 100%" border v-loading="loading">
      <el-table-column prop="id" label="ID" sortable width="80" />
      <el-table-column prop="fullName" label="Name" />
      <el-table-column prop="email" label="Email" />
      <el-table-column prop="role" label="Role" sortable>
        <template #default="scope">
          <el-tag :type="getUserRoleClass(scope.row.role)" effect="light">
            {{ getUserRole(scope.row.role) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="Create Time" width="180">
        <template #default="{ row }">
          {{ new Date(row.createTime).toLocaleString() }}
        </template>
      </el-table-column>
      <el-table-column width="80">
        <template #default="scope">
          <el-tooltip content="Edit User" placement="top">
            <el-button class="btn-icon-info">
              <el-icon><Edit /></el-icon>
            </el-button>
          </el-tooltip>
          <el-tooltip content="Delete User" placement="top">
            <el-button class="btn-icon-danger" @click="handleDelete(scope.row)">
              <el-icon><Delete /></el-icon>
            </el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="Invite User" v-model="inviteDialogVisible" width="500px">
      <el-form
        :model="inviteForm"
        :rules="inviteRules"
        ref="inviteFormRef"
        label-width="120px"
      >
        <el-form-item label="Email" prop="email">
          <el-input v-model="inviteForm.email" />
        </el-form-item>

        <el-form-item label="Role" prop="role">
          <el-select v-model="inviteForm.role" placeholder="Select role">
            <el-option
              v-for="(label, value) in roleMap"
              :key="value"
              :label="label"
              :value="Number(value)"
            />
          </el-select>
        </el-form-item>

        <el-form-item
          v-if="communities.length > 0 && inviteForm.role !== 3"
          label="Community"
          prop="community"
        >
          <el-select
            filterable
            v-model="inviteForm.community"
            placeholder="Select community"
          >
            <el-option
              v-for="community in communities"
              :key="community.id"
              :label="community.name"
              :value="community.id"
            />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button class="btn-secondary" @click="inviteDialogVisible = false"
          >Cancel</el-button
        >
        <el-button class="btn-primary" @click="submitInvite">Invite</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, reactive } from 'vue';
import { Delete, Edit } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { roleMap, getUserRole, getUserRoleClass } from '@/utils/roleHelper';
import request from '@/utils/request';

const users = ref([]);
const communities = ref([]);
const loading = ref(false);

const inviteDialogVisible = ref(false);
const inviteFormRef = ref(null);

const inviteForm = reactive({
  email: '',
  role: null,
  community: null,
});

watch(
  () => inviteForm.role,
  (newRole) => {
    if (newRole === 3) {
      inviteForm.community = null;
    }
  }
);

const inviteRules = {
  email: [
    { required: true, message: 'Please input email', trigger: 'blur' },
    { type: 'email', message: 'Please enter a valid email', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (
          users.value.some((u) => u.email.toLowerCase() === value.toLowerCase())
        ) {
          callback(new Error('This email already exists'));
        } else {
          callback();
        }
      },
      trigger: 'blur',
    },
  ],
  role: [
    { required: true, message: 'Please select a role', trigger: 'change' },
  ],
  community: [
    {
      validator: (rule, value, callback) => {
        if (inviteForm.role !== 3 && !value) {
          callback(new Error('Please select a community'));
        } else {
          callback();
        }
      },
      trigger: 'change',
    },
  ],
};

const submitInvite = async () => {
  if (!inviteFormRef.value) return;

  try {
    // validate form with custom validators
    await inviteFormRef.value.validate();

    loading.value = true;
    const params = new URLSearchParams();
    params.append('email', inviteForm.value.email);
    params.append('role', inviteForm.value.role);
    params.append('userId', localStorage.getItem('userId'));
    params.append('community', inviteForm.value.community);

    const res = await request.post('/manager/sendInvitationCode', params, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    });

    if (res.code === 1) {
      ElMessage.success('User invited successfully');

      // Only reset & close on success
      inviteFormRef.value.resetFields();
      inviteDialogVisible.value = false;
      fetchUsers();
    } else {
      ElMessage.error('An error occurred');
      loading.value = false;
    }
  } catch (error) {
    ElMessage.error('An error occurred');
    loading.value = false;
  }
};

// Fetch user list
const fetchUsers = async () => {
  loading.value = true;
  try {
    const params = new URLSearchParams();
    params.append('userId', localStorage.getItem('userId'));
    const response = await request.get('/manager/users', {
      params: params,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });
    if (response.code === 1) {
      users.value = response.data;
    } else {
      ElMessage.error('Failed to get user list: ' + response.message);
    }
  } catch (error) {
    ElMessage.error(
      'Failed to get user list: ' +
        (error.response?.data?.message || error.message)
    );
  } finally {
    loading.value = false;
  }
};

const fetchCommunities = async () => {
  try {
    const params = new URLSearchParams();
    params.append('userId', localStorage.getItem('userId'));
    const response = await request.get('/community', {
      params: params,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });
    if (response.code === 1) {
      communities.value = response.data;
    } else {
      ElMessage.error('Failed to get communities');
    }
  } catch (error) {
    ElMessage.error('Failed to get communities');
  } finally {
    loading.value = false;
  }
};

// Delete user
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `Are you sure you want to delete user ${row.fullName}? This action cannot be undone.`,
    'Confirm',
    {
      confirmButtonClass: 'btn-danger',
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
    }
  )
    .then(async () => {
      try {
        const params = new URLSearchParams();
        params.append('userId', localStorage.getItem('userId'));
        await request.delete(`/manager/users/${row.id}`, {
          params: params,
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
        });
        const index = users.value.findIndex((user) => user.id === row.id);
        if (index !== -1) {
          users.value.splice(index, 1);
        }
        ElMessage({
          type: 'success',
          message: 'Delete successfully',
        });
      } catch (error) {
        ElMessage.error(
          'Delete user failed: ' +
            (error.response?.data?.message || error.message)
        );
      }
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: 'Delete canceled',
      });
    });
};

// Fetch user list when component is mounted
onMounted(() => {
  fetchUsers();
  fetchCommunities();
});
</script>

<style scoped>
.user-management {
  padding: 1rem;
}

.el-table {
  margin-top: 20px;
}
</style>
