<template>
  <div class="manager-page">
    <Header class="header" />
    <div class="main-content">
      <Sidebar v-if="!isTablet" class="sidebar" />
      <div class="content">
        <div v-if="isTablet">
          <el-result
            icon="warning"
            sub-title="Users, Communities and Projects can only be viewed on desktop"
          >
            <template v-loading="loading" #extra>
              <div class="button-container">
                <el-button
                  class="btn-primary"
                  @click="inviteDialogVisible = true"
                  >Invite User</el-button
                >
                <el-button
                  class="btn-primary ms-0"
                  @click="projectDialogVisible = true"
                  >Create Project</el-button
                >
                <el-button
                  class="btn-primary ms-0"
                  @click="communityDialogVisible = true"
                  >Create Community</el-button
                >
              </div>
            </template>
          </el-result>
        </div>
        <div v-if="!isTablet" class="manager">
          <h2 class="ps-3 pt-3">Manage</h2>
          <div class="ps-3 nav-links pe-3">
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

  <InviteUser
    :communities="communities"
    :users="users"
    :countries="countries"
    :model-value="inviteDialogVisible"
    @update:model-value="(val) => (inviteDialogVisible = val)"
    @submit="handleUserDialogClose"
  />

  <CreateCommunity
    :communities="communities"
    :countries="countries"
    :model-value="communityDialogVisible"
    @update:model-value="(val) => (communityDialogVisible = val)"
    @submit="handleCommunityDialogClose"
  />

  <CreateProject
    :projects="projects"
    :communities="communities"
    :model-value="projectDialogVisible"
    @update:model-value="(val) => (projectDialogVisible = val)"
    @submit="handleProjectDialogClose"
  />
</template>
<script setup>
import Header from '@/components/Header.vue';
import InviteUser from '../dialogs/InviteUser.vue';
import Sidebar from '@/components/Sidebar.vue';
import request from '@/utils/request';
import { ref, onMounted, onUnmounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import CreateCommunity from '../dialogs/CreateCommunity.vue';
import CreateProject from '../dialogs/CreateProject.vue';

const router = useRouter();
const route = useRoute();
const isTablet = ref(window.innerWidth <= 768);
const isSmallScreen = ref(window.innerWidth <= 600);
const inviteDialogVisible = ref(false);
const communityDialogVisible = ref(false);
const projectDialogVisible = ref(false);
const loading = ref(false);
const users = ref([]);
const communities = ref([]);
const projects = ref([]);
const countries = ref([]);

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
    loading.value = true;
    await fetchUsers();
    await fetchCommunities();
    await fetchProjects();
    await fetchCountries();
    loading.value = false;
    return;
  }

  if (route.path === '/manager') {
    router.push('/manager/users');
  }
});

const handleUserDialogClose = async () => {
  loading.value = true;
  await fetchUsers();
  loading.value = false;
};

const handleCommunityDialogClose = async () => {
  loading.value = true;
  await fetchCommunities();
  loading.value = false;
};

const handleProjectDialogClose = async () => {
  loading.value = true;
  await fetchProjects();
  loading.value = false;
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
    ElMessage.error(
      'An error occurred: ' + (error.response?.data?.message || error.message)
    );
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

const fetchProjects = async () => {
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
  }
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
</script>

<style scoped>
.manager-page {
  overflow-x: hidden;
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
