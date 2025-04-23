<template>
  <div class="home-page">
    <Header class="header" />
    <div class="main-content">
      <Sidebar class="sidebar" />
      <div class="content">
        <div class="project-container">
          <router-view></router-view>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import Header from '@/components/Header.vue'
import Sidebar from '@/components/Sidebar.vue'
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
  margin-left: 200px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.project-container {
  height: 100%;
  padding: 20px;
}
</style> 