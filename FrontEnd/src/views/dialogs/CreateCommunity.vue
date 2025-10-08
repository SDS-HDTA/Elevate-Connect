<template>
  <el-dialog
    :before-close="handleClose"
    title="Create New Community"
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
      <el-form-item label="Name" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>

      <el-form-item label="Country" prop="country">
        <el-input v-model="form.country" />
      </el-form-item>

      <el-form-item label="Short Description" prop="shortDescription">
        <el-input
          type="textarea"
          v-model="form.shortDescription"
          :maxlength="150"
          placeholder="Briefly describe the community (max 150 characters)"
          resize="vertical"
        />
      </el-form-item>

      <!-- TODO: make a dropdown for country instead -->
      <!-- <el-form-item label="Status" prop="status">
        <el-select v-model="addForm.status" placeholder="Select status">
          <el-option label="Empathise" :value="0" />
          <el-option label="Discover" :value="1" />
          <el-option label="Define" :value="2" />
          <el-option label="Ideate" :value="3" />
          <el-option label="Prototype" :value="4" />
          <el-option label="Completed" :value="5" />
        </el-select>
      </el-form-item> -->
    </el-form>

    <template #footer>
      <el-button class="btn-secondary" @click="handleClose">Cancel</el-button>
      <el-button class="btn-primary" @click="submitForm">Create</el-button>
    </template>
  </el-dialog>
</template>
<script setup>
import { ref, computed } from 'vue';
import { ElMessage } from 'element-plus';
import request from '@/utils/request';
const props = defineProps({
  modelValue: { type: Boolean, required: true },
  communities: { type: Array, default: () => [] },
});

const emit = defineEmits(['update:modelValue', 'submit', 'close']);

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val),
});

const loading = ref(false);
const formRef = ref(null);

const form = ref({
  name: '',
  country: '',
  shortDescription: '',
});

const rules = {
  name: [
    { required: true, message: 'Required field', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (
          props.communities.some(
            (u) => u.name.toLowerCase() === value.toLowerCase()
          )
        ) {
          callback(new Error('Name already in use'));
        } else {
          callback();
        }
      },
      trigger: 'blur',
    },
  ],
  country: [{ required: true, message: 'Required field', trigger: 'blur' }],
  shortDescription: [
    { required: true, message: 'Required field', trigger: 'blur' },
  ],
};

const submitForm = async () => {
  if (!formRef.value) return;

  const validForm = await formRef.value.validate();
  if (!validForm) return;

  try {
    loading.value = true;

    const res = await request.post(
      '/community/create',
      {
        name: form.value.name,
        country: form.value.country,
        short_description: form.value.shortDescription,
      },
      {
        headers: { 'Content-Type': 'application/json' },
      }
    );

    if (res.code === 1) {
      ElMessage.success('Community created successfully');

      visible.value = false;
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
</script>
