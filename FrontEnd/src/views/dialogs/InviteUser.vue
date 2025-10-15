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
        prop="communityId"
      >
        <el-select
          filterable
          v-model="form.communityId"
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
      <el-form-item
        v-if="requiresOrganization(form.role) && form.role !== null"
        label="Organisation"
        :required="requiresOrganization(form.role)"
        prop="organization"
      >
        <el-select
          v-model="form.organization"
          filterable
          allow-create
          default-first-option
          placeholder="Select or create organisation"
        >
          <el-option
            v-for="organization in organizations"
            :key="organization"
            :label="organization"
            :value="organization"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        v-if="requiresCountry(form.role) && form.role !== null"
        label="Country"
        :required="requiresCountry(form.role)"
        prop="country"
      >
        <el-select
          v-model="form.country"
          filterable
          default-first-option
          placeholder="Select country"
        >
          <el-option
            v-for="country in countries"
            :key="country"
            :label="country"
            :value="country"
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
  countries: { type: Array, default: () => [] },
  users: { type: Array, default: () => [] },
});

const emit = defineEmits(['update:modelValue', 'submit', 'close']);

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val),
});

const organizations = computed(() => {
  const orgSet = new Set();
  props.users.forEach((u) => {
    if (u.organization) {
      orgSet.add(u.organization);
    }
  });
  return Array.from(orgSet);
});

const loading = ref(false);
const formRef = ref(null);
const form = reactive({
  email: '',
  role: null,
  communityId: null,
  organization: null,
  country: null,
});

watch(
  () => form.role,
  (newRole) => {
    if (!requiresCommunity(newRole)) {
      form.communityId = null;
    }

    if (!requiresOrganization(newRole)) {
      form.organization = null;
    }

    if (!requiresCountry(newRole)) {
      form.country = null;
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
  communityId: [
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
  organization: [
    {
      validator: (rule, value, callback) => {
        if (requiresOrganization(form.role) && !value) {
          callback(new Error('Required field'));
        } else {
          callback();
        }
      },
      trigger: 'change',
    },
  ],
  country: [
    {
      validator: (rule, value, callback) => {
        if (requiresCountry(form.role) && !value) {
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
    params.append('communityId', form.communityId);
    params.append('organization', form.organization);
    params.append('country', form.country);

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
const requiresOrganization = (role) => role === 2;
const requiresCountry = (role) => role === 1;
</script>
