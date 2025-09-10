<template>
  <div class="sidebar">
    <el-menu :default-active="activeMenu" class="menu" router>
      <el-menu-item index="/my-projects">
        <el-icon><Folder /></el-icon>
        <span>My Projects</span>
      </el-menu-item>
      <el-menu-item index="/discover">
        <el-icon><Compass /></el-icon>
        <span>Discover</span>
      </el-menu-item>
      <el-menu-item v-if="userType === '0'" index="/manager/invite">
        <el-icon><Setting /></el-icon>
        <span>Manager View</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { Folder, Setting, Compass } from '@element-plus/icons-vue';

defineProps({
  userType: {
    type: String,
    required: true,
  },
});

const route = useRoute();

const activeMenu = computed(() => {
  if (route.path.startsWith('/my-projects')) {
    return '/my-projects';
  }
  if (route.path.startsWith('/manager')) {
    return '/manager/invite';
  }
  return route.path;
});
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
