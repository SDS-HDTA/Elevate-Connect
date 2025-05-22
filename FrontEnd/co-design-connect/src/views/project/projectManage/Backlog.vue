<template>
  <div>
    <!-- 阶段流程条 -->
    <div class="stage-bar">
      <el-steps style="max-width: 600px" :active="0" align-center>
        <el-step title="Empathise"  />
        <el-step title="Discover"  />
        <el-step title="Define"  />
        <el-step title="Ideate"  />
        <el-step title="Prototype" />
        <el-step title="Feedback" />
      </el-steps>
    </div>
    <!-- 负责人操作按钮 -->
    <div v-if="isCreator" class="stage-actions">
      <button @click="confirmChangeStage('next')" :disabled="currentStage === stages.length - 1">前往下一阶段</button>
      <button @click="confirmChangeStage('prev')" :disabled="currentStage === 0">返回上一阶段</button>
    </div>
    <!-- 确认弹窗 -->
    <div v-if="showConfirm" class="modal">
      <div class="modal-content">
        <p>确定要{{ actionType === 'next' ? '前往下一阶段' : '返回上一阶段' }}吗？</p>
        <button @click="changeStage">确定</button>
        <button @click="showConfirm = false">取消</button>
      </div>
    </div>
    <div class="backlog-container">
      <h2>Backlog</h2>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'
import { useRoute } from 'vue-router'
import { ElSteps, ElStep } from 'element-plus'

const route = useRoute()
const projectId = route.params.projectId
const stages = ['Empathise', 'Discover', 'Define', 'Ideate', 'Prototype', 'Feedback']
const currentStage = ref(0)
const showConfirm = ref(false)
const actionType = ref('next') // 'next' or 'prev'

// 判断是否为负责人
const userId = localStorage.getItem('userId')
const creatorId = localStorage.getItem(`project_${projectId}_creatorId`)
const isCreator = userId === creatorId

// 获取当前阶段
const fetchStage = async () => {
  const res = await request.get(`/project/${projectId}/stage`)
  currentStage.value = res.data.stageIndex // 假设接口返回 { data: { stageIndex: 0 } }
}

onMounted(() => {
  fetchStage()
})

// 弹窗确认
const confirmChangeStage = (type) => {
  actionType.value = type
  showConfirm.value = true
}

// 切换阶段
const changeStage = async () => {
  let newStage = currentStage.value
  if (actionType.value === 'next') newStage++
  else if (actionType.value === 'prev') newStage--
  await request.post(`/project/${projectId}/stage`, { stageIndex: newStage })
  currentStage.value = newStage
  showConfirm.value = false
}
</script>

<style scoped>
.backlog-container {
  padding: 20px;
}

/* 阶段流程条整体居中且更宽 */
.stage-bar {
  width: 100%;
  align-items: center;
  margin-bottom: 32px;
}

.stage-dot.active {
  background: #e67c1b;
  border: 2px solid #e67c1b;
}


.stage-actions { margin-bottom: 16px; }
.modal { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.3); display: flex; align-items: center; justify-content: center; }
.modal-content { background: #fff; padding: 24px; border-radius: 8px; }
</style> 