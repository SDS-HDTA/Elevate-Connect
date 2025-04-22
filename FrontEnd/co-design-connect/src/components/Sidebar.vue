<template>
  <div class="sidebar">
    <el-menu
      :default-active="activeMenu"
      class="menu"
      @select="handleSelect"
    >
      <el-menu-item index="home">
        <el-icon><House /></el-icon>
        <span>Home</span>
      </el-menu-item>
      <el-menu-item index="projects">
        <el-icon><Folder /></el-icon>
        <span>Projects</span>
      </el-menu-item>
      <el-menu-item index="tasks">
        <el-icon><List /></el-icon>
        <span>Tasks</span>
      </el-menu-item>
      <el-menu-item index="settings">
        <el-icon><Setting /></el-icon>
        <span>Settings</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { House, Folder, List, Setting } from '@element-plus/icons-vue'

const activeMenu = ref('home')

const emit = defineEmits(['tab-change'])

const handleSelect = (index) => {
  activeMenu.value = index
  emit('tab-change', index)
}
</script>

<style scoped>
.sidebar {
  width: 200px;
  height: 100vh;
  background-color: #fff;
  border-right: 1px solid #e6e6e6;
}

.menu {
  border-right: none;
}
</style> 