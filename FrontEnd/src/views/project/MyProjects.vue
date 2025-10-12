<template>
  <div class="home-page">
    <Header class="header" />
    <div class="main-content">
      <Sidebar
        v-if="
          !isTablet && permissionStore.hasPermission(permissions.AccessDiscover)
        "
        class="sidebar"
      />
      <div
        :class="
          'content' +
          (!permissionStore.hasPermission(permissions.AccessDiscover)
            ? ' ms-0'
            : '')
        "
      >
        <div class="project-container">
          <router-view></router-view>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import Header from '@/components/Header.vue';
import Sidebar from '@/components/Sidebar.vue';
import { usePermissionStore } from '@/stores/permissionStore';
import { permissions } from '@/models/permission';
const isTablet = ref(window.innerWidth <= 768);
const isSmallScreen = ref(window.innerWidth <= 600);
const permissionStore = usePermissionStore();

const updateScreen = () => {
  isTablet.value = window.innerWidth <= 768;
  isSmallScreen.value = window.innerWidth <= 600;
};

onMounted(async () => {
  window.addEventListener('resize', updateScreen);
});

onUnmounted(() => {
  window.removeEventListener('resize', updateScreen);
});
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  width: 100vw;
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
  margin-top: 60px;
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
