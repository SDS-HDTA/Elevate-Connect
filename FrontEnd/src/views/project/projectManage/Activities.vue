<template>
  <div v-if="!isTablet" class="backlog-container">
    <div class="backlog-top">
      <div class="step-bar">
        <el-steps :active="activeStep" align-center finish-status="success">
          <el-step v-for="(item, idx) in steps" :key="idx" :title="item" />
        </el-steps>
      </div>
      <div
        class="step-btns"
        v-require-permission="permissions.ModifyCurrentProjectStage"
      >
        <el-button
          class="btn-secondary"
          :disabled="activeStep === 0"
          @click="handlePrev"
        >
          <el-icon><ArrowLeft /></el-icon>
          Go Back
        </el-button>
        <el-button
          class="btn-primary"
          :disabled="activeStep === steps.length - 1"
          @click="handleNext"
        >
          Go Next
          <el-icon><ArrowRight /></el-icon>
        </el-button>
      </div>
    </div>
    <!-- Bottom content area -->
    <div class="backlog-content">
      <div class="iterations-container">
        <div
          v-for="iteration in iterations"
          :key="iteration.id"
          class="iteration-section"
          style="width: 100%; overflow-x: auto"
        >
          <div class="interation-header">
            <span>{{ iteration.title }}</span>
          </div>
          <el-table
            :data="iteration.tasks"
            row-key="id"
            border
            max-height="400"
            style="width: 100%"
            :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
            @selection-change="handleSelectionChange"
            ref="backlogTable"
          >
            <el-table-column
              prop="type"
              label="Type"
              width="60"
              header-align="left"
            >
              <template #default="scope">
                <span class="flex align-items-center">
                  <el-icon v-if="scope.row.type === 'task'">
                    <Document />
                  </el-icon>
                  <el-icon v-else-if="scope.row.type === 'subtask'">
                    <DocumentAdd />
                  </el-icon>
                  <el-tooltip
                    v-if="scope.row.type === 'task'"
                    content="Add Subtask"
                    placement="top"
                  >
                    <el-button
                      v-require-permission="permissions.CreateTask"
                      class="btn-icon-primary plus-height ms-1"
                      @click.stop="handleAddSubTask(scope.row, iteration)"
                    >
                      <el-icon><Plus /></el-icon>
                    </el-button>
                  </el-tooltip>
                </span>
              </template>
            </el-table-column>
            <el-table-column
              prop="code"
              label="Code"
              min-width="60"
              header-align="center"
              align="center"
            >
              <template #default="scope">
                <span>{{ scope.row.code }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="content" label="Content" min-width="200">
              <template #default="scope">
                {{ scope.row.content }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="Status" min-width="120">
              <template #default="scope">
                <div class="status-cell">
                  <el-tag
                    :type="
                      scope.row.status === 2
                        ? 'success'
                        : scope.row.status === 1
                          ? 'warning'
                          : 'info'
                    "
                    class="status-tag"
                  >
                    {{ statusMap[scope.row.status] }}
                  </el-tag>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="assigneeId" label="Assignee" min-width="140">
              <template #default="scope">
                <div class="assignee-cell">
                  <div class="assignee-display">
                    <Avatar
                      :full-name="scope.row?.assignee"
                      :size="20"
                      v-if="getMember(scope.row.assigneeId) !== null"
                    />
                    <span>{{
                      scope.row.assigneeId === null
                        ? 'Unknown'
                        : scope.row?.assignee
                    }}</span>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              :width="
                permissionStore.hasPermission(permissions.AdminAllPermissions)
                  ? 70
                  : 40
              "
              v-if="permissionStore.hasPermission(permissions.EditTask)"
            >
              <template #default="scope">
                <el-tooltip content="Edit Task" placement="top">
                  <el-button
                    @click="openEditDialog(scope.row)"
                    v-require-permission="permissions.EditTask"
                    class="btn-icon-info"
                  >
                    <el-icon><Edit /></el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip content="Delete Task" placement="top">
                  <el-button
                    v-require-permission="permissions.AdminAllPermissions"
                    class="btn-icon-danger"
                    @click="handleDelete(scope.row)"
                  >
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
          <div class="new-task-container">
            <el-button
              v-require-permission="permissions.CreateTask"
              class="btn-icon-primary"
              @click="handleAddNewTask(iteration)"
            >
              <el-icon><Plus /></el-icon>
              Add Task
            </el-button>
          </div>
        </div>
      </div>
    </div>
    <el-dialog
      :before-close="handleEditDialogClose"
      title="Edit Task"
      v-model="editDialogVisible"
      width="500px"
    >
      <el-form
        :model="editForm"
        :rules="editRules"
        v-loading="editDialogLoading"
        ref="editFormRef"
        label-width="120px"
      >
        <!-- Content Field -->
        <el-form-item label="Content" prop="content">
          <el-input
            v-model="editForm.content"
            placeholder="Enter task content"
            type="textarea"
            resize="vertical"
          />
        </el-form-item>

        <!-- Status Field -->
        <el-form-item label="Status" prop="status">
          <el-select
            v-model="editForm.status"
            placeholder="Select Status"
            style="width: 100%"
          >
            <el-option label="To Do" value="0" />
            <el-option label="In Progress" value="1" />
            <el-option label="Done" value="2" />
          </el-select>
        </el-form-item>

        <!-- Assignee Field -->
        <el-form-item label="Assignee" prop="assigneeId">
          <el-select
            v-model="editForm.assigneeId"
            placeholder="Select member"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="member in members"
              :key="member.id"
              :label="member.firstName + ' ' + member.lastName"
              :value="Number(member.id)"
            >
              <div class="assignee-option">
                <Avatar
                  :full-name="member.firstName + ' ' + member.lastName"
                  :size="20"
                />
                <span>{{ member.firstName + ' ' + member.lastName }}</span>
              </div>
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button class="btn-secondary" @click="handleEditDialogClose">
          Cancel
        </el-button>
        <el-button class="btn-primary" @click="submitEdit(editForm.id)">
          Save
        </el-button>
      </template>
    </el-dialog>
  </div>
  <div v-if="isTablet">
    <div class="flex align-items-center">
      <span>Current stage:</span>
      <el-tag class="ms-2" :type="getStageType(activeStep)">{{
        getProjectStageText(activeStep)
      }}</el-tag>
    </div>

    <el-result
      icon="warning"
      sub-title="Activities can only be viewed on desktop"
    >
    </el-result>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import {
  ElSteps,
  ElStep,
  ElButton,
  ElIcon,
  ElMessageBox,
  ElMessage,
} from 'element-plus';
import {
  ArrowLeft,
  ArrowRight,
  Plus,
  Edit,
  Document,
  DocumentAdd,
  Delete,
} from '@element-plus/icons-vue';
import request from '@/utils/request';
import { useRoute } from 'vue-router';
import Avatar from '@/components/Avatar.vue';
import { permissions } from '@/models/permission';
import { usePermissionStore } from '@/stores/permissionStore';
import { getProjectStageText, getStageType } from '@/utils/projectStageHelper';

const steps = [
  'Empathise',
  'Discover',
  'Define',
  'Ideate',
  'Prototype',
  'Completed',
];
const activeStep = ref(0);
const route = useRoute();
const permissionStore = usePermissionStore();
const isTablet = ref(window.innerWidth <= 768);
const editDialogVisible = ref(false);
const editDialogLoading = ref(false);
const editFormRef = ref(null);

const editForm = ref({
  id: null,
  content: '',
  status: '',
  assigneeId: null,
});

const editRules = {
  content: [
    { required: true, message: 'Please enter content', trigger: 'blur' },
  ],
  status: [
    { required: true, message: 'Please select a status', trigger: 'change' },
  ],
  assigneeId: [
    { required: true, message: 'Please select an assignee', trigger: 'change' },
  ],
};

const iterations = ref([]);

const selectedRows = ref([]);

const handleSelectionChange = (val) => {
  selectedRows.value = val;
};

const updateScreen = () => {
  isTablet.value = window.innerWidth <= 768;
};

// Get current project status
const fetchStatus = async () => {
  try {
    const projectId = route.params.id;
    const res = await request.get(`/projects/${projectId}/stage`);
    if (res.code === 1) {
      activeStep.value = res.data;
    }
  } catch (e) {
    activeStep.value = 0;
  }
};

// Update project status
const updateProjectStatus = async (newStage) => {
  try {
    const projectId = route.params.id;

    const formData = new FormData();
    formData.append('projectStage', newStage);

    const res = await request.put(`/projects/${projectId}/stage`, formData);
    if (res.code === 1) {
      activeStep.value = newStage;
    }
  } catch (error) {
    console.error('Failed to update project status:', error);
  }
};

// Create new iteration method
const createIteration = async (status) => {
  const projectId = route.params.id;
  await request.post(`/projects/${projectId}/iterations`, {
    projectStatus: status,
    userId: localStorage.getItem('userId'),
  });
};

const handlePrev = async () => {
  if (activeStep.value > 0) {
    try {
      await ElMessageBox.confirm(
        'Are you sure you want to go back a stage?',
        'Confirm',
        {
          confirmButtonClass: 'btn-primary',
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
        }
      );
      const newStatus = activeStep.value - 1;
      await updateProjectStatus(newStatus);
      await createIteration(newStatus);
      await fetchIterations();
    } catch (e) {}
  }
};

const handleNext = async () => {
  if (activeStep.value < steps.length - 1) {
    try {
      await ElMessageBox.confirm(
        'Are you sure you want to go to the next stage?',
        'Confirm',
        {
          confirmButtonClass: 'btn-primary',
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
        }
      );
      const newStatus = activeStep.value + 1;
      await updateProjectStatus(newStatus);
      await createIteration(newStatus);
      await fetchIterations();
    } catch (e) {}
  }
};

const backlogTable = ref(null);

// Get project members
const members = ref([]);
const loadMembers = () => {
  try {
    const projectId = route.params.id;
    const memberStr = localStorage.getItem(`project_${projectId}_members`);
    if (memberStr) {
      members.value = JSON.parse(memberStr);
    } else {
      members.value = [];
    }
  } catch {
    members.value = [];
  }
};

onMounted(async () => {
  await fetchStatus();
  await fetchIterations();
  loadMembers();

  window.addEventListener('resize', updateScreen);
});

onUnmounted(() => {
  window.removeEventListener('resize', updateScreen);
});

// Check if user has permission to delete task
const canDeleteTask = (task) => {
  const currentUserId = localStorage.getItem('userId');
  // If task creator, can delete own task
  return String(task.creatorId) === String(currentUserId);
};

// Delete data in frontend
const deleteTaskFromIterations = (iterations, taskId) => {
  for (let iteration of iterations) {
    // Check main task
    const mainTaskIndex = iteration.tasks.findIndex(
      (task) => task.id === taskId
    );
    if (mainTaskIndex !== -1) {
      // If main task, delete entire task (including subtasks)
      iteration.tasks.splice(mainTaskIndex, 1);
      return;
    }
    // Check subtasks
    for (let task of iteration.tasks) {
      if (task.children) {
        const childTaskIndex = task.children.findIndex(
          (child) => child.id === taskId
        );
        if (childTaskIndex !== -1) {
          task.children.splice(childTaskIndex, 1);
          return;
        }
      }
    }
  }
};

// Delete task
const handleDelete = async (row) => {
  try {
    // First check permissions
    if (!canDeleteTask(row)) {
      ElMessageBox.alert(
        'You do not have permission to delete this task. Only the task creator or project owner can delete tasks.',
        'Permission Denied',
        {
          confirmButtonText: 'OK',
          type: 'error',
        }
      );
      return;
    }

    await ElMessageBox.confirm(
      'Are you sure you want to delete this task? This action cannot be undone.',
      'Confirm',
      {
        confirmButtonClass: 'btn-danger',
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
      }
    );

    // Save original data to restore if deletion fails
    const originalIterations = JSON.parse(JSON.stringify(iterations.value));

    // Execute frontend deletion
    deleteTaskFromIterations(iterations.value, row.id);

    // Send deletion request to backend
    const projectId = route.params.id;
    const res = await request.delete(`/projects/${projectId}/tasks/${row.id}`, {
      data: {
        userId: localStorage.getItem('userId'),
      },
    });

    if (res.code !== 1) {
      // If backend deletion fails, restore data
      iterations.value = originalIterations;
      ElMessageBox.alert(
        'Failed to delete the task. Please try again.',
        'Error',
        {
          confirmButtonText: 'OK',
          type: 'error',
        }
      );
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to delete task:', error);
      // Also restore data when error occurs
      iterations.value = originalIterations;
      ElMessageBox.alert(
        'An error occurred while deleting the task. Please try again.',
        'Error',
        {
          confirmButtonText: 'OK',
          type: 'error',
        }
      );
    }
  }
};

// Get iteration data
const fetchIterations = async () => {
  try {
    const projectId = route.params.id;
    const res = await request.get(`/projects/${projectId}/iterations`, {
      params: {
        status: activeStep.value,
      },
    });
    if (res.code === 1) {
      // Sort by id from large to small
      iterations.value = res.data.sort((a, b) => b.id - a.id);
    }
  } catch (error) {
    console.error('Failed to fetch iterations:', error);
    iterations.value = [];
  }
};

// Add handler for creating new task
const handleAddNewTask = async (iteration) => {
  try {
    const projectId = route.params.id;
    // Construct new task data
    const newTaskData = {
      taskId: 0, // Main task taskId is 0
      creatorId: localStorage.getItem('userId'),
      content: 'Double click to edit task content',
      status: 0,
      assigneeId: 0,
      createTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
      iterationId: iteration.id, // Add iterationId
    };
    // Send request to backend
    const res = await request.post(`/projects/${projectId}/tasks`, newTaskData);
    if (res.code === 1 && res.data) {
      // Backend returns new task, add to frontend
      iteration.tasks.push(res.data);
      // Scroll to newly added row
      // nextTick(() => {
      //   const table = backlogTable.value
      //   if (table) {
      //     const scrollBody = table.$el.querySelector('.el-scrollbar__body')
      //     if (scrollBody) {
      //       scrollBody.scrollTo({
      //         top: scrollBody.scrollHeight,
      //         behavior: 'smooth'
      //       })
      //     }
      //   }
      // })
    } else {
      ElMessageBox.alert('Failed to add task, please try again', 'Error', {
        confirmButtonText: 'OK',
        type: 'error',
      });
    }
  } catch (error) {
    ElMessageBox.alert(
      'An error occurred while adding a task, please try again',
      'Error',
      {
        confirmButtonText: 'OK',
        type: 'error',
      }
    );
  }
};

// Get member information
const getMember = (userId) => {
  if (userId === null) return null;
  const member = members.value.find((m) => Number(m.id) === Number(userId));
  return member || null;
};

function openEditDialog(row) {
  editForm.value = {
    id: row.id,
    content: row.content,
    status: String(row.status),
    assigneeId: row.assigneeId,
  };
  editDialogVisible.value = true;
}

const submitEdit = async (taskId) => {
  await editFormRef.value.validate(async (valid) => {
    if (!valid) return;

    editDialogLoading.value = true;
    const projectId = route.params.id;

    try {
      const request = {
        id: taskId,
        content: editForm.value.content,
        status: editForm.value.status,
        assigneeId: Number(editForm.value.assigneeId),
        userId: localStorage.getItem('userId'),
      };

      const res = await request.put(
        `/projects/${projectId}/tasks/${taskId}`,
        request
      );

      if (res.code === 1) {
        // Update local table data after save

        await fetchIterations();
        ElMessage.success('Task updated successfully');
        editDialogVisible.value = false;
        editFormRef.value.resetFields();
      } else {
        ElMessageBox.alert(
          'Failed to update the task. Please try again.',
          'Error',
          {
            confirmButtonText: 'OK',
            type: 'error',
          }
        );
      }
    } catch (error) {
      console.error(error);
      ElMessageBox.alert(
        'An error occurred while updating the task. Please try again.',
        'Error',
        {
          confirmButtonText: 'OK',
          type: 'error',
        }
      );
    } finally {
      editDialogLoading.value = false;
    }
  });
};

function handleEditDialogClose() {
  if (editFormRef.value) editFormRef.value.resetFields();
  editDialogVisible.value = false;
}

// Add subtask method
const handleAddSubTask = async (parentTask, iteration) => {
  if (parentTask.type !== 'task') return; // Only allow main tasks to add subtasks

  try {
    const projectId = route.params.id;
    // Construct subtask data
    const newSubtaskData = {
      creatorId: localStorage.getItem('userId'),
      content: 'Double click to edit subtask content',
      taskId: parentTask.id, // Subtask uses parent task's id
      status: 0,
      assigneeId: 0,
      createTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
      iterationId: iteration.id, // Use the passed-in iteration's id
    };

    // Use the same API endpoint
    const res = await request.post(
      `/projects/${projectId}/tasks`,
      newSubtaskData
    );

    if (res.code === 1 && res.data) {
      // Backend returns new subtask, add to frontend
      if (!parentTask.children) parentTask.children = [];
      parentTask.children.push(res.data);
    } else {
      ElMessageBox.alert('Failed to add subtask, please try again', 'Error', {
        confirmButtonText: 'OK',
        type: 'error',
      });
    }
  } catch (error) {
    ElMessageBox.alert(
      'An error occurred while adding a subtask, please try again',
      'Error',
      {
        confirmButtonText: 'OK',
        type: 'error',
      }
    );
  }
};

const statusMap = { 0: 'To Do', 1: 'In Progress', 2: 'Done' };
</script>

<style scoped>
.backlog-container {
  padding: 24px;
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
}

.backlog-top {
  margin-bottom: 24px;
  text-align: center;
}

.step-bar {
  margin-bottom: 16px;
}

.step-btns {
  display: flex;
  justify-content: center;
  gap: 32px;
  margin-bottom: 32px;
}

.backlog-content {
  flex: 1 1 auto;
  overflow-y: auto;
  min-height: 0;
  border-radius: 12px;
  padding: 24px;
}

.iterations-container {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.iteration-section {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 16px;
}

.interation-header {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.interation-header span {
  font-size: 1.5em;
  color: #303133;
}

.step-bar :deep(.el-step__head.is-process) {
  color: orange !important;
  border-color: orange !important;
}

.step-bar :deep(.el-step__title.is-process) {
  color: orange !important;
}

.step-bar :deep(.el-step__icon.is-process) {
  background-color: orange !important;
  border-color: orange !important;
  color: #fff !important;
}

.new-task-container {
  width: 100%;
  margin-top: 16px;
  display: flex;
  justify-content: center;
}

.editable-cell {
  cursor: pointer;
  padding: 4px 0;
}

.editable-cell:hover {
  background-color: rgba(64, 158, 255, 0.1);
}

.editable-cell .el-input {
  width: 100%;
  height: 30px;
  font-size: 14px;
}

.type-inline {
  display: inline-flex;
  align-items: center;
}

.status-cell {
  display: flex;
  align-items: center;
  justify-content: start;
}

.status-tag {
  min-width: 70px;
  text-align: center;
  font-size: 13px;
}

.assignee-cell {
  display: flex;
  align-items: center;
  justify-content: start;
}

.assignee-option {
  display: flex;
  align-items: center;
  gap: 6px;
}
.assignee-avatar {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 4px;
}
.assignee-display {
  display: flex;
  align-items: center;
  gap: 6px;
}

.plus-height {
  height: 14px;
}
</style>
