<template>
  <div class="user-management">
    <div class="mb-3 w-100 flex justify-content-end">
      <el-button class="btn-icon-primary" @click="fetchCommunities()"
        ><el-icon><Plus class="me-1" /></el-icon>Invite User</el-button
      >
    </div>
    <el-table :data="users" style="width: 100%" border v-loading="loading">
      <el-table-column prop="id" label="ID" sortable width="80" />
      <el-table-column prop="fullName" label="Name" #default="{ row }">
        {{ `${row.firstName} ${row.lastName}` }}
      </el-table-column>
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
            <el-button class="btn-icon-info" @click="handleEdit(scope.row)">
              <el-icon><Edit /></el-icon>
            </el-button>
          </el-tooltip>
          <el-tooltip
            :content="
              isRowDisabled(scope.row)
                ? `Cannot delete yourself or this user`
                : 'Delete User'
            "
            placement="top"
          >
            <el-button
              :disabled="isRowDisabled(scope.row)"
              class="btn-icon-danger"
              @click="handleDelete(scope.row)"
            >
              <el-icon><Delete /></el-icon>
            </el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      :before-close="handleInviteDialogClose"
      title="Invite User"
      v-model="inviteDialogVisible"
      width="500px"
    >
      <el-form
        v-loading="inviteDialogLoading"
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
          v-if="
            communities.length > 0 &&
            requiresCommunity(inviteForm.role) &&
            inviteForm.role !== null
          "
          label="Community"
          :required="requiresCommunity(inviteForm.role)"
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
        <el-button class="btn-secondary" @click="handleInviteDialogClose"
          >Cancel</el-button
        >
        <el-button class="btn-primary" @click="submitInvite">Invite</el-button>
      </template>
    </el-dialog>

    <el-dialog
      :before-close="handleEditDialogClose"
      title="Edit User"
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
        <el-form-item label="First Name" prop="firstName">
          <el-input v-model="editForm.firstName" />
        </el-form-item>
        <el-form-item label="Last Name" prop="lastName">
          <el-input v-model="editForm.lastName" />
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="editForm.email" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button class="btn-secondary" @click="handleEditDialogClose"
          >Cancel</el-button
        >
        <el-button class="btn-primary" @click="submitEdit(editingUser.id)"
          >Save</el-button
        >
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, reactive, computed } from 'vue';
import { Delete, Edit, Plus } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { roleMap, getUserRole, getUserRoleClass } from '@/utils/roleHelper';
import request from '@/utils/request';
import { useUserStore } from '@/stores/userStore';

const users = ref([]);
const communities = ref([]);
const loading = ref(false);
const inviteDialogLoading = ref(false);
const editDialogLoading = ref(false);
const inviteDialogVisible = ref(false);
const editDialogVisible = ref(false);
const inviteFormRef = ref(null);
const editFormRef = ref(null);
const editingUser = ref(null);
const userStore = useUserStore();
const currentUserId = computed(() => userStore.userInfo?.id || null);

const inviteForm = reactive({
  email: '',
  role: null,
  community: null,
});

const editForm = reactive({
  firstName: '',
  lastName: '',
  email: '',
});

watch(
  () => inviteForm.role,
  (newRole) => {
    if (!requiresCommunity(newRole)) {
      inviteForm.community = null;
    }
  }
);

const handleInviteDialogClose = (done) => {
  if (inviteFormRef.value) {
    inviteFormRef.value.resetFields();
  }
  inviteDialogVisible.value = false;
  done();
};

const handleEditDialogClose = (done) => {
  if (editFormRef.value) {
    editFormRef.value.resetFields();
  }
  editingUser.value = null;
  editDialogVisible.value = false;
  done();
};

const inviteRules = {
  email: [
    { required: true, message: 'Required field', trigger: 'blur' },
    { type: 'email', message: 'Invalid email', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (
          users.value.some((u) => u.email.toLowerCase() === value.toLowerCase())
        ) {
          callback(new Error('Email already in use'));
        } else {
          callback();
        }
      },
      trigger: 'blur',
    },
  ],
  role: [{ required: true, message: 'Required field', trigger: 'change' }],
  community: [
    {
      validator: (rule, value, callback) => {
        if (requiresCommunity(inviteForm.role) && !value) {
          callback(new Error('Required field'));
        } else {
          callback();
        }
      },
      trigger: 'change',
    },
  ],
};

const editRules = {
  email: [
    { required: true, message: 'Required field', trigger: 'blur' },
    { type: 'email', message: 'Invalid email', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (editingUser.value && value === editingUser.value.email)
          return callback();
        if (
          users.value.some(
            (u) =>
              u.email.toLowerCase() === value.toLowerCase() &&
              u.id !== editingUser.value.id
          )
        ) {
          return callback(new Error('Email already in use'));
        }
        callback();
      },
      trigger: 'blur',
    },
  ],
  firstName: [
    {
      validator: (rule, value, callback) => {
        if (!value) return callback(new Error('Required field'));
        callback();
      },
      trigger: 'blur',
    },
  ],
  lastName: [
    {
      validator: (rule, value, callback) => {
        if (!value) return callback(new Error('Required field'));
        callback();
      },
      trigger: 'blur',
    },
  ],
};

const submitInvite = async () => {
  if (!inviteFormRef.value) return;

  const validForm = await inviteFormRef.value.validate();
  if (!validForm) return;

  try {
    inviteDialogLoading.value = true;

    const params = new URLSearchParams();
    params.append('email', inviteForm.email);
    params.append('role', inviteForm.role);
    params.append('userId', localStorage.getItem('userId'));
    params.append('community', inviteForm.community);

    const res = await request.post('/manager/sendInvitationCode', params, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    });

    if (res.code === 1) {
      ElMessage.success('User invited successfully');

      inviteFormRef.value.resetFields();
      inviteDialogVisible.value = false;
      fetchUsers();
    } else {
      ElMessage.error('An error occurred: ' + res.message);
    }
  } catch (error) {
    ElMessage.error(
      'An error occurred: ' + (error.response?.data?.message || error.message)
    );
  } finally {
    inviteDialogLoading.value = false;
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
      ElMessage.error('An error occurred: ' + response.message);
    }
  } catch (error) {
    ElMessage.error(
      'An error occurred: ' + (error.response?.data?.message || error.message)
    );
  } finally {
    loading.value = false;
  }
};

const fetchCommunities = async () => {
  inviteDialogVisible.value = true;
  inviteDialogLoading.value = true;
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
      ElMessage.error('An error occurred: ' + response.message);
    }
  } catch (error) {
    ElMessage.error('An error occurred: ' + error.message);
  } finally {
    inviteDialogLoading.value = false;
  }
};

const submitEdit = async (editingUserId) => {
  if (!editFormRef.value) return;

  const validForm = await editFormRef.value.validate();
  if (!validForm) return;

  try {
    editDialogLoading.value = true;

    await request.put(
      '/user',
      {
        id: editingUserId,
        email: editForm.email,
        firstName: editForm.firstName,
        lastName: editForm.lastName,
      },
      {
        headers: { 'Content-Type': 'application/json' },
      }
    );

    ElMessage.success('User updated successfully');

    editFormRef.value.resetFields();
    editingUser.value = null;
    editDialogVisible.value = false;
    fetchUsers();
  } catch (error) {
    ElMessage.error('An error occurred: ' + error.message);
  } finally {
    editDialogLoading.value = false;
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
      loading.value = true;
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
          message: 'Deleted successfully',
        });
      } catch (error) {
        ElMessage.error(
          'An error occurred: ' +
            (error.response?.data?.message || error.message)
        );
      } finally {
        loading.value = false;
      }
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: 'Delete canceled',
      });
      loading.value = false;
    });
};

const handleEdit = (row) => {
  editDialogVisible.value = true;

  editForm.firstName = row.firstName;
  editForm.lastName = row.lastName;
  editForm.email = row.email;

  // keep a snapshot of original values
  editingUser.value = { ...row };
};

// Can't delete yourself or user with ID 1 (original admin)
const isRowDisabled = (row) => row.id === 1 || row.id === currentUserId.value;
const requiresCommunity = (role) => role === 0;

onMounted(() => {
  fetchUsers();
});
</script>

<style scoped>
.user-management {
  padding: 0 1rem 1rem 1rem;
}

.el-table {
  margin-top: 20px;
}
</style>
