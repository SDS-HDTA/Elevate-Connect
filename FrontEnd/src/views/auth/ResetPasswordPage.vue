<template>
  <div class="register-page">
    <Header class="header" />
    <div class="main-content">
      <div v-if="step === 1" class="register-container">
        <div class="back-button-container">
          <div class="back-button" @click="$router.push('/')">
            <el-icon><ArrowLeft /></el-icon>
            <span>Back</span>
          </div>
        </div>
        <h1 class="form-title-xl">Reset Password</h1>
        <span class="form-title">
          Enter your email to send a code, then enter the code sent to your
          email
        </span>

        <div class="register-card">
          <el-form
            :model="step1Data"
            :rules="step1Rules"
            v-loading="step1Loading"
            ref="step1Ref"
            label-width="120px"
          >
            <el-form-item label="Email Address" prop="email">
              <div class="flex flex-row w-100">
                <el-input v-model="step1Data.email" />
                <el-button
                  class="btn-icon-primary ms-3"
                  :disabled="isCodeSent && countdown > 0"
                  @click="sendVerificationCode"
                >
                  <el-icon class="me-2"><Promotion /></el-icon>
                  {{ countdown > 0 ? `Retry in ${countdown}s` : 'Send Code' }}
                </el-button>
              </div>
            </el-form-item>

            <el-form-item label="Verification Code" prop="verificationCode">
              <el-input
                @keypress="preventIllegalChars"
                v-model="step1Data.verificationCode"
              />
            </el-form-item>
          </el-form>
          <div class="flex justify-content-center">
            <el-button @click="handleSubmitCode" class="btn-primary"
              >Continue</el-button
            >
          </div>
        </div>
      </div>
      <div v-if="step === 2" class="register-container">
        <div class="back-button-container">
          <div class="back-button" @click="decreaseStep()">
            <el-icon><ArrowLeft /></el-icon>
            <span>Back</span>
          </div>
        </div>
        <h1 class="form-title-xl">Reset Password</h1>
        <span class="form-title">
          Code confirmed, please set your new password
        </span>

        <div class="register-card">
          <el-form
            :model="step2Data"
            :rules="step2Rules"
            v-loading="step2Loading"
            ref="step2Ref"
            label-width="120px"
          >
            <el-form-item label="Password" prop="password">
              <el-input
                :type="showPassword ? 'text' : 'password'"
                v-model="step2Data.password"
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
            <el-form-item label="Confirm Password" prop="confirmPassword">
              <el-input
                :type="showConfirmPassword ? 'text' : 'password'"
                v-model="step2Data.confirmPassword"
                placeholder="Confirm Password"
                class="form-control"
                required
              />
              <button
                type="button"
                :class="
                  showConfirmPassword ? 'password-toggled' : 'password-toggle'
                "
                @click="showConfirmPassword = !showConfirmPassword"
              >
                <el-icon class="view-icon"><View /></el-icon>
              </button>
            </el-form-item>
          </el-form>
          <div class="flex justify-content-center">
            <el-button @click="handleResetPassword" class="btn-primary"
              >Confirm</el-button
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
import request from '@/utils/request';
import { useUserStore } from '@/stores/userStore';
import { ElMessage } from 'element-plus';
import { View, ArrowLeft, Promotion } from '@element-plus/icons-vue';

const router = useRouter();
const showPassword = ref(false);
const showConfirmPassword = ref(false);
const isCodeSent = ref(false);
const countdown = ref(0);
const step1Loading = ref(false);
const step2Loading = ref(false);
const step1Ref = ref(null);
const step2Ref = ref(null);
const step = ref(1);
const userStore = useUserStore();

const preventIllegalChars = (event) => {
  const allowedChars = /^[0-9]$/;
  if (!allowedChars.test(event.key)) {
    event.preventDefault();
  }
};

const step1Data = reactive({
  email: '',
  verificationCode: '',
});

const step2Data = reactive({
  userId: '',
  password: '',
  confirmPassword: '',
});

const step1Rules = {
  email: [
    { required: true, message: 'Required field', trigger: 'blur' },
    { type: 'email', message: 'Invalid email', trigger: 'blur' },
  ],
  inviteCode: [{ required: true, message: 'Required field', trigger: 'blur' }],
};

const step2Rules = {
  password: [
    { required: true, message: 'Required field', trigger: 'blur' },
    {
      min: 8,
      max: 20,
      message: 'Password must be between 8 and 20 characters',
      trigger: 'blur',
    },
    {
      pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,20}$/,
      message: 'Password must contain letters and numbers',
      trigger: 'blur',
    },
  ],
  confirmPassword: [
    { required: true, message: 'Required field', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== step2Data.password) {
          callback(new Error('Passwords do not match'));
        } else {
          callback();
        }
      },
    },
  ],
};

const decreaseStep = () => {
  step.value = 1;

  step2formData.userId = '';
  step2formData.password = '';
  step2formData.confirmPassword = '';
};

// Send verification code
const sendVerificationCode = async () => {
  if (!step1Data.email) {
    ElMessage.error({
      message: 'Please enter your email address',
      type: 'error',
      duration: 2000,
    });
    return;
  }

  loading.value = true;

  try {
    const params = new URLSearchParams();
    params.append('email', step1Data.email);
    await request.post('/password/resetCode', params, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });

    // Start countdown after successful send
    isCodeSent.value = true;
    countdown.value = 60;
    const timer = setInterval(() => {
      countdown.value--;
      if (countdown.value <= 0) {
        clearInterval(timer);
      }
    }, 1000);
  } catch (error) {
    ElMessage.error(
      error.message || 'Failed to send verification code, please try again'
    );
  } finally {
    loading.value = false;
  }
};

const handleSubmitCode = async () => {
  if (!step1Ref.value) return;

  const validForm = await step1Ref.value.validate();
  if (!validForm) return;

  step1Loading.value = true;

  try {
    const response = await request.post(
      '/password/confirmCode',
      {
        email: step1Data.email,
        verificationCode: step1Data.verificationCode,
      },
      {
        headers: {
          'Content-Type': 'application/json',
        },
      }
    );

    if (response.code === 1) {
      ElMessage.success('Code verified, please set your new password');

      step2Data.userId = response.data.userId;
      step.value = 2;

      // Reset step1 data
      step1Data.email = '';
      step1Data.verificationCode = '';
    } else {
      ElMessage.error(
        response.message || 'Failed to verify code, please try again'
      );
    }
  } catch (error) {
    ElMessage.error(
      error.message || 'Failed to reset password, please try again'
    );
  } finally {
    step1Loading.value = false;
  }
};

// Submit form
const handleResetPassword = async () => {
  if (!step2Ref.value) return;

  const validForm = await step2Ref.value.validate();
  if (!validForm) return;

  step2Loading.value = true;

  try {
    const params = new URLSearchParams();
    params.append('userId', step2Data.userId);
    params.append('newPassword', step2Data.password);

    const response = await request.post('/password/update', params, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });

    if (response.code === 1) {
      ElMessage.success('Password changed successfully');

      if (userStore.userInfo) {
        router.push('/my-projects'); // Keep logged-in users in the app, redirect to my-projects homepage
      } else {
        router.push('/login'); // Redirect non-logged-in users to login page
      }
    } else {
      ElMessage.error(
        response.message || 'Failed to reset password, please try again'
      );
    }
  } catch (error) {
    ElMessage.error(
      error.message || 'Failed to reset password, please try again'
    );
  } finally {
    step2Loading.value = false;
  }
};
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  margin-top: 60px;
  overflow-y: auto;
}

.register-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: var(--color-background-light);
  margin-top: -60px;
  min-height: calc(100vh - 120px);
}

.form-title {
  padding-left: 1rem;
  padding-right: 1rem;
  color: var(--color-dark);
  text-align: center;
}

.form-title-xl {
  font-size: 2rem;
  width: min(90%, 520px);

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

.register-card {
  width: min(90%, 520px);
  padding: 1rem;
}

.back-button-container {
  display: flex;
  align-items: flex-start;
  width: min(90%, 520px);
  padding-left: 1rem;
}
</style>
