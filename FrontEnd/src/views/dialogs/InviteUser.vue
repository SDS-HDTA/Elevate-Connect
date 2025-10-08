<template>
  <el-dialog
    :before-close="handleClose"
    title="Invite User"
    v-model="visible"
    max-width="500px"
    class="custom-dialog"
  >
    <el-form
      v-loading="loading"
      :model="form"
      :rules="rules"
      ref="formRef"
      label-width="120px"
    >
      <el-form-item label="Email" prop="email">
        <el-input v-model="form.email" />
      </el-form-item>

      <el-form-item label="Role" prop="role">
        <el-select v-model="form.role" placeholder="Select role">
          <el-option
            v-for="(label, value) in roleMap"
            :key="value"
            :label="label"
            :value="Number(value)"
          />
        </el-select>
      </el-form-item>

      <el-form-item
        v-if="
          communities.length > 0 &&
          requiresCommunity(form.role) &&
          form.role !== null
        "
        label="Community"
        :required="requiresCommunity(form.role)"
        prop="community"
      >
        <el-select
          filterable
          v-model="form.community"
          placeholder="Select community"
        >
          <el-option
            v-for="community in communities"
            :key="community.id"
            :label="community.name"
            :value="community.id"
          />
        </el-select>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button class="btn-secondary" @click="handleClose">Cancel</el-button>
      <el-button class="btn-primary" @click="submitInvite">Invite</el-button>
    </template>
  </el-dialog>
</template>
<script setup>
import { ref, reactive, computed, watch } from 'vue';
import request from '@/utils/request';
import { ElMessage } from 'element-plus';
import { roleMap } from '@/utils/roleHelper';
const props = defineProps({
  modelValue: { type: Boolean, required: true },
  communities: { type: Array, default: () => [] },
  users: { type: Array, default: () => [] },
});

const emit = defineEmits(['update:modelValue', 'submit', 'close']);

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val),
});

const loading = ref(false);
const formRef = ref(null);
const form = reactive({
  email: '',
  role: null,
  community: null,
});

watch(
  () => form.role,
  (newRole) => {
    if (!requiresCommunity(newRole)) {
      form.community = null;
    }
  }
);

const rules = {
  email: [
    { required: true, message: 'Required field', trigger: 'blur' },
    { type: 'email', message: 'Invalid email', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (
          props.users.some((u) => u.email.toLowerCase() === value.toLowerCase())
        ) {
          callback(new Error('Email already in use'));
        } else {
          callback();
        }
      },
      trigger: 'blur',
    },
  ],
  role: [{ required: true, message: 'Required field', trigger: 'change' }],
  community: [
    {
      validator: (rule, value, callback) => {
        if (requiresCommunity(form.role) && !value) {
          callback(new Error('Required field'));
        } else {
          callback();
        }
      },
      trigger: 'change',
    },
  ],
};

const submitInvite = async () => {
  if (!formRef.value) return;

  const validForm = await formRef.value.validate();
  if (!validForm) return;

  try {
    loading.value = true;

    const params = new URLSearchParams();
    params.append('email', form.email);
    params.append('role', form.role);
    params.append('community', form.community);

    const res = await request.post('/manager/sendInvitationCode', params, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    });

    if (res.code === 1) {
      ElMessage.success('User invited successfully');

      modelValue.value = false;
      formRef.value.resetFields();
      emit('submit');
    } else {
      ElMessage.error('An error occurred: ' + res.message);
    }
  } catch (error) {
    ElMessage.error(
      'An error occurred: ' + (error.response?.data?.message || error.message)
    );
  } finally {
    loading.value = false;
  }
};

function handleClose() {
  emit('close');
  emit('update:modelValue', false);
  formRef.value.resetFields();
}

const requiresCommunity = (role) => role === 0;
</script>
