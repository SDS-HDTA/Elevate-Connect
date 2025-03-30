<template>
  <div class="register-container">
    <!-- 品牌Logo -->
    <h1 class="brand-logo">JD</h1>

    <!-- 注册表单容器 -->
    <div class="register-card">
      <!-- 表单标题 -->
      <h2 class="form-title">Register Page</h2>

      <form @submit.prevent="handleSubmit">
        <!-- 邮箱输入 -->
        <div class="input-group">
          <input type="email" v-model.trim="formData.email" placeholder="Enter your email" class="form-control" required
            autocomplete="username" />
        </div>

        <!-- 密码输入 -->
        <div class="input-group">
          <input type="password" v-model.trim="formData.password" placeholder="Please set your password"
            class="form-control" required autocomplete="new-password" />
          <button type="button" class="password-toggle" @click="showPassword = !showPassword">
            <!-- {{ showPassword ? 'Hide' : 'Show' }} password -->
          </button>
        </div>

        <!-- 确认密码 -->
        <div class="input-group">
          <input type="password" v-model.trim="formData.confirmPassword" placeholder="Enter password" class="form-control"
            required autocomplete="new-password" />
        </div>

        <!-- 邀请码 -->
        <div class="input-group">
          <input type="text" v-model.trim="formData.inviteCode" placeholder="Promotion code" class="form-control" />
        </div>

        <!-- 提交按钮 -->
        <button type="submit" class="submit-btn">
          立即注册
        </button>
      </form>

      <!-- 辅助链接 -->
      <div class="auth-links">
        <RouterLink to="/login" class="link">已有账号？立即登录</RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const showPassword = ref(false)
const showConfirmPassword = ref(false)

const formData = reactive({
  email: '',
  password: '',
  confirmPassword: '',
  inviteCode: ''
})

const validateForm = () => {
  // 密码一致性验证
  if (formData.password !== formData.confirmPassword) {
    alert('两次输入的密码不一致')
    return false
  }

  // 密码复杂度验证
  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,20}$/
  if (!passwordRegex.test(formData.password)) {
    alert('密码需包含字母和数字，长度8-20位')
    return false
  }

  return true
}

const handleSubmit = async () => {
  if (!validateForm()) return

  try {
    const response = await fetch('/api/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        email: formData.email,
        password: formData.password,
        invite_code: formData.inviteCode
      })
    })

    if (response.ok) {
      router.push('/login?register=success')
    } else {
      const errorData = await response.json()
      alert(errorData.message || '注册失败')
    }
  } catch (error) {
    console.error('请求失败:', error)
    alert('网络错误，请稍后重试')
  }
}
</script>

<style scoped>
/* 注册页面特定样式 */
.register-container {
  min-height: 100vh;
  display: grid;
  place-items: center;
  background: #f8f9fa;
  padding: 2rem;
}

.register-card {
  background: white;
  width: min(90%, 600px);
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.form-title {
  font-size: 1.5rem;
  color: #333;
  text-align: center;
  margin-bottom: 2rem;
}

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

.auth-links {
  margin-top: 1.5rem;
  display: flex;
  justify-content: center;
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

/* 移动端适配 */
@media (max-width: 768px) {
  .register-card {
    padding: 1.5rem 1rem;
  }

  .form-control {
    padding: 1rem;
  }
}

.password-group {
  position: relative;
}

.password-toggle {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #666;
  font-size: 0.875rem;
  padding: 4px 8px;
  border-radius: 4px;
  background: transparent;
  transition: all 0.3s ease;
}

.password-toggle:hover {
  color: #e1251b;
  background: rgba(225, 37, 27, 0.1);
}

/* 确保输入框不会与按钮重叠 */
.password-group .form-control {
  padding-right: 70px;
}
</style>