<template>
  <div class="header">
    <div class="logo-container">
      <img src="/logo.png" class="logo" />
      <span v-if="!isTablet">Elevate Connect</span>
    </div>
    <div class="user-info">
      <el-dropdown @command="handleCommand">
        <el-icon class="menu">
          <Grid />
        </el-icon>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item v-if="!userInfo" @click="router.push('/login')">
              Sign in
            </el-dropdown-item>
            <el-dropdown-item v-else command="profile"
              >Profile</el-dropdown-item
            >
            <el-dropdown-item v-else command="logout" divided
              >Logout</el-dropdown-item
            >
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, onUnmounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import request from "@/utils/request";
import { Grid } from "@element-plus/icons-vue";

const router = useRouter();
const userInfo = ref(null);
const isTablet = ref(window.innerWidth <= 768);

const updateScreen = () => {
  isTablet.value = window.innerWidth <= 768;
};

const getUserInfo = async () => {
  try {
    const userId = localStorage.getItem("userId");
    const res = await request.get(`/user/info?userId=${userId}`);
    if (res.code === 1) {
      userInfo.value = res.data;
      localStorage.setItem("username", res.data.username);
      localStorage.setItem("userEmail", res.data.email);
    }
  } catch (error) {
    console.error("Failed to fetch user info:", error);
    userInfo.value = null;
  }
};

const handleCommand = async (command) => {
  if (command === "profile") {
    router.push(`/profile/${userInfo.value.id}`);
  } else if (command === "logout") {
    try {
      // 清除本地存储的用户信息
      localStorage.removeItem("userId");
      localStorage.removeItem("token");
      localStorage.removeItem("username");
      localStorage.removeItem("userEmail");
      // 清除用户信息
      userInfo.value = null;
      // 显示成功消息
      ElMessage.success("Logout successfully");
      // 跳转到登录页
      router.push("/");
    } catch (error) {
      console.error("Logout failed:", error);
      ElMessage.error("Logout failed");
    }
  }
};

onMounted(() => {
  getUserInfo();
  window.addEventListener("resize", updateScreen);
});

onUnmounted(() => {
  userInfo.value = null;
  window.removeEventListener("resize", updateScreen);
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

.user-info {
  display: flex;
  align-items: center;
}

.user-link {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #333;
}

.login-link {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #333;
}

.menu {
  font-size: 26px;
  cursor: pointer;
  color: #106a52;
}

.logo {
  width: 40px;
  height: 40px;
  margin-right: 10px;
}

.logo-container {
  display: flex;
  flex-direction: row;
  align-items: center;
}
</style>
