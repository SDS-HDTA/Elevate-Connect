<template>
  <div class="backlog-top">
    <div class="step-bar">
      <el-steps :active="activeStep" align-center>
        <el-step v-for="(item, idx) in steps" :key="idx" :title="item" />
      </el-steps>
    </div>
    <div class="step-btns">
      <el-button
        type="primary"
        :disabled="activeStep === 0"
        @click="goPrev"
      >
        <el-icon><ArrowLeft /></el-icon>
        上一步
      </el-button>
      <el-button
        type="primary"
        :disabled="activeStep === steps.length - 1"
        @click="goNext"
      >
        下一步
        <el-icon><ArrowRight /></el-icon>
      </el-button>
    </div>
  </div>
  <!-- 底部内容留空 -->
  <div class="backlog-content"></div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElSteps, ElStep, ElButton, ElIcon } from 'element-plus'
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

// 获取当前项目的status
const fetchStatus = async () => {
  try {
    const projectId = route.params.id
    const res = await request.get(`/projects/${projectId}`)
    if (res.code === 1) {
      activeStep.value = res.data.project.status
    }
  } catch (e) {
    activeStep.value = 0
  }
}

const goPrev = () => {
  if (activeStep.value > 0) {
    activeStep.value--
    // 这里可以加上更新status的接口调用
  }
}
const goNext = () => {
  if (activeStep.value < steps.length - 1) {
    activeStep.value++
    // 这里可以加上更新status的接口调用
  }
}

onMounted(() => {
  fetchStatus()
})
</script>

<style scoped>
.backlog-top {
  margin-top: 32px;
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
  min-height: 300px;
}
</style>
