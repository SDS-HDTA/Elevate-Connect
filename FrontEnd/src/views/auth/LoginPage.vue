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
          <el-form
            :model="formData"
            :rules="rules"
            v-loading="loading"
            ref="formRef"
            label-width="120px"
          >
            <el-form-item label="Email" prop="email">
              <el-input
                autocomplete="username"
                placeholder="Email Address"
                v-model="formData.email"
              />
            </el-form-item>
            <el-form-item label="Password" prop="password">
              <el-input
                :type="showPassword ? 'text' : 'password'"
                v-model="formData.password"
                placeholder="Password"
                class="form-control"
                required
              />
              <button
                type="button"
                :class="showPassword ? 'password-toggled' : 'password-toggle'"
                @click="showPassword = !showPassword"
              >
                <el-icon class="view-icon"><View /></el-icon>
              </button>
            </el-form-item>
          </el-form>
          <div class="auth-links mt-3 mb-4">
            <RouterLink to="/reset-password" class="link"
              >Forgot Password?</RouterLink
            >
            <RouterLink to="/register" class="link">Have a code?</RouterLink>
          </div>
          <div class="flex justify-content-center">
            <el-button @click="handleSubmit" class="btn-primary"
              >Login</el-button
            >
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { View, ArrowLeft } from '@element-plus/icons-vue';
import Header from '@/components/Header.vue';
import request from '@/utils/request';
import { useUserStore } from '@/stores/userStore';

// Initialize router
const router = useRouter();
const showPassword = ref(false);
const userStore = useUserStore();
const loading = ref(false);
const formRef = ref(null);

const formData = reactive({
  email: '',
  password: '',
});

const rules = {
  email: [
    { required: true, message: 'Required field', trigger: 'blur' },
    { type: 'email', message: 'Invalid email', trigger: 'blur' },
  ],
  password: [{ required: true, message: 'Required field', trigger: 'blur' }],
};

// Form submission handling
const handleSubmit = async () => {
  if (!formRef.value) return;

  const valid = await formRef.value.validate();
  if (!valid) return;

  loading.value = true;
  try {
    const params = new URLSearchParams();
    params.append('email', formData.email);
    params.append('password', formData.password);

    const { token } = await request.post('/login', params, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });

    if (token) {
      await userStore.setUserInfo(token);
      router.replace('/my-projects');
    } else {
      alert('Failed to login, please try again');
    }
  } catch (error) {
    console.error('Failed to login:', error);
    alert(error.message || 'Failed to login, please try again');
  } finally {
    loading.value = false;
  }
};
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

.back-button-container {
  display: flex;
  align-items: flex-start;
  width: min(90%, 520px);
  padding-left: 1rem;
}
</style>
