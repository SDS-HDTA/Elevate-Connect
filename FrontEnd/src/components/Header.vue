<template>
  <div class="header" :class="!showLogo ? 'no-logo-header' : ''">
    <div
      v-if="showLogo"
      class="logo-container"
      @click="
        userStore.userInfo ? router.push('/my-projects') : router.push('/')
      "
    >
      <img alt="Elevate Connect Logo" src="/logo.png" class="logo" />
      <span class="app-name" v-if="!isTablet">Elevate Connect</span>
    </div>
    <div class="user-info">
      <div v-if="!isTablet">
        <el-dropdown
          v-if="userStore.userInfo"
          trigger="click"
          @command="handleCommand"
          :show-timeout="0"
          :hide-timeout="0"
        >
          <div v-if="userStore.userInfo" class="user-link">
            <Avatar :username="userName" :size="32" />
            <span class="username">{{ userName }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="userStore.logout">
                Logout
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
      <el-dropdown
        v-if="isTablet || !userStore.userInfo"
        trigger="click"
        @command="handleCommand"
        :show-timeout="0"
        :hide-timeout="0"
      >
        <span class="menu" role="button" tabindex="0" @click.stop>
          <el-icon>
            <Grid />
          </el-icon>
        </span>

        <template #dropdown>
          <el-dropdown-menu v-if="!userStore.userInfo">
            <el-dropdown-item>
              <a
                href="mailto:admin@elevateprograms.org?subject=Elevate%20Connect%20Support"
                >Contact Us</a
              >
            </el-dropdown-item>
            <el-dropdown-item divided>
              <a
                href="https://elevateprograms.org/who-we-are/"
                target="_blank"
                rel="noopener noreferrer"
                >About Us</a
              >
            </el-dropdown-item>
          </el-dropdown-menu>

          <el-dropdown-menu v-else>
            <el-dropdown-item command="profile">
              <div v-if="userStore.userInfo" class="user-link">
                <Avatar :username="userName" :size="32" />
                <span class="username">{{ userName }}</span>
              </div>
            </el-dropdown-item>
            <el-dropdown-item @click="router.push('/discover')" divided>
              Discover
            </el-dropdown-item>
            <el-dropdown-item @click="router.push('/my-projects')" divided>
              My Projects
            </el-dropdown-item>
            <el-dropdown-item
              v-if="userType === 0"
              @click="router.push('/manager-view')"
              divided
            >
              Manager View
            </el-dropdown-item>
            <el-dropdown-item v-if="userType !== 0" divided>
              <a
                href="mailto:admin@elevateprograms.org?subject=Elevate%20Connect%20Support"
                >Contact Us</a
              >
            </el-dropdown-item>
            <el-dropdown-item @click="userStore.logout" divided>
              Logout
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import Avatar from './Avatar.vue';
import { Grid } from '@element-plus/icons-vue';
import { useUserStore } from '@/stores/userStore';

defineProps({
  showLogo: {
    type: Boolean,
    default: true,
  },
});

const router = useRouter();
const isTablet = ref(window.innerWidth <= 768);
const userStore = useUserStore();
const userName = computed(() => userStore.userInfo?.username || '');
const userType = computed(() => {
  const t = userStore.userInfo?.type ?? 1;
  return Number(t);
});

const updateScreen = () => {
  isTablet.value = window.innerWidth <= 768;
};

onMounted(() => {
  window.addEventListener('resize', updateScreen);
});

onUnmounted(() => {
  window.removeEventListener('resize', updateScreen);
});
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.no-logo-header {
  justify-content: flex-end;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-link {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: var(--color-dark);
}

.menu {
  font-size: 26px;
  cursor: pointer;
  color: var(--color-primary);
}

.logo {
  width: 40px;
  height: 40px;
  margin-right: 15px;
}

.logo-container {
  cursor: pointer;
  display: flex;
  flex-direction: row;
  align-items: center;
}

.app-name {
  font-size: 18px;
  color: var(--color-primary);
}

.username {
  margin-left: 10px;
}
</style>
