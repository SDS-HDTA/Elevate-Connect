<template>
  <div class="home-page">
    <Header class="header" />
    <div class="main-content">
      <Sidebar class="sidebar" />
      <div class="content">
        <div class="project-container">
          <router-view v-if="isLoggedIn"></router-view>
          <NotLoggedIn v-else />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Header from '@/components/Header.vue'
import Sidebar from '@/components/Sidebar.vue'
import NotLoggedIn from '@/components/NotLoggedIn.vue'
const isLoggedIn = ref(false)

// 检查登录状态
const checkLoginStatus = () => {
  const token = localStorage.getItem('token')
  isLoggedIn.value = !!token
}

onMounted(() => {
  checkLoginStatus()
})
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
  min-height: calc(100vh - 60px);
}

.sidebar {
  flex: 0 0 20%;
  background-color: #fff;
  border-right: 1px solid #e6e6e6;
  min-width: 150px;
}

.content {
  flex: 0 0 80%;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
  padding: 20px;
}

.project-container {
  height: 100%;
  max-width: 1200px;
  margin: 0 auto;
}
</style> 