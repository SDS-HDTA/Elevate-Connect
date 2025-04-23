<template>
  <div class="home-page">
    <Header class="header" />
    <div class="main-content">
      <Sidebar class="sidebar" />
      <div class="content">
        <div class="tasks-container">
          <h1>Tasks Page</h1>
          <p>This page is under construction.</p>
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

.tasks-container {
  padding: 2rem;
  text-align: center;
}

h1 {
  color: #2c3e50;
  font-size: 2rem;
  margin-bottom: 1rem;
}

p {
  color: #5e6d82;
  font-size: 1.1rem;
}
</style> 