`<template>
    <div class="home-page">
      <Header class="header" />
      <div class="main-content">
        <Sidebar class="sidebar" @tab-change="handleTabChange" />
        <div class="content">
          <AllProjects v-if="currentTab === 'home'" />
          <MyProjects v-else-if="currentTab === 'projects'" />
          <div v-else-if="currentTab === 'tasks'" class="coming-soon">
            <el-empty description="Tasks feature coming soon" />
          </div>
          <div v-else-if="currentTab === 'settings'" class="coming-soon">
            <el-empty description="Settings feature coming soon" />
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { ElMessage } from 'element-plus'
  import Header from '@/components/Header.vue'
  import Sidebar from '@/components/Sidebar.vue'
  import AllProjects from '@/components/AllProjects.vue'
  import MyProjects from '@/components/MyProjects.vue'
  
  const currentTab = ref('home')
  
  const handleTabChange = (tab) => {
    currentTab.value = tab
  }
  
  // 在组件挂载时初始化项目列表
  onMounted(() => {
    // 可以在这里添加全局的错误处理
    window.addEventListener('unhandledrejection', (event) => {
      ElMessage.error('An error occurred. Please try again later.')
      console.error('Unhandled promise rejection:', event.reason)
    })
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
  
  .coming-soon {
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 40px;
  }
  </style>`