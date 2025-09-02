<template>
  <div class="manager-page">
    <Header class="header" />
    <div class="main-content">
      <Sidebar v-if="!isTablet" class="sidebar" />
      <div class="content">
        <div class="not-manager" v-if="!isManager">
          <el-empty
            description="You do not have permission to access this page"
            image-size="120"
          >
            <el-icon
              style="font-size: 48px; color: #f56c6c; margin-bottom: 16px"
            >
              <CircleCloseFilled />
            </el-icon>
            <div style="margin-top: 12px">
              <el-button type="primary" @click="goHome">Back to Home</el-button>
            </div>
          </el-empty>
        </div>
        <div class="manager" v-else>
          <div class="nav-links">
            <router-link
              to="/manager/invite"
              class="nav-link"
              active-class="router-link-active"
              >Invitation</router-link
            >
            <router-link
              to="/manager/users"
              class="nav-link"
              active-class="router-link-active"
              >User Management</router-link
            >
            <router-link
              to="/manager/projects"
              class="nav-link"
              active-class="router-link-active"
              >Project Management</router-link
            >
          </div>
          <!-- <el-dropdown v-if="isTablet">
            <el-icon class="menu">
              <Menu />
            </el-icon>

            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>
                  <router-link to="/manager/invite">Invitation</router-link>
                </el-dropdown-item>
                <el-dropdown-item divided>
                  <router-link to="/manager/users">User Management</router-link>
                </el-dropdown-item>
                <el-dropdown-item divided>
                  <router-link to="/manager/projects"
                    >Project Management</router-link
                  >
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown> -->
          <div class="content-area">
            <router-view></router-view>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import Header from '@/components/Header.vue';
import Sidebar from '@/components/Sidebar.vue';
import { ref, onMounted, onUnmounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { CircleCloseFilled, Menu } from '@element-plus/icons-vue';

const isManager = ref(false);
const mangaerEmail = ref('matthew@adler.id.au');
const router = useRouter();
const route = useRoute();
const isTablet = ref(window.innerWidth <= 768);
const isSmallScreen = ref(window.innerWidth <= 600);

const updateScreen = () => {
  isTablet.value = window.innerWidth <= 768;
  isSmallScreen.value = window.innerWidth <= 600;
};

onUnmounted(() => {
  window.removeEventListener('resize', updateScreen);
});

const checkManager = () => {
  if (localStorage.getItem('userEmail') === mangaerEmail.value) {
    isManager.value = true;
  }
};

onMounted(() => {
  window.addEventListener('resize', updateScreen);
  checkManager();
  if (route.path === '/manager') {
    router.push('/manager/invite');
  }
});

function goHome() {
  router.push('/');
}
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
  background-color: #f5f7fa;
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
  width: 100%;
  display: flex;
  flex-direction: column;
}

.nav-links {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  border-bottom: 1px solid #e4e7ed;
  background: #fff;
  width: 100%;

  @media screen and (min-width: 769px) {
    justify-content: flex-start; /* Adjust based on sidebar width */
  }
}

.nav-link {
  text-align: center;
  padding: 0 12px;
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  color: #106a52;
  border-radius: 6px 6px 0 0;
  margin: 0 4px;
  height: 100%;
  display: flex;
  align-items: center;
  transition:
    all 0.3s,
    box-shadow 0.2s;
  position: relative;
}

.nav-link:hover {
  color: #106a52;
}

.nav-link.router-link-active {
  color: #106a52;
  font-weight: bold;
}

.nav-link.router-link-active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: -4px;
  right: -4px;
  background-color: #106a52;
  border-bottom: 2px solid #106a52;
}

.content-area {
  flex: 1;
  height: 100%;
  background: #fff;
  border-radius: 0 0 8px 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.06);
  overflow: auto;
}

.not-manager {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  font-size: 24px;
  color: #666;
  background: #fff;
}

.menu {
  font-size: 26px;
  color: #106a52;
  cursor: pointer;
}
</style>
