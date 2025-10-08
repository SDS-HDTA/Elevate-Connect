<template>
  <div class="backlog-container">
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
                <div
                  class="editable-cell"
                  @dblclick="handleEdit(scope.row, 'content')"
                >
                  <span
                    v-if="
                      !scope.row.isEditing ||
                      scope.row.editingField !== 'content'
                    "
                  >
                    {{ scope.row.content }}
                  </span>
                  <el-input
                    v-else
                    v-model="scope.row.content"
                    size="small"
                    @blur="handleSave(scope.row, 'content')"
                    @keyup.enter="handleSave(scope.row, 'content')"
                  />
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="Status" min-width="120">
              <template #default="scope">
                <div class="status-cell">
                  <el-select
                    v-if="
                      scope.row.isEditing && scope.row.editingField === 'status'
                    "
                    v-model="scope.row._statusStr"
                    size="small"
                    @blur="handleSave(scope.row, 'status')"
                    @change="handleSave(scope.row, 'status')"
                    @visible-change="
                      (val) => {
                        if (!val) handleSave(scope.row, 'status');
                      }
                    "
                    style="width: 100px"
                    filterable="{false}"
                    placeholder="Select Status"
                  >
                    <el-option label="TO DO" value="TO DO" />
                    <el-option label="IN PROGRESS" value="IN PROGRESS" />
                    <el-option label="DONE" value="DONE" />
                  </el-select>
                  <el-tag
                    v-else
                    :type="
                      scope.row.status === 2
                        ? 'success'
                        : scope.row.status === 1
                          ? 'warning'
                          : 'info'
                    "
                    class="status-tag"
                    @dblclick="handleEdit(scope.row, 'status')"
                    style="cursor: pointer"
                  >
                    {{ statusMap[scope.row.status] }}
                  </el-tag>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="assigneeId" label="Assignee" min-width="140">
              <template #default="scope">
                <div class="assignee-cell">
                  <el-select
                    v-if="
                      scope.row.isEditing &&
                      scope.row.editingField === 'assignee'
                    "
                    v-model="scope.row._assigneeId"
                    size="small"
                    filterable
                    placeholder="Select members"
                    style="width: 120px"
                    @change="handleSave(scope.row, 'assignee')"
                    @blur="handleSave(scope.row, 'assignee')"
                    @visible-change="
                      (val) => {
                        if (!val) handleSave(scope.row, 'assignee');
                      }
                    "
                  >
                    <el-option
                      v-for="member in members"
                      :key="member.id"
                      :label="member.fullName"
                      :value="Number(member.id)"
                    >
                      <div class="assignee-option">
                        <Avatar :full-name="member.fullName" :size="20" />
                        <span>{{ member.fullName }}</span>
                      </div>
                    </el-option>
                  </el-select>
                  <div
                    v-else
                    class="assignee-display"
                    @dblclick="handleEdit(scope.row, 'assignee')"
                  >
                    <Avatar
                      :full-name="
                        getMember(scope.row.assigneeId)?.fullName ||
                        scope.row.assigneeId
                      "
                      :size="20"
                      v-if="getMember(scope.row.assigneeId) !== null"
                    />
                    <span>{{
                      scope.row.assigneeId === null
                        ? 'Unknown'
                        : getMember(scope.row.assigneeId)?.fullName ||
                          scope.row.assigneeId
                    }}</span>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              prop="createTime"
              label="Create Time"
              min-width="100"
            >
              <template #default="scope">
                {{ formatDate(scope.row.createTime) }}
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElSteps, ElStep, ElButton, ElIcon, ElMessageBox } from 'element-plus';
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

const iterations = ref([]);

const selectedRows = ref([]);

const handleSelectionChange = (val) => {
  selectedRows.value = val;
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

// Add date formatting function
const formatDate = (dateString) => {
  if (!dateString) return '';
  // Only return YYYY-MM-DD
  return dateString.slice(0, 10);
};

// Get member information
const getMember = (userId) => {
  if (userId === null) return null;
  const member = members.value.find((m) => Number(m.id) === Number(userId));
  return member || null;
};

// Add editing related functions
const handleEdit = (row, field) => {
  // If it is a code field, return directly, no editing allowed
  if (field === 'code') {
    return;
  }

  if (!canDeleteTask(row)) {
    ElMessageBox.alert(
      'You do not have permission to edit this task. Only the task creator or project owner can edit tasks.',
      'Permission Denied',
      {
        confirmButtonText: 'OK',
        type: 'error',
      }
    );
    return;
  }
  row.isEditing = true;
  row.editingField = field;
  row.originalValue = row[field];
  if (field === 'status') {
    row._statusStr = statusMap[row.status];
  }
  if (field === 'assignee') {
    row._assigneeId = row.assigneeId;
  }
};

const handleSave = async (row, field) => {
  try {
    if (field === 'status') {
      const newStatusNum = statusReverseMap[row._statusStr];
      if (newStatusNum === row.status) {
        row.isEditing = false;
        row.editingField = null;
        return;
      }
      const projectId = route.params.id;
      const res = await request.put(`/projects/${projectId}/tasks/${row.id}`, {
        status: newStatusNum, // Use number type directly
        userId: localStorage.getItem('userId'),
      });
      if (res.code === 1) {
        row.status = newStatusNum;
        row.isEditing = false;
        row.editingField = null;
      } else {
        row._statusStr = statusMap[row.status];
        ElMessageBox.alert(
          'Failed to update the task. Please try again.',
          'Error',
          {
            confirmButtonText: 'OK',
            type: 'error',
          }
        );
      }
    } else if (field === 'assignee') {
      if (row._assigneeId === row.assigneeId) {
        row.isEditing = false;
        row.editingField = null;
        return;
      }
      const projectId = route.params.id;
      const res = await request.put(`/projects/${projectId}/tasks/${row.id}`, {
        assigneeId: Number(row._assigneeId), // Ensure assigneeId is a number type
        userId: localStorage.getItem('userId'),
      });
      if (res.code === 1) {
        row.assigneeId = row._assigneeId;
        row.isEditing = false;
        row.editingField = null;
      } else {
        ElMessageBox.alert(
          'Failed to update the assignee. Please try again.',
          'Error',
          {
            confirmButtonText: 'OK',
            type: 'error',
          }
        );
      }
    } else {
      if (row[field] === row.originalValue) {
        row.isEditing = false;
        row.editingField = null;
        return;
      }
      const projectId = route.params.id;
      const res = await request.put(`/projects/${projectId}/tasks/${row.id}`, {
        [field]: row[field],
        userId: localStorage.getItem('userId'),
      });
      if (res.code === 1) {
        row.isEditing = false;
        row.editingField = null;
      } else {
        row[field] = row.originalValue;
        ElMessageBox.alert(
          'Failed to update the task. Please try again.',
          'Error',
          {
            confirmButtonText: 'OK',
            type: 'error',
          }
        );
      }
    }
  } catch (error) {
    row[field] = row.originalValue;
    ElMessageBox.alert(
      'An error occurred while updating the task. Please try again.',
      'Error',
      {
        confirmButtonText: 'OK',
        type: 'error',
      }
    );
  } finally {
    row.isEditing = false;
    row.editingField = null;
  }
};

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

const statusMap = { 0: 'TO DO', 1: 'IN PROGRESS', 2: 'DONE' };
const statusReverseMap = { 'TO DO': 0, 'IN PROGRESS': 1, DONE: 2 };
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
  justify-content: center;
}
.status-tag {
  min-width: 70px;
  text-align: center;
  font-size: 13px;
  letter-spacing: 1px;
}

.assignee-cell {
  display: flex;
  align-items: center;
  justify-content: center;
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
