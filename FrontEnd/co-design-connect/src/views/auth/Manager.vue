<template>
  <div class="home-page">
    <Header class="header" />
    <div class="main-content">
      <Sidebar class="sidebar" />
      <div class="content">
        <div class="manager-container">
          <div v-if="!hasPermission" class="no-permission">
            <el-result
              icon="error"
              title="无访问权限"
              sub-title="您没有权限访问此页面"
            >
              <template #extra>
                <el-button type="primary" @click="$router.push('/')">返回首页</el-button>
              </template>
            </el-result>
          </div>
          <div v-else>
            <!-- 自定义Tab栏 -->
            <div class="manager-nav">
              <router-link to="/manager/invitation" class="nav-link">Invitation</router-link>
              <router-link to="/manager/user" class="nav-link">User management</router-link>
              <router-link to="/manager/project" class="nav-link">Project Management</router-link>
            </div>
            <!-- 子页面内容 -->
            <div class="router-area">
              <router-view />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import Header from '@/components/Header.vue'
import Sidebar from '@/components/Sidebar.vue'

const router = useRouter()
const route = useRoute()
const hasPermission = ref(false)

// 允许访问的管理员邮箱列表
const adminEmails = [
  'testfz@qq.com',
]

onMounted(async () => {
  try {
    const userEmail = localStorage.getItem('userEmail')
    if (adminEmails.includes(userEmail)) {
      hasPermission.value = true
      // 如果刚进来在/manager，自动跳转到/manager/invitation
      if (route.path === '/manager') {
        router.replace('/manager/invitation')
      }
    } else {
      hasPermission.value = false
    }
  } catch (error) {
    console.error('验证权限时出错:', error)
    hasPermission.value = false
  }
})

// tab切换
function goTab(path) {
  if (route.path !== path) {
    router.push(path)
  }
}
function isActive(path) {
  return route.path === path
}
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
  flex-direction: row;
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
  height: 100vh;
}
.manager-container {
  height: 100vh;
  width: 100%;
  padding: 20px;
}
.manager-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.router-area {
  padding: 20px;
  height: 100%; 
}
.logo {
  font-size: 2rem;
  color: #e74c3c;
  font-family: 'Comic Sans MS', cursive, sans-serif;
  font-weight: bold;
}
.avatar {
  margin-right: 20px;
}
.manager-nav {
  margin-bottom: 16px;
  display: flex;
  justify-content: space-between;
  max-width: 800px;
}
.no-permission {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.nav-link {
  padding: 12px 24px;
  text-decoration: none;
  color: #303133;
  border-radius: 4px;
  transition: all 0.3s;
  text-align: center;
  flex: 1;
  margin: 0 8px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 0.5px;
  border-bottom: 2px solid transparent;
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.nav-link:hover {
  color: #409EFF;
  background-color: #f0f7ff;
  border-bottom: 2px solid #409EFF;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
}

.nav-link.router-link-active {
  color: #409EFF;
  background-color: #f0f7ff;
  border-bottom: 2px solid #409EFF;
  font-weight: 700;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
}
</style>
