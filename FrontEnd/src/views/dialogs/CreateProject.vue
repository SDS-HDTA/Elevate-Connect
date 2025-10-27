<template>
  <el-dialog
    :before-close="handleClose"
    title="Create New Project"
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

      <el-form-item label="Community" prop="communityId">
        <el-select v-model="form.communityId" placeholder="Select community">
          <el-option
            v-for="community in communities"
            :key="community.id"
            :label="community.name"
            :value="community.id"
          />
        </el-select>
        <el-tip v-if="form.communityId" class="tip"
          >Country:
          {{
            communities.find((c) => c.id === form.communityId)?.country
          }}</el-tip
        >
      </el-form-item>

      <el-form-item label="Category" prop="category">
        <el-select v-model="form.category" placeholder="Select category">
          <el-option
            v-for="(label, value) in projectCategories"
            :key="value"
            :label="label"
            :value="value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="Description" prop="description">
        <el-input
          type="textarea"
          v-model="form.description"
          :maxlength="150"
          placeholder="Briefly describe the project (max 150 characters)"
          resize="vertical"
        />
      </el-form-item>

      <el-form-item label="Status" prop="status">
        <el-select v-model="form.status" placeholder="Select status">
          <el-option label="Empathise" :value="0" />
          <el-option label="Discover" :value="1" />
          <el-option label="Define" :value="2" />
          <el-option label="Ideate" :value="3" />
          <el-option label="Prototype" :value="4" />
          <el-option label="Completed" :value="5" />
        </el-select>
      </el-form-item>

      <el-form-item label="Target Date" prop="deadline">
        <el-date-picker
          v-model="form.deadline"
          type="date"
          placeholder="Select target date"
          format="DD-MM-YYYY"
          value-format="YYYY-MM-DD"
          :disabled-date="disablePastDates"
        />
      </el-form-item>

      <el-form-item label="Image" prop="image">
        <div v-if="imagePreview" class="flex flex-column align-items-end">
          <el-button class="btn-icon-danger" @click="removeImage()"
            ><el-icon class="me-1"><Remove /></el-icon>Remove Image</el-button
          >
          <img class="w-100" :src="imagePreview" alt="Project Image" />
        </div>
        <el-upload
          v-else
          ref="uploadRef"
          :file-list="uploadFiles"
          :auto-upload="false"
          :limit="1"
          :on-exceed="handleExceed"
          :on-change="handleImageChange"
        >
          <el-button class="btn-secondary">
            <el-icon><Plus /></el-icon>
            <span>Select Image</span>
          </el-button>
          <template #tip>
            <div class="el-upload__tip">
              Only one image can be uploaded. Recommended dimensions: 700x400
            </div>
          </template>
        </el-upload>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button class="btn-secondary" @click="handleClose">Cancel</el-button>
      <el-button class="btn-primary" :loading="loading" @click="submitForm"
        >Create</el-button
      >
    </template>
  </el-dialog>
</template>
<script setup>
import { ref, computed } from 'vue';
import { Remove, Plus } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import request from '@/utils/request';
import { projectCategories } from '@/utils/projectCategoryHelper';
import { disablePastDates } from '@/utils/disablePastDates';
import { getLegibleFileSize } from '@/utils/imageHelper';

const props = defineProps({
  modelValue: { type: Boolean, required: true },
  communities: { type: Array, default: () => [] },
  projects: { type: Array, default: () => [] },
});

const emit = defineEmits(['update:modelValue', 'submit', 'close']);

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val),
});

const loading = ref(false);
const formRef = ref(null);
const uploadFiles = ref([]);
const uploadRef = ref(null);
const imagePreview = ref(null);

const form = ref({
  name: '',
  communityId: null,
  description: '',
  status: null,
  deadline: '',
  image: null,
});

const rules = {
  name: [{ required: true, message: 'Required field', trigger: 'blur' }],
  communityId: [{ required: true, message: 'Required field', trigger: 'blur' }],
  description: [{ required: true, message: 'Required field', trigger: 'blur' }],
  category: [{ required: true, message: 'Required field', trigger: 'blur' }],
  status: [{ required: true, message: 'Required field', trigger: 'change' }],
  deadline: [{ required: true, message: 'Required field', trigger: 'change' }],
  image: [{ required: true, message: 'Required field', trigger: 'change' }],
};

const handleExceed = () => {
  ElMessage.warning('Only one picture can be uploaded');
};

const submitForm = async () => {
  if (!formRef.value) return;

  try {
    const validForm = await formRef.value.validate();
    if (!validForm) {
      ElMessage.error('Please fill in all required fields');
      return;
    }
  } catch (error) {
    console.error('Form validation failed:', error);
    ElMessage.error('Please check all fields and try again');
    return;
  }

  try {
    loading.value = true;

    // Create FormData object
    const formData = new FormData();

    // Append form fields
    formData.append('communityId', form.value.communityId);
    formData.append('name', form.value.name);
    formData.append('description', form.value.description);
    formData.append('category', form.value.category);
    formData.append('status', form.value.status);
    formData.append('targetDate', form.value.deadline);
    formData.append('projectImage', form.value.image);

    const res = await request({
      url: '/projects/create',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    if (res.code === 1) {
      ElMessage.success('Project created successfully');

      // Clear uploaded files
      if (uploadRef.value) {
        uploadRef.value.clearFiles();
      }

      formRef.value.resetFields();

      emit('submit');
      emit('update:modelValue', false);
    } else {
      ElMessage.error(res.message || 'Project creation failed');
    }
  } catch (error) {
    console.error('Error creating project:', error);
    ElMessage.error(
      'Error creating project: ' +
        (error.response?.data?.message || error.message)
    );
  } finally {
    loading.value = false;
  }
};

function handleClose() {
  emit('close');
  emit('update:modelValue', false);
  if (uploadRef.value) {
    uploadRef.value.clearFiles();
  }
  removeImage();
  formRef.value.resetFields();
}

const handleImageChange = (file) => {
  if (!file.raw.type.startsWith('image/')) {
    ElMessage.error('Only image files are allowed!');
    form.value.image = null;
    uploadFiles.value = [];
    return;
  }

  if (file.size > MAX_FILE_SIZE_BYTES) {
    ElMessage.error(
      `File is too large, the max size is ${getLegibleFileSize()}MB`
    );
    form.value.image = null;
    uploadFiles.value = [];
    return;
  }

  const rawFile = file.raw;
  form.value.image = rawFile;
  imagePreview.value = URL.createObjectURL(rawFile);

  // Manually trigger validation for the image field
  if (formRef.value) {
    formRef.value.validateField('image');
  }
};

function removeImage() {
  URL.revokeObjectURL(imagePreview.value);
  imagePreview.value = null;
  form.value.image = null;
}
</script>
<style scoped>
.tip {
  font-size: 12px;
  opacity: 0.6;
}
</style>
