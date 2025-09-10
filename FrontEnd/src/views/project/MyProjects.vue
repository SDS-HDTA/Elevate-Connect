<template>
  <div class="home-page">
    <Header :user-info="userStore.userInfo" class="header" />
    <div class="main-content">
      <Sidebar :user-type="userType" v-if="!isTablet" class="sidebar" />
      <div class="content">
        <div class="project-container">
          <router-view></router-view>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue';
import Header from '@/components/Header.vue';
import Sidebar from '@/components/Sidebar.vue';
import { useUserStore } from '@/stores/userStore';
const isTablet = ref(window.innerWidth <= 768);
const isSmallScreen = ref(window.innerWidth <= 600);
const userStore = useUserStore();
const userType = computed(() => {
  const t = userStore.userInfo?.type ?? '1';
  return String(t);
});

const updateScreen = () => {
  isTablet.value = window.innerWidth <= 768;
  isSmallScreen.value = window.innerWidth <= 600;
};

onMounted(() => {
  window.addEventListener('resize', updateScreen);
});

onUnmounted(() => {
  window.removeEventListener('resize', updateScreen);
});

onMounted(async () => {
  await userStore.getUserInfo();
});
</script>

<style scoped>
.home-page {
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
  flex-direction: row;
  margin-top: 60px;
  flex: 1;
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
  height: calc(100vh - 60px);

  @media screen and (min-width: 769px) {
    margin-left: 200px; /* Adjust based on sidebar width */
  }
}

.project-container {
  height: 100%;
  width: 100%;
  margin: 0 auto;
}
</style>
