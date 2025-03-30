<template>
  <div class="login-container">
    <!-- 品牌Logo -->
    <h1 class="brand-logo">Elevater</h1>

    <!-- 登录表单容器 -->
    <div class="login-card">
      <!-- 表单标题 -->
      <h2 class="form-title">Login Page</h2>

      <!-- 使用@submit.prevent阻止默认提交 -->
      <form @submit.prevent="handleSubmit">
        <!-- 邮箱输入 -->
        <div class="input-group">
          <input type="email" v-model.trim="formData.email" placeholder="Enter you email" class="form-control" required
            autocomplete="username" />
        </div>

        <!-- 密码输入 -->
        <div class="input-group">
          <input type="password" v-model.trim="formData.password" placeholder="Enter your password" class="form-control" required
            autocomplete="current-password" />
        </div>

        <!-- 提交按钮 -->
        <button type="submit" class="submit-btn">
          Sign in
        </button>
      </form>

      <!-- 辅助链接 -->
      <div class="auth-links">
        <RouterLink to="/forgot" class="link">Forgot password?</RouterLink>
        <RouterLink to="/register" class="link">Sign up</RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'

// 初始化路由
const router = useRouter()

// 响应式表单数据（使用reactive替代多个ref）
const formData = reactive({
  email: '',
  password: ''
})

// 表单提交处理
const handleSubmit = async () => {
  try {
    // 执行登录逻辑
    const response = await fetch('/api/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(formData)
    })

    if (response.ok) {
      // 登录成功后跳转到主页
      router.push('/dashboard')
    } else {
      // 处理错误情况
      console.error('Login failed')
    }
  } catch (error) {
    console.error('Error:', error)
  }
}
</script>

<style scoped>
/* 容器样式 */
.login-container {
  min-height: 100vh;
  display: grid;
  place-items: center;
  background: #f8f9fa;
  padding: 2rem;
}

/* 品牌标识 */
.brand-logo {
  color: #e1251b;
  font-size: 3.5rem;
  text-align: center;
  margin-bottom: 2.5rem;
}

/* 登录卡片 */
.login-card {
  background: white;
  width: min(90%, 600px);
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

/* 表单标题 */
.form-title {
  font-size: 1.5rem;
  color: #333;
  text-align: center;
  margin-bottom: 2rem;
}

/* 输入组样式 */
.input-group {
  margin-bottom: 1.5rem;
}

.form-control {
  width: 100%;
  padding: 0.75rem;
  border: 2px solid #e9ecef;
  border-radius: 6px;
  transition: border-color 0.3s ease;
}

.form-control:focus {
  border-color: #e1251b;
  outline: none;
}

/* 提交按钮 */
.submit-btn {
  width: 100%;
  padding: 0.75rem;
  background: #e1251b;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 1rem;
  cursor: pointer;
  transition:
    background 0.3s ease,
    transform 0.1s ease;
}

.submit-btn:hover {
  background: #c82333;
}

.submit-btn:active {
  transform: scale(0.98);
}

/* 辅助链接 */
.auth-links {
  margin-top: 1.5rem;
  display: flex;
  justify-content: space-between;
}

.link {
  color: #6c757d;
  font-size: 0.875rem;
  text-decoration: none;
  transition: color 0.3s ease;
}

.link:hover {
  color: #e1251b;
}
</style>

<style scoped>
/* 视口基础设置 */
.login-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  /* 新增垂直居中 */
  padding: 20px;
  background: #f5f5f5;
}



/* 移动端适配 */
@media (max-width: 768px) {
  .login-container {
    padding: 10px;
    justify-content: flex-start;
    /* 顶部对齐 */
    padding-top: 20vh;
    /* 视觉居中 */
  }

  .jd-logo {
    font-size: 36px;
    margin-bottom: 20px;
  }

  .login-box {
    width: 95%;
    padding: 20px;
    box-shadow: none;
    border: 1px solid #ddd;
    /* 移动端更简洁的边框 */
  }

  .form-input {
    height: 45px;
    /* 增大触摸区域 */
    font-size: 16px;
  }

  .login-btn {
    height: 45px;
    font-size: 18px;
  }

  .helper-links {
    flex-direction: column;
    /* 垂直排列链接 */
    gap: 10px;
    align-items: center;
  }
}

/* 超小屏幕适配 */
@media (max-width: 480px) {
  .login-container {
    padding: 10px;
    justify-content: flex-start;
    /* 顶部对齐 */
    padding-top: 20vh;
    /* 视觉居中 */
  }

  .login-title {
    font-size: 20px;
  }

  .form-input {
    font-size: 14px;
  }

  .link {
    font-size: 12px;
  }
}

/* 添加大屏幕适配 */
@media (min-width: 1200px) {
  .login-card {
    width: 50%;
    /* 在大屏幕上使用相对宽度 */
    padding: 3rem;
    /* 增大内边距 */
    border-radius: 16px;
    /* 增强视觉效果 */
  }

  .form-title {
    font-size: 2rem;
    /* 增大标题字号 */
  }

  .form-control {
    padding: 1rem;
    /* 增大输入框尺寸 */
    font-size: 1.1rem;
  }
}
</style>