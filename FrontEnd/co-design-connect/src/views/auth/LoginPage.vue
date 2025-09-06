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
              <div class="input-with-icon input-group-with-bottom">
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
              <div class="auth-links">
                <RouterLink to="/reset-password" class="link"
                  >Forgot Password?</RouterLink
                >
                <RouterLink to="/register" class="link"
                  >Have a code?</RouterLink
                >
              </div>
              <el-button class="btn-primary">Login</el-button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { View, Lock, Message, ArrowLeft } from '@element-plus/icons-vue';
import Header from '@/components/Header.vue';
import request from '@/utils/request';

// 初始化路由
const router = useRouter();
const showPassword = ref(false);

// 响应式表单数据（使用reactive替代多个ref）
const formData = reactive({
  email: '',
  password: '',
});

// 表单提交处理
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
      //保存信息并跳转主页
      localStorage.setItem('token', res.data.accessToken);
      localStorage.setItem('userId', res.data.id);
      router.push('/');
    } else {
      alert(res.message || 'Failed to login, please try again');
    }
  } catch (error) {
    console.error('Failed to login:', error);
    alert(error.message || 'Failed to login, please try again');
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

.sidebar {
  position: fixed;
  left: 0;
  top: 60px;
  bottom: 0;
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

/* 品牌标识 */
.brand-logo {
  color: var(--color-primary);
  font-size: 3.5rem;
  text-align: center;
}

/* 登录卡片 */
.login-card {
  width: min(90%, 520px);
  padding: 1rem;
}

/* 表单标题 */
.form-title {
  font-size: 2rem;
  color: var(--color-dark);
  text-align: center;
}

.input-with-icon {
  position: relative;
  width: 100%;
}

.input-with-icon .form-control {
  width: 100%;
  padding: 0.75rem 0.75rem 0.75rem 2.5rem; /* extra left padding for icon */
  border-radius: 6px;
  transition: border-color 0.3s ease;
}

.input-with-icon .form-control:focus {
  outline: none;
}

.input-with-icon .input-icon {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 1.2rem;
  color: var(--color-light);
  pointer-events: none;
}

.input-with-icon .form-control:focus + .input-icon,
.input-with-icon .input-icon:focus {
  color: var(--color-primary);
}

.input-group {
  width: 100%;
}

.input-group-with-bottom {
  width: 100%;
  margin-bottom: 1.5rem;
}

.password-group {
  position: relative;
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

.form-control {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  transition: border-color 0.3s ease;
}

.password-group .form-control {
  padding-right: 70px;
}

.form-control:focus {
  border: 1px solid var(--color-primary);
  outline: none;
}

/* 辅助链接 */
.auth-links {
  display: flex;
  width: 100%;
  justify-content: space-between;
  margin-bottom: 1.5rem;
  margin-top: 1rem;
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

/* 移动端适配 */
@media (max-width: 768px) {
  .form-control {
    padding: 1rem;
  }
}
</style>
