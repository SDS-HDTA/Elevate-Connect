<template>
  <div class="channel-container">
    <div v-for="topic in topics" :key="topic.id" class="topic-block">
      <!-- 主题发起人和日期 -->
      <div class="topic-header">
        <span class="topic-creator">{{ topic.creatorName }}</span>
        <span class="topic-date">{{ formatDate(topic.createTime) }}</span>
      </div>
      <!-- 主题标题 -->
      <h2 class="topic-title">{{ topic.title }}</h2>
      <!-- 主题描述 -->
      <div class="topic-desc">{{ topic.description }}</div>
      <!-- 回复区 -->
      <div class="messages">
        <template v-for="(msg, idx) in topic.messages" :key="msg.id">
          <el-divider v-if="idx !== 0" />
          <div class="message-item">
            <div class="msg-header">
              <span class="msg-name">{{ msg.senderName }}</span>
              <span class="msg-date">{{ formatDate(msg.createTime) }}</span>
            </div>
            <div class="msg-content">{{ msg.content }}</div>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElDivider } from 'element-plus'
import request from '@/utils/request'

// 示例数据
const topics = ref([
  {
    id: 1,
    title: '利率与贴现率的区别',
    description: '请问名义利率和实际利率在NPV计算中怎么选？',
    creatorName: '张三',
    createTime: '2024-06-01T10:00:00',
    messages: [
      {
        id: 101,
        content: '一般用实际利率，除非现金流已包含通胀。',
        senderName: '李四',
        createTime: '2024-06-01T10:05:00'
      }
    ]
  },
  {
    id: 2,
    title: '折旧方法讨论',
    description: '直线法和加速折旧法在财务报表上有啥影响？',
    creatorName: '王五',
    createTime: '2024-06-01T11:00:00',
    messages: [
      {
        id: 201,
        content: '直线法更简单，折旧额每年一样。',
        senderName: '赵六',
        createTime: '2024-06-01T11:10:00'
      },
      {
        id: 202,
        content: '加速折旧前期费用高，后期低，对税有影响。',
        senderName: '钱七',
        createTime: '2024-06-01T11:15:00'
      }
    ]
  }
])

// 时间格式化
function formatDate(dateStr) {
  const d = new Date(dateStr)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}

onMounted(async () => {
  // 真实请求时再覆盖
  const res = await request.get('/api/project/channel')
  topics.value = (res.topics || []).map(topic => ({
    ...topic,
    messages: (topic.messages || []).sort((a, b) => new Date(a.createTime) - new Date(b.createTime))
  }))
})
</script>

<style scoped>
.channel-container {
  background: #fff;
  padding: 24px;
  min-height: 600px;
}
.topic-block {
  margin-bottom: 40px;
  border-bottom: 1px solid #eee;
  padding-bottom: 24px;
}
.topic-header {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 16px;
  color: #666;
  margin-bottom: 8px;
}
.topic-creator {
  font-weight: bold;
}
.topic-date {
  color: #999;
}
.topic-title {
  font-size: 2em;
  font-weight: bold;
  margin: 0 0 12px 0;
}
.topic-desc {
  background: #f7f7f7;
  border-radius: 6px;
  padding: 14px 18px;
  margin-bottom: 18px;
  font-size: 16px;
  color: #333;
}
.messages {
  margin-top: 8px;
}
.message-item {
  padding: 10px 0;
}
.msg-header {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 16px;
  color: #555;
  margin-bottom: 4px;
}
.msg-name {
  font-weight: bold;
}
.msg-date {
  color: #aaa;
}
.msg-content {
  font-size: 16px;
  color: #333;
  padding-left: 2px;
}
</style>
