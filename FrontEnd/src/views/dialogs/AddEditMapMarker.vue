<template>
  <el-dialog
    :title="isEdit ? 'Edit Marker' : 'New Marker'"
    v-model="visible"
    width="500px"
    :close-on-click-modal="false"
  >
    <el-form :model="form" ref="formRef" label-width="100px">
      <el-form-item
        label="Title"
        prop="title"
        :rules="[
          { required: true, message: 'Title is required', trigger: 'blur' },
        ]"
      >
        <el-input v-model="form.title" placeholder="Enter title" />
      </el-form-item>

      <el-form-item
        :rules="[
          {
            required: true,
            message: 'Description is required',
            trigger: 'blur',
          },
        ]"
        label="Description"
        prop="description"
      >
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="4"
          placeholder="Enter description"
        />
      </el-form-item>

      <el-form-item
        :rules="[
          { required: true, message: 'Type is required', trigger: 'blur' },
        ]"
        label="Type"
        prop="type"
      >
        <el-select v-model="form.type" filterable placeholder="Select type">
          <el-option
            v-for="item in markerTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button class="btn-secondary" @click="cancel">Cancel</el-button>
      <el-button class="btn-primary" @click="confirm">{{
        isEdit ? 'Save' : 'Create'
      }}</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { markerTypes } from '@/utils/markerTypeHelper';
import { ref, reactive, watch, defineEmits, defineProps, computed } from 'vue';

const props = defineProps({
  modelValue: Boolean,
  markerData: { type: Object, default: () => ({}) }, // existing marker for edit
  isEdit: { type: Boolean, default: false },
});

const emit = defineEmits(['update:modelValue', 'confirm']);

const visible = ref(props.modelValue);
const formRef = ref(null);
const form = reactive({
  title: props.markerData?.title || '',
  description: props.markerData?.desc || '',
  type: Number(props.markerData?.type) || 0,
});
const markerTypeOptions = computed(() =>
  Object.entries(markerTypes).map(([value, label]) => ({
    value: Number(value),
    label,
  }))
);

watch(
  () => props.modelValue,
  (val) => (visible.value = val)
);

watch(
  () => props.markerData,
  (newData) => {
    if (newData) {
      form.title = newData.title || '';
      form.description = newData.desc || '';
      form.type = Number(newData.type) || 0;
    }
  },
  { immediate: true, deep: true }
);

watch(
  () => props.isEdit,
  (editing) => {
    if (!editing) {
      form.title = '';
      form.description = '';
      form.type = 0;
    }
  }
);

watch(visible, (val) => emit('update:modelValue', val));

function confirm() {
  formRef.value.validate((valid) => {
    if (valid) {
      emit('confirm', { ...form });
      visible.value = false;
    }
  });
}

function cancel() {
  visible.value = false;
}
</script>
