<template>
  <div class="header">
    <div class="logo-container" @click="router.push('/')">
      <img src="/logo.png" class="logo" />
      <span class="app-name" v-if="!isTablet">Elevate Connect</span>
    </div>
    <div class="user-info">
      <el-dropdown @command="handleCommand">
        <el-icon v-if="isTablet" class="menu">
          <Grid />
        </el-icon>
        <div v-else-if="userInfo" class="user-link">
          <Avatar :username="userInfo.username" :size="32" />
          <span class="username">{{ userInfo.username }}</span>
        </div>
        <div v-else-if="!userInfo">
          <router-link to="/login" class="login-link">
            <el-button type="text">Sign in</el-button>
          </router-link>
        </div>
        <template #dropdown>
          <el-dropdown-menu v-if="!userInfo">
            <el-dropdown-item @click="router.push('/login')">
              Sign in
            </el-dropdown-item>
          </el-dropdown-menu>

          <el-dropdown-menu v-else>
            <el-dropdown-item v-if="isTablet" command="profile">
              <div class="user-link">
                <Avatar :username="userInfo.username" :size="32" />
                <span class="username">{{ userInfo?.username }}</span>
              </div>
            </el-dropdown-item>
            <el-dropdown-item v-if="isTablet" @click="router.push('/')" divided>
              Project Feed
            </el-dropdown-item>
            <el-dropdown-item v-if="isTablet" @click="router.push('/')" divided>
              Browse Projects
            </el-dropdown-item>
            <el-dropdown-item
              v-if="isTablet"
              @click="router.push('/my-projects')"
              divided
            >
              My Projects
            </el-dropdown-item>
            <el-dropdown-item
              v-if="isTablet"
              @click="router.push('/manager-view')"
              divided
            >
              Manager View
            </el-dropdown-item>
            <el-dropdown-item :divided="isTablet" command="logout" divided>
              Logout
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { useRouter, RouterLink } from 'vue-router';
import Avatar from './Avatar.vue';
import { ElMessage } from 'element-plus';
import request from '@/utils/request';
import { Grid } from '@element-plus/icons-vue';

const router = useRouter();
const userInfo = ref(null);
const isTablet = ref(window.innerWidth <= 768);

const updateScreen = () => {
  isTablet.value = window.innerWidth <= 768;
};

const getUserInfo = async () => {
  try {
    const userId = localStorage.getItem('userId');
    const res = await request.get(`/user/info?userId=${userId}`);
    if (res.code === 1) {
      userInfo.value = res.data;
      localStorage.setItem('username', res.data.username);
      localStorage.setItem('userEmail', res.data.email);
    }
  } catch (error) {
    console.error('Failed to fetch user info:', error);
    userInfo.value = null;
  }
};

const handleCommand = async (command) => {
  if (command === 'profile') {
    router.push(`/profile/${userInfo.value.id}`);
  } else if (command === 'logout') {
    try {
      // Clear user information from local storage
      localStorage.removeItem('userId');
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      localStorage.removeItem('userEmail');
      // Clear user information
      userInfo.value = null;
      // Show success message
      ElMessage.success('Logout successfully');
      // Navigate to home page
      router.push('/');
    } catch (error) {
      console.error('Logout failed:', error);
      ElMessage.error('Logout failed');
    }
  }
};

onMounted(() => {
  getUserInfo();
  window.addEventListener('resize', updateScreen);
});

onUnmounted(() => {
  userInfo.value = null;
  window.removeEventListener('resize', updateScreen);
});
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-info {
  display: flex;
  align-items: center;
}

.user-link {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #333;
}

.login-link {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #333;
}

.menu {
  font-size: 26px;
  cursor: pointer;
  color: #106a52;
}

.logo {
  width: 40px;
  height: 40px;
  margin-right: 15px;
}

.logo-container {
  cursor: pointer;
  display: flex;
  flex-direction: row;
  align-items: center;
}

.app-name {
  font-size: 18px;
  font-weight: bold;
}

.username {
  margin-left: 10px;
}
</style>
