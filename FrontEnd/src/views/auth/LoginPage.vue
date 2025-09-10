<template>
  <div class="login-page">
    <Header class="header" />
    <div class="main-content">
      <div class="login-container">
        <div class="back-button-container">
          <div class="back-button" @click="$router.push('/')">
            <el-icon><ArrowLeft /></el-icon>
            <span>Back</span>
          </div>
        </div>
        <h1 class="form-title">Log in</h1>

        <div class="login-card">
          <form @submit.prevent="handleSubmit">
            <div class="login-content">
              <div class="input-with-icon input-group mb-4">
                <el-icon class="input-icon"><Message /></el-icon>
                <input
                  type="email"
                  v-model.trim="formData.email"
                  placeholder="Email address"
                  class="form-control"
                  required
                  autocomplete="username"
                />
              </div>
              <div class="input-with-icon password-group">
                <el-icon class="input-icon"><Lock /></el-icon>
                <input
                  :type="showPassword ? 'text' : 'password'"
                  v-model.trim="formData.password"
                  placeholder="Password"
                  class="form-control"
                  required
                  autocomplete="current-password"
                />
                <button
                  type="button"
                  :class="showPassword ? 'password-toggled' : 'password-toggle'"
                  @click="showPassword = !showPassword"
                >
                  <el-icon class="view-icon"><View /></el-icon>
                </button>
              </div>
              <div class="auth-links mt-3 mb-4">
                <RouterLink to="/reset-password" class="link"
                  >Forgot Password?</RouterLink
                >
                <RouterLink to="/register" class="link"
                  >Have a code?</RouterLink
                >
              </div>
              <el-button native-type="submit" class="btn-primary"
                >Login</el-button
              >
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { View, Lock, Message, ArrowLeft } from '@element-plus/icons-vue';
import Header from '@/components/Header.vue';
import request from '@/utils/request';
import { useUserStore } from '@/stores/userStore';

// Initialize router
const router = useRouter();
const showPassword = ref(false);
const userStore = useUserStore();

// Reactive form data (using reactive instead of multiple refs)
const formData = reactive({
  email: '',
  password: '',
});

// Form submission handling
const handleSubmit = async () => {
  try {
    const params = new URLSearchParams();
    params.append('email', formData.email);
    params.append('password', formData.password);

    const res = await request.post('/login', params, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });

    if (res.code === 1) {
      userStore.setUserInfo(res.data);
      router.push('/my-projects');
    } else {
      alert(res.message || 'Failed to login, please try again');
    }
  } catch (error) {
    console.error('Failed to login:', error);
    alert(error.message || 'Failed to login, please try again');
  }
};

onMounted(async () => {
  await userStore.getUserInfo();

  if (userStore.userInfo) {
    router.push('/my-projects');
  }
});
</script>

<style scoped>
.login-page {
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
  margin-top: 60px;
  overflow-y: auto;
}

.login-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: var(--color-background-light);
  min-height: calc(100vh - 60px);
}

.login-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.login-card {
  width: min(90%, 520px);
  padding: 1rem;
}

.form-title {
  font-size: 2rem;
  color: var(--color-dark);
  text-align: center;
}

.password-toggle {
  position: absolute;
  right: 12px;
  top: 55%;
  transform: translateY(-50%);
  color: var(--color-light);
  transition: all 0.3s ease;
}

.password-toggle:hover {
  color: var(--color-dark);
}

.password-toggled {
  position: absolute;
  right: 12px;
  top: 55%;
  transform: translateY(-50%);
  transition: all 0.3s ease;
  color: var(--color-secondary);
}

.password-toggled:hover {
  color: var(--color-primary);
}

.auth-links {
  display: flex;
  width: 100%;
  justify-content: space-between;
}

.link {
  color: var(--color-primary);
  font-size: 0.875rem;
  text-decoration: underline;
  transition: color 0.3s ease;
}

.link:hover {
  color: #282d38;
}

.view-icon {
  font-size: 1.2rem;
}

.back-button-container {
  display: flex;
  align-items: flex-start;
  width: min(90%, 520px);
  padding-left: 1rem;
}
</style>
