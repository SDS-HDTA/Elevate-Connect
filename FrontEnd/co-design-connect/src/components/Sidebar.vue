<template>
  <div class="sidebar">
    <el-menu
      :default-active="activeMenu"
      class="menu"
      router
    >
      <el-menu-item index="/">
        <el-icon><House /></el-icon>
        <span>Home</span>
      </el-menu-item>
      <el-menu-item index="/my-projects">
        <el-icon><Folder /></el-icon>
        <span>My Projects</span>
      </el-menu-item>
      <el-menu-item index="/tasks">
        <el-icon><List /></el-icon>
        <span>Tasks</span>
      </el-menu-item>
      <el-menu-item index="/settings">
        <el-icon><Setting /></el-icon>
        <span>Settings</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { House, Folder, List, Setting } from '@element-plus/icons-vue'

const route = useRoute()
const activeMenu = computed(() => 
  route.path.startsWith('/my-projects') ? '/my-projects' : route.path
)
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