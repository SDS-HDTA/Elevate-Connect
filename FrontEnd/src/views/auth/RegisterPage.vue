<template>
  <div class="register-page">
    <Header class="header" />
    <div class="main-content">
      <div v-if="registerStep === 1" class="register-container">
        <div class="back-button-container">
          <div class="back-button" @click="$router.push('/')">
            <el-icon><ArrowLeft /></el-icon>
            <span>Back</span>
          </div>
        </div>
        <h1 class="form-title-xl">Register</h1>

        <div class="register-card">
          <el-form
            :model="step1formData"
            :rules="step1Rules"
            v-loading="step1Loading"
            ref="step1Ref"
            label-width="120px"
          >
            <el-form-item label="Email Address" prop="email">
              <el-input v-model="step1formData.email" />
            </el-form-item>
            <el-form-item label="Invite Code" prop="inviteCode">
              <el-input v-model="step1formData.inviteCode" />
            </el-form-item>
          </el-form>
          <div class="flex justify-content-center">
            <el-button @click="handleConfirmCode" class="btn-primary"
              >Continue</el-button
            >
          </div>
        </div>
      </div>
      <div v-if="registerStep === 2" class="register-container">
        <div class="back-button-container">
          <div class="back-button" @click="decreaseStep()">
            <el-icon><ArrowLeft /></el-icon>
            <span>Back</span>
          </div>
        </div>
        <h1 class="form-title-xl">Register</h1>

        <div class="register-card">
          <el-form
            :model="step2formData"
            :rules="step2Rules"
            v-loading="step2Loading"
            ref="step2Ref"
            label-width="120px"
          >
            <div class="flex flex-row w-100">
              <el-form-item
                class="w-100 me-2"
                label="First Name"
                prop="firstName"
              >
                <el-input
                  placeholder="Given Name(s)"
                  @keypress="preventIllegalChars"
                  v-model="step2formData.firstName"
                />
              </el-form-item>
              <el-form-item
                class="w-100 ms-2"
                label="Last Name"
                prop="lastName"
              >
                <el-input
                  placeholder="Surname"
                  @keypress="preventIllegalChars"
                  v-model="step2formData.lastName"
                />
              </el-form-item>
            </div>
            <el-form-item label="Phone Number" prop="phone">
              <el-input
                type="tel"
                v-model="step2formData.phone"
                @keypress="sanitizePhoneNumber"
                placeholder="e.g. +61412345678"
                class="form-control"
                required
              />
            </el-form-item>
            <el-form-item label="Password" prop="password">
              <el-input
                :type="showPassword ? 'text' : 'password'"
                v-model="step2formData.password"
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
                v-model="step2formData.confirmPassword"
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
            <el-button @click="handleSignup" class="btn-primary"
              >Sign up</el-button
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
import { ArrowLeft, View } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import Header from '@/components/Header.vue';
import { parsePhoneNumberFromString } from 'libphonenumber-js';

const router = useRouter();
const showPassword = ref(false);
const showConfirmPassword = ref(false);
const step2Loading = ref(false);
const step1Loading = ref(false);
const step2Ref = ref(null);
const step1Ref = ref(null);
const registerStep = ref(2); // 1: Email & Invite Code, 2: Complete Registration

const step1formData = reactive({
  email: '',
  inviteCode: '',
});

const step2formData = reactive({
  firstName: '',
  lastName: '',
  password: '',
  confirmPassword: '',
  communityId: '',
  country: '',
  phone: '',
  organization: '',
  role: '',
  email: '',
});

const step1Rules = {
  email: [
    { required: true, message: 'Required field', trigger: 'blur' },
    { type: 'email', message: 'Invalid email', trigger: 'blur' },
  ],
  phone: [
    { required: true, message: 'Required field', trigger: 'blur' },
    {
      pattern: /^\+?[1-9]\d{1,14}$/,
      message: 'Invalid phone number',
      trigger: 'blur',
    },
  ],
  inviteCode: [{ required: true, message: 'Required field', trigger: 'blur' }],
};

const step2Rules = {
  firstName: [
    { required: true, message: 'Required field', trigger: 'blur' },
    {
      min: 3,
      max: 20,
      message: 'First name must be between 3 and 20 characters',
      trigger: 'blur',
    },
  ],
  lastName: [
    { required: true, message: 'Required field', trigger: 'blur' },
    {
      min: 3,
      max: 20,
      message: 'Last name must be between 3 and 20 characters',
      trigger: 'blur',
    },
  ],
  phone: [
    { required: true, message: 'Required field', trigger: 'blur' },
    {
      pattern: /^\+?\d{0,3}?[-.\s()]?\d{6,14}$/,
      message: 'Invalid phone number format',
      trigger: 'blur',
    },
  ],
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
        if (value !== step2formData.password) {
          callback(new Error('Passwords do not match'));
        } else {
          callback();
        }
      },
    },
  ],
};

// Prevent illegal characters from being entered
const preventIllegalChars = (event) => {
  // Allow English letters, numbers and spaces
  const allowedChars = /^[a-zA-Z0-9\s]$/;
  if (!allowedChars.test(event.key)) {
    event.preventDefault();
  }
};

const sanitizePhoneNumber = (event) => {
  // Allow only +, numbers and spaces
  const allowedChars = /^[+0-9\s]$/;
  if (!allowedChars.test(event.key)) {
    event.preventDefault();
  }
};

const handleConfirmCode = async () => {
  if (!step1Ref.value) return;

  const validForm = await step1Ref.value.validate();
  if (!validForm) return;

  step1Loading.value = true;

  try {
    const response = await request.post(
      '/checkCode',
      { email: step1formData.email, code: step1formData.inviteCode },
      {
        headers: {
          'Content-Type': 'application/json',
        },
      }
    );

    if (response.code === 1) {
      ElMessage.success('Code confirmed successfully');
      registerStep.value = 2;

      step2formData.email = step1formData.email;
      step2formData.role = response.data.role;
      step2formData.communityId = response.data.communityId;
      step2formData.country = response.data.country;
      step2formData.organization = response.data.organization;

      step1formData.email = '';
      step1formData.inviteCode = '';
    } else {
      ElMessage.error(response.message || 'Code confirmation failed');
    }
  } catch (error) {
    const errorMessage =
      error.response?.data?.message ||
      error.message ||
      'Code confirmation failed, please try again';

    ElMessage.error(errorMessage);
  } finally {
    step1Loading.value = false;
  }
};

const decreaseStep = () => {
  registerStep.value = 1;

  step2formData.firstName = '';
  step2formData.lastName = '';
  step2formData.password = '';
  step2formData.confirmPassword = '';
  step2formData.communityId = '';
  step2formData.country = '';
  step2formData.organization = '';
  step2formData.role = '';
  step2formData.email = '';
  step2formData.phone = '';
};

const handleSignup = async () => {
  if (!step2Ref.value) return;

  const validForm = await step2Ref.value.validate();
  if (!validForm) return;

  step2Loading.value = true;
  try {
    const requestBody = {
      firstName: step2formData.firstName,
      lastName: step2formData.lastName,
      email: step2formData.email,
      password: step2formData.password,
      role: step2formData.role,
      communityId: !!step2formData.communityId ? step2formData.communityId : '',
      country: !!step2formData.country ? step2formData.country : '',
      phone: normalizePhone(step2formData.phone),
      organization: !!step2formData.organization
        ? step2formData.organization
        : '',
    };

    const response = await request.post('/register', requestBody, {
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (response) {
      ElMessage.success('Registration successful');

      router.push('/login');
    } else {
      ElMessage.error(response.message || 'Registration failed');
    }
  } catch (error) {
    const errorMessage =
      error.response?.data?.message ||
      error.message ||
      'Registration failed, please try again';
    ElMessage.error(errorMessage);
  } finally {
    step2Loading.value = false;
  }
};

const normalizePhone = (phone) => {
  try {
    const parsed = parsePhoneNumberFromString(phone);
    return parsed ? parsed.number : phone; // returns E.164 format like +61412345678
  } catch {
    return phone;
  }
};
</script>

<style scoped>
.register-page {
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

.register-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: var(--color-background-light);
  min-height: calc(100vh - 60px);
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
