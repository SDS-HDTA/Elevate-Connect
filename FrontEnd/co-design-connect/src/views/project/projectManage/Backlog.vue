<template>
  <div class="backlog-container">
    <div class="backlog-top">
      <div class="step-bar">
        <el-steps :active="activeStep" align-center finish-status="success">
          <el-step v-for="(item, idx) in steps" :key="idx" :title="item" />
        </el-steps>
      </div>
      <div class="step-btns" v-if="isCreator">
        <el-button
          type="primary"
          :disabled="activeStep === 0"
          @click="handlePrev"
        >
          <el-icon><ArrowLeft /></el-icon>
          Go Back
        </el-button>
        <el-button
          type="primary"
          :disabled="activeStep === steps.length - 1"
          @click="handleNext"
        >
          Go Next
          <el-icon><ArrowRight /></el-icon>
        </el-button>
      </div>
    </div>
    <!-- 底部内容区域 -->
    <div class="backlog-content" >
        <div class="backlogs-scroll-area-y" ref="backlogsScrollArea">
          <div class="backlogs-scroll-area-x" style="width: 100%; overflow-x: auto;">
            <div v-for="iteration in iterations" :key="iteration.id" class="iteration-section">
              <div class="interation-header">
                <span>Iteration {{ iteration.name }}</span>
              </div>
              <el-table
                :data="iteration.tasks"
                row-key="id"
                border
                max-height="400"
                style="width: 100%;"
                :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
                @selection-change="handleSelectionChange"
                ref="backlogTable"
              >
                <el-table-column type="selection" width="50" />
                <el-table-column prop="code" label="Code" min-width="120" />
                <el-table-column prop="creator" label="Creator" min-width="100" />
                <el-table-column prop="content" label="Content" min-width="200" />
                <el-table-column prop="status" label="Status" min-width="80" />
                <el-table-column prop="assignee" label="Assignee" min-width="100"/>
                <el-table-column label="Operation" width="100">
                  <template #default="scope">
                    <el-button
                      link
                      type="danger"
                      size="middle"
                      @click="handleDelete(scope.row)"
                    >
                      Delete
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { ElSteps, ElStep, ElButton, ElIcon, ElMessageBox } from 'element-plus'
import { ArrowLeft, ArrowRight } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { useRoute, useRouter } from 'vue-router'

const steps = [
  'Empathise',
  'Discover',
  'Define',
  'Ideate',
  'Prototype',
  'Feedback'
]
const activeStep = ref(0)
const route = useRoute()
const router = useRouter()
const isCreator = ref(false)

const iterations = ref([
  {
    id: 1,
    name: "1",
    tasks: [
      {
        id: 1,
        code: 'UTS41113-79',
        creator: 'Yaohong Ge',
        creatorId: 'user_001',
        content: 'Complete the project creation test',
        status: 'DONE',
        assignee: 'Yaohong Ge',
        children: [
          {
            id: 2,
            code: 'UTS41113-80',
            creator: 'Yaohong Ge',
            creatorId: 'user_001',
            content: 'Complete the test of communication within the project',
            status: 'DONE',
            assignee: 'Yaohong Ge'
          }
        ]
      },
      {
        id: 3,
        code: 'UTS41113-81',
        creator: 'Yaohong Ge',
        creatorId: 'user_001',
        content: 'Solve the problem of front-end and back-end standardization',
        status: 'DONE',
        assignee: 'Yaohong Ge'
      }
    ]
  },
  {
    id: 2,
    name: "2",
    tasks: [
      {
        id: 4,
        code: 'UTS41113-60',
        creator: 'Mingrui Qi',
        creatorId: 'user_002',
        content: 'Develop core apis for main interface, function jump, project ...',
        status: 'DONE',
        assignee: 'Mingrui Qi'
      },
      {
        id: 5,
        code: 'UTS41113-65',
        creator: 'Mingrui Qi',
        creatorId: 'user_002',
        content: 'Design and implement a new model for project and member ...',
        status: 'DONE',
        assignee: 'Mingrui Qi'
      }
    ]
  }
])

const selectedRows = ref([])

const handleSelectionChange = (val) => {
  selectedRows.value = val
}

// 获取当前项目的status
const fetchStatus = async () => {
  try {
    const projectId = route.params.id
    const res = await request.get(`/projects/${projectId}/status`)
    if (res.code === 1) {
      activeStep.value = res.data.status
      console.log(activeStep.value)
    }
  } catch (e) {
    activeStep.value = 0
    isCreator.value = false
  }
}

// 更新项目状态
const updateProjectStatus = async (newStatus) => {
  try {
    const projectId = route.params.id
    const res = await request.put(`/projects/${projectId}/status`, {
      status: newStatus
    })
    if (res.code === 1) {
      activeStep.value = newStatus
    }
  } catch (error) {
    console.error('Failed to update project status:', error)
  }
}

const handlePrev = () => {
  if (activeStep.value > 0) {
    ElMessageBox.confirm(
      'Are you sure you want to go back?',
      'Confirm',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }
    ).then(() => {
      const newStatus = activeStep.value - 1
      updateProjectStatus(newStatus)
    }).catch(() => {})
  }
}

const handleNext = () => {
  if (activeStep.value < steps.length - 1) {
    ElMessageBox.confirm(
      '确定要进入下一步吗？',
      '确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    ).then(() => {
      const newStatus = activeStep.value + 1
      updateProjectStatus(newStatus)
    }).catch(() => {})
  }
}

const checkIsCreator = () => {
  const projectId = route.params.id
  const creatorId = localStorage.getItem(`project_${projectId}_creatorId`)
  const currentUserId = localStorage.getItem('userId')
  isCreator.value = creatorId === currentUserId
}

const backlogTable = ref(null)

onMounted(() => {
  fetchStatus()
  fetchIterations()
  checkIsCreator()
})

// 检查是否有权限删除任务
const canDeleteTask = (task) => {
  const currentUserId = localStorage.getItem('userId')
  // 如果是项目创建者，可以删除任何任务
  if (isCreator.value) {
    return true
  }
  // 如果是任务创建者，可以删除自己的任务
  return task.creatorId === currentUserId
}

// 在前端删除数据
const deleteTaskFromIterations = (iterations, taskId) => {
  for (let iteration of iterations) {
    // 检查主任务
    const mainTaskIndex = iteration.tasks.findIndex(task => task.id === taskId)
    if (mainTaskIndex !== -1) {
      // 如果是主任务，直接删除整个任务（包括子任务）
      iteration.tasks.splice(mainTaskIndex, 1)
      return
    }
    // 检查子任务
    for (let task of iteration.tasks) {
      if (task.children) {
        const childTaskIndex = task.children.findIndex(child => child.id === taskId)
        if (childTaskIndex !== -1) {
          task.children.splice(childTaskIndex, 1)
          return
        }
      }
    }
  }
}

// 删除任务
const handleDelete = async (row) => {
  try {
    // 首先检查权限
    if (!canDeleteTask(row)) {
      ElMessageBox.alert(
        'You do not have permission to delete this task. Only the task creator or project owner can delete tasks.',
        'Permission Denied',
        {
          confirmButtonText: 'OK',
          type: 'warning'
        }
      )
      return
    }

    await ElMessageBox.confirm(
      'Are you sure you want to delete this task?',
      'Confirm',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }
    )
    
    // 保存原始数据，以便在删除失败时恢复
    const originalIterations = JSON.parse(JSON.stringify(iterations.value))
    
    // 执行前端删除
    deleteTaskFromIterations(iterations.value, row.id)
    
    // 发送删除请求到后端
    const projectId = route.params.id
    const res = await request.delete(`/projects/${projectId}/tasks/${row.id}`)
    
    if (res.code !== 1) {
      // 如果后端删除失败，恢复数据
      iterations.value = originalIterations
      ElMessageBox.alert(
        'Failed to delete the task. Please try again.',
        'Error',
        {
          confirmButtonText: 'OK',
          type: 'error'
        }
      )
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to delete task:', error)
      // 发生错误时也恢复数据
      iterations.value = originalIterations
      ElMessageBox.alert(
        'An error occurred while deleting the task. Please try again.',
        'Error',
        {
          confirmButtonText: 'OK',
          type: 'error'
        }
      )
    }
  }
}

// 获取迭代数据
const fetchIterations = async () => {
  try {
    const projectId = route.params.id
    const res = await request.get(`/projects/${projectId}/iterations`, {
      params: {
        status: activeStep.value
      }
    })
    if (res.code === 1) {
      iterations.value = res.data
    }
  } catch (error) {
    console.error('Failed to fetch iterations:', error)
    iterations.value = []
  }
}

</script>

<style scoped>
.backlog-container {
  background: #fff;
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
  background-color: #fafafa;
  padding: 24px;
}

.content-block {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 24px;
}

.content-header {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e4e7ed;
}

.content-header h2 {
  margin: 0;
  font-size: 1.5em;
  color: #303133;
}

.content-body {
  min-height: 200px;
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

.iteration-section {
  margin-bottom: 24px;
}

.iteration-section:last-child {
  margin-bottom: 0;
}

</style>
