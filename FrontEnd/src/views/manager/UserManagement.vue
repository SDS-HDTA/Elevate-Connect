<template>
  <div class="user-management">
    <el-table :data="users" style="width: 100%" border v-loading="loading">
      <el-table-column prop="id" label="ID" sortable width="80" />
      <el-table-column prop="fullName" label="Name" />
      <el-table-column prop="email" label="Email" />
      <el-table-column prop="role" label="Role" sortable>
        <template #default="scope">
          <el-tag
            :type="scope.row.role === 3 ? 'success' : 'warning'"
            effect="light"
          >
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
          <el-tooltip content="Delete User" placement="top">
            <el-button class="btn-icon-danger" @click="handleDelete(scope.row)">
              <el-icon><Delete /></el-icon>
            </el-button>
          </el-tooltip>
          <el-tooltip content="Edit User" placement="top">
            <el-button class="btn-icon-primary">
              <el-icon><Edit /></el-icon>
            </el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { Delete, Edit } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import request from '@/utils/request';

const users = ref([]);
const loading = ref(false);

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

// User role mapping
const getUserRole = (role) => {
  const roleMap = {
    0: 'Community Insight Partner',
    1: 'Country Collaboration Partner',
    2: 'Humanitarian Impact Partner',
    3: 'Elevate Facilitation Lead',
  };
  return roleMap[role] || 'Unknown Role';
};

// Delete user
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `Are you sure you want to delete user ${row.name}?`,
    'Warning',
    {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning',
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
