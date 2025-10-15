<template>
  <div class="user-management">
    <div class="mb-3 w-100 flex justify-content-end">
      <el-button class="btn-icon-primary" @click="inviteDialogVisible = true"
        ><el-icon><Plus class="me-1" /></el-icon>Invite User</el-button
      >
    </div>
    <el-table :data="users" style="width: 100%" border v-loading="loading">
      <el-table-column prop="id" label="ID" sortable width="70" />
      <el-table-column prop="fullName" label="Name" #default="{ row }">
        {{ `${row.firstName} ${row.lastName}` }}
      </el-table-column>
      <el-table-column prop="email" label="Email" />
      <el-table-column prop="phone" label="Phone" width="150">
        <template #default="{ row }">
          <a class="btn-link-primary" href="tel:+{{ row.phone }}">{{
            row.phone || '-'
          }}</a>
        </template>
      </el-table-column>
      <el-table-column prop="role" label="Role" sortable>
        <template #default="scope">
          <el-tag :type="getUserRoleClass(scope.row.role)" effect="light">
            {{ getUserRole(scope.row.role) }}
          </el-tag>
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

    <InviteUser
      :communities="communities"
      :users="users"
      :countries="countries"
      :model-value="inviteDialogVisible"
      @update:model-value="(val) => (inviteDialogVisible = val)"
      @submit="fetchUsers"
    />

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
        <el-form-item label="Phone" prop="phone">
          <el-input
            type="tel"
            v-model="editForm.phone"
            @keypress="sanitizePhoneNumber"
            placeholder="e.g. +61412345678"
            required
          />
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
import { ref, onMounted, reactive, computed } from 'vue';
import { Delete, Edit, Plus } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import InviteUser from '../dialogs/InviteUser.vue';
import { getUserRole, getUserRoleClass } from '@/utils/roleHelper';
import request from '@/utils/request';
import { useUserStore } from '@/stores/userStore';
import { sanitizePhoneNumber } from '@/utils/phoneHelper';

const users = ref([]);
const communities = ref([]);
const countries = ref([]);
const loading = ref(false);
const inviteDialogVisible = ref(false);
const editDialogLoading = ref(false);
const editDialogVisible = ref(false);
const editFormRef = ref(null);
const editingUser = ref(null);
const userStore = useUserStore();
const currentUserId = computed(() => userStore.userInfo?.id || null);

const editForm = reactive({
  firstName: '',
  lastName: '',
  email: '',
  phone: '',
});

const handleEditDialogClose = () => {
  editDialogVisible.value = false;
  editingUser.value = null;

  if (editFormRef.value) {
    editFormRef.value.resetFields();
  }
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
  phone: [
    { required: true, message: 'Required field', trigger: 'blur' },
    {
      pattern: /^(\+?[1-9]\d{1,14}|0\d{8,10})$/,
      message: 'Invalid phone number',
      trigger: 'blur',
    },
  ],
};

// Fetch user list
const fetchUsers = async () => {
  loading.value = true;
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
    ElMessage.error(
      'An error occurred: ' + (error.response?.data?.message || error.message)
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
        phone: !!editForm.phone ? editForm.phone : null,
      },
      {
        headers: { 'Content-Type': 'application/json' },
      }
    );

    ElMessage.success('User updated successfully');

    editDialogVisible.value = false;
    editingUser.value = null;
    editFormRef.value.resetFields();
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
    `Are you sure you want to delete user ${row.firstName} ${row.lastName}? This action cannot be undone.`,
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

const handleEdit = (row) => {
  editDialogVisible.value = true;

  editForm.firstName = row.firstName;
  editForm.lastName = row.lastName;
  editForm.email = row.email;
  editForm.phone = row.phone;

  // keep a snapshot of original values
  editingUser.value = { ...row };
};

// Can't delete yourself or user with ID 1 (original admin)
const isRowDisabled = (row) => row.id === 1 || row.id === currentUserId.value;

onMounted(() => {
  loading.value = true;
  fetchCountries();
  fetchCommunities();
  fetchUsers(); // fetch users sets loading to true and false
});
</script>

<style scoped>
.user-management {
  overflow-x: hidden;
  padding: 0 1rem 1rem 1rem;
}

.el-table {
  margin-top: 20px;
}
</style>
