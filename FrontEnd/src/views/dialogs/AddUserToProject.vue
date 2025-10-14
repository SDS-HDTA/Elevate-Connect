<template>
  <el-dialog
    title="Add Humanitarian Impact Partners to Project"
    :model-value="modelValue"
    width="500px"
    @close="$emit('update:modelValue', false)"
  >
    <div v-loading="loading">
      <el-form label-position="top">
        <el-form-item label="Select Users">
          <el-select
            v-model="selectedUsers"
            filterable
            multiple
            collapse-tags
            collapse-tags-tooltip
            placeholder="Select users to add"
            style="width: 100%"
          >
            <el-tooltip
              v-for="user in filteredUsers"
              :key="user.id"
              :content="user.email"
              placement="top"
            >
              <el-option
                :key="user.id"
                :label="`${user.firstName} ${user.lastName} (${user.organization})`"
                :value="user.id"
              />
            </el-tooltip>
          </el-select>
          <el-tip class="tip"
            >You can remove users from the project in the project's participants
            page.</el-tip
          >
        </el-form-item>
      </el-form>
    </div>

    <template #footer>
      <el-button @click="$emit('update:modelValue', false)">Cancel</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">
        Add Users
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import request from '@/utils/request';

const props = defineProps({
  modelValue: Boolean,
  projectId: Number,
  users: Array, // all system users
});
const emits = defineEmits(['update:modelValue', 'submit']);

const selectedUsers = ref([]);
const loading = ref(false);
const submitting = ref(false);
const projectMemberIds = ref([]); // store current project member IDs

// Fetch current project members (so we can exclude them)
const fetchProjectMembers = async () => {
  if (!props.projectId) return;
  loading.value = true;
  try {
    const response = await request.get(`/projects/${props.projectId}/members`, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    });

    if (response.code === 1) {
      projectMemberIds.value = response.data.map((user) => user.id);
    } else {
      ElMessage.error('Failed to fetch project members: ' + response.message);
    }
  } catch (error) {
    ElMessage.error('An error occurred: ' + error.message);
  } finally {
    loading.value = false;
  }
};

// Only show users who:
// 1. Are not already project members
// 2. Have role === 2
const filteredUsers = computed(() =>
  (props.users || []).filter(
    (user) => !projectMemberIds.value.includes(user.id) && user.role === 2
  )
);

const handleSubmit = async () => {
  if (!selectedUsers.value.length) {
    ElMessage.warning('Please select at least one user.');
    return;
  }

  submitting.value = true;
  try {
    const response = await request.post(
      `/manager/project/${props.projectId}/users`,
      {
        userIds: selectedUsers.value,
      }
    );

    if (response.code === 1) {
      ElMessage.success('Users added successfully.');
      emits('submit');
      emits('update:modelValue', false);
    } else {
      ElMessage.error('Failed to add users: ' + response.message);
    }
  } catch (error) {
    ElMessage.error('An error occurred: ' + error.message);
  } finally {
    submitting.value = false;
  }
};

// Reset selections + fetch members whenever dialog opens
watch(
  () => props.modelValue,
  (val) => {
    if (val) {
      selectedUsers.value = [];
      fetchProjectMembers();
    }
  }
);
</script>
<style scoped>
.tip {
  font-size: 12px;
  opacity: 0.6;
}
</style>
