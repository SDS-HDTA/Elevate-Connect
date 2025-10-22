<template>
  <div class="sidebar">
    <el-menu :default-active="activeMenu" class="menu" router>
      <el-menu-item index="/my-projects">
        <el-icon><Folder /></el-icon>
        <span>{{ userRole === 3 ? 'All' : 'My' }} Projects</span>
      </el-menu-item>
      <el-menu-item
        v-if="
          permissionStore.hasPermission(permissions.AccessDiscover) &&
          userRole !== 3
        "
        index="/discover"
      >
        <el-icon><Compass /></el-icon>
        <span>Discover</span>
      </el-menu-item>
      <el-menu-item
        v-if="permissionStore.hasPermission(permissions.AdminAllPermissions)"
        index="/manager/users"
      >
        <el-icon><Setting /></el-icon>
        <span>Admin Panel</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { Folder, Setting, Compass } from '@element-plus/icons-vue';
import { permissions } from '@/models/permission';
import { usePermissionStore } from '@/stores/permissionStore';
import { useUserStore } from '@/stores/userStore';

const route = useRoute();
const permissionStore = usePermissionStore();
const userStore = useUserStore();
const userRole = computed(() => {
  const t = userStore.userInfo?.role ?? 0;
  return Number(t);
});

const activeMenu = computed(() => {
  if (route.path.startsWith('/my-projects')) {
    return '/my-projects';
  }
  if (route.path.startsWith('/manager')) {
    return '/manager/users';
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
