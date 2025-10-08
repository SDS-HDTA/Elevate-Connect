<template>
  <div class="manager-page">
    <Header class="header" />
    <div class="main-content">
      <Sidebar v-if="!isTablet" class="sidebar" />
      <div class="content">
        <div v-if="isTablet" class="not-found-container">
          <el-result
            icon="warning"
            sub-title="Users, Communities and Projects can only be viewed on desktop"
          >
            <template v-loading="loading" #extra>
              <div class="button-container">
                <el-button class="btn-primary" @click="fetchCommunities()"
                  >Invite User</el-button
                >
                <el-button
                  class="btn-primary ms-0"
                  @click="router.push('/my-projects')"
                  >Create Project</el-button
                >
                <el-button
                  class="btn-primary ms-0"
                  @click="router.push('/my-projects')"
                  >Create Community</el-button
                >
              </div>
            </template>
          </el-result>
        </div>
        <div v-if="!isTablet" class="manager">
          <h2 class="ps-3 pt-3">Manage</h2>
          <div class="ps-3 nav-links">
            <router-link
              to="/manager/users"
              class="nav-link"
              active-class="router-link-active"
              >Users</router-link
            >
            <router-link
              to="/manager/projects"
              class="nav-link"
              active-class="router-link-active"
              >Projects</router-link
            >
            <router-link
              to="/manager/community"
              class="nav-link"
              active-class="router-link-active"
              >Community</router-link
            >
          </div>
          <div class="content-area">
            <router-view></router-view>
          </div>
        </div>
      </div>
    </div>
  </div>
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
</template>
<script setup>
import Header from '@/components/Header.vue';
import Sidebar from '@/components/Sidebar.vue';
import request from '@/utils/request';
import { roleMap } from '@/utils/roleHelper';
import { ref, onMounted, onUnmounted, watch, reactive } from 'vue';
import { useRouter, useRoute } from 'vue-router';

const router = useRouter();
const route = useRoute();
const isTablet = ref(window.innerWidth <= 768);
const isSmallScreen = ref(window.innerWidth <= 600);
const inviteDialogLoading = ref(false);
const inviteDialogVisible = ref(false);
const inviteFormRef = ref(null);
const loading = ref(false);
const users = ref([]);
const communities = ref([]);

const inviteForm = reactive({
  email: '',
  role: null,
  community: null,
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
  inviteDialogVisible.value = false;

  if (inviteFormRef.value) {
    inviteFormRef.value.resetFields();
  }
  done();
};

const updateScreen = () => {
  isTablet.value = window.innerWidth <= 768;
  isSmallScreen.value = window.innerWidth <= 600;
};

onUnmounted(() => {
  window.removeEventListener('resize', updateScreen);
});

onMounted(async () => {
  window.addEventListener('resize', updateScreen);

  if (isTablet.value) {
    await fetchUsers();
    return;
  }

  if (route.path === '/manager') {
    router.push('/manager/users');
  }
});

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

const submitInvite = async () => {
  if (!inviteFormRef.value) return;

  const validForm = await inviteFormRef.value.validate();
  if (!validForm) return;

  try {
    inviteDialogLoading.value = true;

    const params = new URLSearchParams();
    params.append('email', inviteForm.email);
    params.append('role', inviteForm.role);
    params.append('userId', currentUserId.value);
    params.append('community', inviteForm.community);

    const res = await request.post('/manager/sendInvitationCode', params, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    });

    if (res.code === 1) {
      ElMessage.success('User invited successfully');

      inviteDialogVisible.value = false;
      inviteFormRef.value.resetFields();
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

const fetchCommunities = async () => {
  inviteDialogVisible.value = true;
  inviteDialogLoading.value = true;
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
  } finally {
    inviteDialogLoading.value = false;
  }
};

const requiresCommunity = (role) => role === 0;
</script>

<style scoped>
.manager-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
}

.main-content {
  display: flex;
  margin-top: 60px;
  flex: 1;
  overflow-y: auto;
}

.sidebar {
  position: fixed;
  left: 0;
  top: 60px;
  bottom: 0;
}

.content {
  flex: 1;
  background-color: var(--color-background-light);
  min-height: calc(100vh - 60px);
  padding: 0;
  display: flex;
  flex-direction: column;

  @media screen and (min-width: 769px) {
    margin-left: 200px; /* Adjust based on sidebar width */
  }
}

.manager {
  flex: 1;
  width: calc(100vw - 200px);
  display: flex;
  flex-direction: column;
}

.content-area {
  flex: 1;
  height: 100%;
  border-radius: 0 0 8px 8px;
  overflow: auto;
}

.not-manager {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  font-size: 24px;
  color: #666;
  background: var(--color-background-light);
}

.menu {
  font-size: 26px;
  color: var(--color-primary);
  cursor: pointer;
}

.button-container {
  display: flex;
  gap: 0.5rem;
  flex-direction: row;
  align-items: center;
}

@media screen and (max-width: 600px) {
  .button-container {
    display: flex;
    gap: 1rem;
    flex-direction: column;
    align-items: center;
  }

  .button-container .el-button {
    width: auto; /* default */
    min-width: 100%; /* match the "I have a code" width */
  }
}
</style>
