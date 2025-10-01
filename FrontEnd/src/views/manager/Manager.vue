<template>
  <div class="manager-page">
    <Header class="header" />
    <div class="main-content">
      <Sidebar v-if="!isTablet" class="sidebar" />
      <div class="content">
        <div class="manager">
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
              >Community Management</router-link
            >
          </div>
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

onMounted(async () => {
  window.addEventListener('resize', updateScreen);

  if (route.path === '/manager') {
    router.push('/manager/users');
  }
});
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
</style>
