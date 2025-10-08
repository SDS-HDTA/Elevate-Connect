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

      <el-form-item label="Area" prop="area">
        <el-input v-model="form.area" />
      </el-form-item>

      <el-form-item label="Category" prop="category">
        <el-input v-model="form.category" />
        <!-- TODO: Include helper category function getProjectCategoryText here -->
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
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>

      <el-form-item label="Image" prop="image">
        <el-upload
          ref="uploadRef"
          class="image-upload"
          :auto-upload="false"
          :show-file-list="true"
          :limit="1"
          :on-exceed="handleExceed"
          :on-change="handleImageChange"
        >
          <el-button class="upload-btn btn-secondary">
            <el-icon><Upload /></el-icon>
            <span>Select Image</span>
          </el-button>
          <template #tip>
            <div class="el-upload__tip">Only one image can be uploaded</div>
          </template>
        </el-upload>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button class="btn-secondary" @click="handleClose"> Cancel </el-button>
      <el-button class="btn-primary" @click="submitForm">Create</el-button>
    </template>
  </el-dialog>
</template>
<script setup>
import { ref, computed } from 'vue';
import { Upload } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import request from '@/utils/request';
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
const uploadRef = ref(null);

const form = ref({
  name: '',
  area: '',
  category: '',
  description: '',
  status: null,
  deadline: '',
  image: null,
});

const rules = {
  name: [{ required: true, message: 'Required field', trigger: 'blur' }],
  area: [{ required: true, message: 'Required field', trigger: 'blur' }],
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

  const validForm = await formRef.value.validate();
  if (!validForm) return;

  try {
    loading.value = true;

    // TODO: Change this to interact with a new community dropdown
    const requestData = {
      name: addForm.value.name,
      communityId: 1,
      creatorId: userId.value,
      area: addForm.value.area,
      category: addForm.value.category,
      description: addForm.value.description,
      status: addForm.value.status,
      targetDate: addForm.value.deadline,
      image: addForm.value.image,
    };

    /* TODO: Fix with image uploading */
    // if (addForm.value.image) {
    //   formData.append('image', addForm.value.image);
    // }

    const res = await request({
      url: '/projects/create',
      method: 'post',
      data: requestData,
      processData: false,
    });

    if (res.code === 1) {
      ElMessage.success('Project created successfully');

      // Clear uploaded files
      if (uploadRef.value) {
        uploadRef.value.clearFiles();
      }

      formRef.value.resetFields();

      emit('submit');
    } else {
      ElMessage.error(res.message || 'Project creation failed');
    }
  } catch (error) {
    console.error('Error creating project:', error);
    ElMessage.error(
      'Error creating project: ' +
        (error.response?.data?.message || error.message)
    );
  }
};

function handleClose() {
  emit('close');
  emit('update:modelValue', false);
  if (uploadRef.value) {
    uploadRef.value.clearFiles();
  }
  formRef.value.resetFields();
}

const handleImageChange = (file, fileList) => {
  // Only allow image files
  const validFiles = fileList.filter((f) => f.raw.type.startsWith('image/'));

  if (validFiles.length < fileList.length) {
    ElMessage.error('Only image files are allowed!');
  }

  // Update the form model
  addForm.value.image = validFiles[0]?.raw || null;

  // Update the upload component's file list to remove invalid files
  fileList.splice(0, fileList.length, ...validFiles);
};
</script>
