<template>
  <div class="header">
    <div class="logo">
      <h1>Co-Design-Connect</h1>
    </div>
    <div class="user-info">
      <router-link to="/profile" class="user-link" v-if="userInfo">
        <el-avatar :size="32" :icon="UserFilled" />
        <span class="username">{{ userInfo.username }}</span>
      </router-link>
      <router-link to="/login" class="login-link" v-else>
        <el-button type="text">Sign in</el-button>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { UserFilled } from '@element-plus/icons-vue'
import request from '@/utils/request'

// 用户信息
const userInfo = ref(null)

// 获取用户信息的方法
const getUserInfo = async () => {
  try {
    const userId = localStorage.getItem('userId')
    const res = await request.get(`/user/info?userId=${userId}`)
    if (res.code === 1) {
      userInfo.value = res.data
    }
  } catch (error) {
    console.error('Failed to fetch user info:', error)
    userInfo.value = null
  }
}

onMounted(() => {
  getUserInfo()
})
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

.logo h1 {
  color: #e74c3c;
  margin: 0;
  font-size: 24px;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-link, .login-link {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #333;
}

.username {
  margin-left: 8px;
}
</style> 