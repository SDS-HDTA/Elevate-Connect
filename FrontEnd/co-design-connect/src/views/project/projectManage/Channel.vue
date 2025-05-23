<template>
  <div class="channel-container">
    <div class="posts-scroll-area" ref="postsScrollArea">
      <div v-for="post in posts" :key="post.id" class="post-block">
        <!-- 主题发起人和日期 -->
        <div class="post-header">
          <span class="post-creator">{{ post.creatorName }}</span>
          <span class="post-date">{{ formatDate(post.createTime) }}</span>
        </div>
        <!-- 主题标题 -->
        <h3 class="post-title">{{ post.title }}</h3>
        <!-- 主题描述 -->
        <div class="post-desc">{{ post.description }}</div>
        <!-- 回复区 -->
        <el-divider class="post-divider" />
        <div class="messages">
          <template v-for="(msg, idx) in post.messages" :key="msg.id">
            <div
              class="message-item"
              :style="{ '--msg-color': getAvatarColor(msg.senderName) }"
            >
              <div class="msg-header">
                <div class="msg-avatar">
                  <Avatar :username="msg.senderName" :size="28" />
                </div>
                <span class="msg-name">{{ msg.senderName }}</span>
                <span class="msg-date">{{ formatMsgDate(msg.createTime) }}</span>
              </div>
              <div class="msg-content">{{ msg.content }}</div>
            </div>
          </template>
        </div>
        <!-- 回复按钮和输入框 -->
        <div class="reply-row">
          <el-button size="small" type="primary" @click="showReplyInput(post.id)">Reply</el-button>
        </div>
        <div v-if="replyingPostId === post.id" class="reply-input-area">
          <el-input
            v-model="replyContent"
            placeholder="Type your message here..."
            size="large"
            class="reply-input"
            @keyup.enter="submitReply(post)"
            :autosize="{ minRows: 1, maxRows: 3 }"
            type="textarea"
          />
          <el-button size="middle" type="success" @click="submitReply(post)">Send</el-button>
          <el-button size="middle" @click="cancelReply">Cancel</el-button>
        </div>
      </div>
    </div>

    <!-- 发帖输入框/按钮区域 -->
    <div class="create-post-area">
      <template v-if="creatingPost">
        <div class="create-post-card">
          <div class="create-post-header">
            <div class="user-info">
              <Avatar :username="username" :size="32" />
              <span class="user-name">{{ username }}</span>
            </div>
            <el-button class="close-btn" @click="cancelCreatePost" circle>
              <el-icon size="20"><Close /></el-icon>
            </el-button>
          </div>
          <el-input
            v-model="newPostTitle"
            placeholder="Add a title"
            size="large"
            class="create-post-title-input"
            maxlength="50"
          />
          <el-divider class="create-post-divider" />
          <el-input
            v-model="newPostDescription"
            placeholder="Type your message here..."
            size="large"
            class="create-post-desc-input"
            :autosize="{ minRows: 4, maxRows: 8 }"
            type="textarea"
            maxlength="200"
          />
          <div class="create-post-footer">
            <el-button size="large" type="success" class="send-btn" @click="submitNewPost">Post</el-button>
          </div>
        </div>
      </template>
      <el-button
        v-else
        class="create-post-btn"
        type="primary"
        size="large"
        @click="onCreatePost"
      >
        <i class="el-icon-edit" style="margin-right:6px;"></i>
        Start a post
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { ElDivider, ElButton, ElInput, ElMessage } from 'element-plus'
import Avatar from '@/components/Avatar.vue'
import request from '@/utils/request'
import { useRoute } from 'vue-router'
import { Close } from '@element-plus/icons-vue'

const route = useRoute()
const postsScrollArea = ref(null)
const ws = ref(null)

// 示例数据
// const posts = ref([
//   {
//     id: 1,
//     title: '利率与贴现率的区别',
//     description: '请问名义利率和实际利率在NPV计算中怎么选？',
//     creatorName: '张三',
//     createTime: '2024-06-01T10:00:00',
//     messages: [
//       {
//         id: 101,
//         content: '一般用实际利率，除非现金流已包含通胀。',
//         senderName: '李四',
//         createTime: '2024-06-01T10:05:00'
//       },
//       {
//         id: 102,
//         content: '补充一下，实际利率 = 名义利率 - 通货膨胀率',
//         senderName: '王五',
//         createTime: '2024-06-01T10:15:00'
//       }
//     ]
//   },
//   {
//     id: 2,
//     title: '折旧方法讨论',
//     description: '直线法和加速折旧法在财务报表上有啥影响？',
//     creatorName: '王五',
//     createTime: '2024-06-01T11:00:00',
//     messages: [
//       {
//         id: 201,
//         content: '直线法更简单，折旧额每年一样。',
//         senderName: '赵六',
//         createTime: '2024-06-01T11:10:00'
//       },
//       {
//         id: 202,
//         content: '加速折旧前期费用高，后期低，对税有影响。',
//         senderName: '钱七',
//         createTime: '2024-06-01T11:15:00'
//       },
//       {
//         id: 203,
//         content: '建议根据资产使用情况选择，如果资产前期使用强度大，用加速折旧更合理。',
//         senderName: '孙八',
//         createTime: '2024-06-01T11:30:00'
//       }
//     ]
//   },
//   {
//     id: 3,
//     title: '项目风险评估方法',
//     description: '大家平时都用什么方法做项目风险评估？',
//     creatorName: '李四',
//     createTime: '2024-06-02T09:00:00',
//     messages: [
//       {
//         id: 301,
//         content: '我们公司主要用德尔菲法，邀请专家打分。',
//         senderName: '周九',
//         createTime: '2024-06-02T09:10:00'
//       },
//       {
//         id: 302,
//         content: '我们用的是蒙特卡洛模拟，可以量化风险。',
//         senderName: '吴十',
//         createTime: '2024-06-02T09:20:00'
//       }
//     ]
//   },
//   {
//     id: 4,
//     title: '投资回收期计算',
//     description: '动态投资回收期和静态投资回收期哪个更准确？',
//     creatorName: '赵六',
//     createTime: '2024-06-02T14:00:00',
//     messages: [
//       {
//         id: 401,
//         content: '动态回收期考虑了时间价值，更准确。',
//         senderName: '郑十一',
//         createTime: '2024-06-02T14:05:00'
//       },
//       {
//         id: 402,
//         content: '但静态回收期计算简单，适合快速评估。',
//         senderName: '王十二',
//         createTime: '2024-06-02T14:10:00'
//       },
//       {
//         id: 403,
//         content: '建议两个都算，互相验证。',
//         senderName: '李十三',
//         createTime: '2024-06-02T14:15:00'
//       }
//     ]
//   },
//   {
//     id: 5,
//     title: '敏感性分析问题',
//     description: '做敏感性分析时，关键变量如何选择？',
//     creatorName: '钱七',
//     createTime: '2024-06-03T10:00:00',
//     messages: [
//       {
//         id: 501,
//         content: '通常选择对项目影响最大的变量，如价格、成本等。',
//         senderName: '孙十四',
//         createTime: '2024-06-03T10:10:00'
//       },
//       {
//         id: 502,
//         content: '还要考虑变量的不确定性，波动大的变量要重点分析。',
//         senderName: '周十五',
//         createTime: '2024-06-03T10:20:00'
//       },
//       {
//         id: 503,
//         content: '建议用蛛网图展示多变量敏感性分析结果。',
//         senderName: '吴十六',
//         createTime: '2024-06-03T10:30:00'
//       }
//     ]
//   }
// ])

const replyingPostId = ref(null)
const replyContent = ref('')
const creatingPost = ref(false)
const newPostTitle = ref('')
const newPostDescription = ref('')
const username = ref(localStorage.getItem('username'))
const channelId = ref(0)

// WebSocket 连接管理
function initWebSocket() {
  const projectId = route.params.id
  console.log(projectId)
  const wsUrl = `ws://localhost:8080/projects/${projectId}/channel`
  ws.value = new WebSocket(wsUrl)
  ws.value.onopen = () => {
    console.log('WebSocket 连接已建立')
    
  }
  
  ws.value.onmessage = (event) => {
    const data = JSON.parse(event.data)
    handleWebSocketMessage(data)
  }
  
  ws.value.onerror = (error) => {
    console.error('WebSocket error:', error)
    ElMessage.error('Real-time communication connection error')
  }
  
  ws.value.onclose = () => {
    console.log('WebSocket connection closed')
  }
}

// 处理接收到的 WebSocket 消息
function handleWebSocketMessage(data) {
  switch (data.type) {
    case 'new_message':
      // 处理新消息
      const post = posts.value.find(p => p.id === data.postId)
      if (post) {
        post.messages.push(data.message)
        // nextTick(() => {
        //   if (postsScrollArea.value) {
        //     postsScrollArea.value.scrollTop = postsScrollArea.value.scrollHeight
        //   }
        // })
      }
      break
    case 'new_post':
      // 处理新帖子
      posts.value.push(data.post)
      nextTick(() => {
        if (postsScrollArea.value) {
          postsScrollArea.value.scrollTop = postsScrollArea.value.scrollHeight
        }
      })
      break
    case 'delete_message':
      // 处理删除消息
      const targetPost = posts.value.find(p => p.id === data.postId)
      if (targetPost) {
        targetPost.messages = targetPost.messages.filter(m => m.id !== data.messageId)
      }
      break
    case 'delete_post':
      // 处理删除帖子
      posts.value = posts.value.filter(p => p.id !== data.postId)
      break
  }
}

// 发送 WebSocket 消息
function sendWebSocketMessage(type, data) {
  if (ws.value && ws.value.readyState === WebSocket.OPEN) {
    ws.value.send(JSON.stringify({
      type,
      ...data
    }))
  } else {
    ElMessage.warning('Real-time communication not connected, please refresh the page and try again')
  }
}

function showReplyInput(postId) {
  replyingPostId.value = postId
  replyContent.value = ''
  nextTick(() => {
    // 自动聚焦输入框
    const input = document.querySelector('.reply-input input')
    if (input) input.focus()
  })
}

function cancelReply() {
  replyingPostId.value = null
  replyContent.value = ''
}

// 格式化时间为 YYYY-MM-DD HH:mm:ss 格式
function formatDateTime(date) {
  const d = new Date(date)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}:${String(d.getSeconds()).padStart(2, '0')}`
}

// 修改提交回复函数
async function submitReply(post) {
  const content = replyContent.value.trim()
  if (!content) {
    ElMessage.warning('Reply content cannot be empty')
    return
  }
  try {
    const formData = new URLSearchParams()
    formData.append('postId', post.id)
    formData.append('content', content)
    formData.append('createTime', formatDateTime(new Date()))
    formData.append('userId', localStorage.getItem('userId'))

    const res = await request.post(`/projects/${route.params.id}/channel/reply`, formData)
    
    if (res.code === 1) {
      // 通过 WebSocket 发送新消息
      sendWebSocketMessage('new_message', {
        message: res.data
      })
    }
    else {
      ElMessage.error('Failed to reply: '+res.message)
    }
    replyContent.value = ''
    replyingPostId.value = null
  } catch (e) {
    ElMessage.error('Reply failed, please try again: '+e)
  }
}

// 时间格式化
function formatDate(dateStr) {
  const d = new Date(dateStr)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}

// 获取头像颜色（与Avatar.vue一致）
function getAvatarColor(name) {
  let hash = 0
  for (let i = 0; i < name.length; i++) {
    hash = name.charCodeAt(i) + ((hash << 5) - hash)
  }
  let color = '#'
  for (let i = 0; i < 3; i++) {
    color += ('00' + ((hash >> (i * 8)) & 0xff).toString(16)).slice(-2)
  }
  return color
}

// 格式化消息日期：只显示月-日 时:分
function formatMsgDate(dateStr) {
  const d = new Date(dateStr)
  return `${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}

function onCreatePost() {
  creatingPost.value = true
  newPostTitle.value = ''
  newPostDescription.value = ''
  nextTick(() => {
    const input = document.querySelector('.create-post-title-input input')
    if (input) input.focus()
  })
}

function cancelCreatePost() {
  creatingPost.value = false
  newPostTitle.value = ''
  newPostDescription.value = ''
}

async function submitNewPost() {
  const title = newPostTitle.value.trim()
  const description = newPostDescription.value.trim()
  if (!title) {
    ElMessage.warning('Title cannot be empty')
    return
  }
  if (!description) {
    ElMessage.warning('Description cannot be empty')
    return
  }
  try {
    const formData = new URLSearchParams()
    formData.append('title', title)
    formData.append('description', description)
    formData.append('createTime', formatDateTime(new Date()))
    formData.append('userId', localStorage.getItem('userId'))
    formData.append('channelId', channelId.value)

    const res = await request.post(`/projects/${route.params.id}/channel/post`, formData)
    if (res.code === 1) {
      // 通过 WebSocket 发送新帖子
      sendWebSocketMessage('new_post', {
        post: res.data
      })
    }
    else {
      ElMessage.error('Failed to post: '+res.message)
    }
    creatingPost.value = false
    newPostTitle.value = ''
    newPostDescription.value = ''
  } catch (e) {
    ElMessage.error('Failed to post: '+e)
  }
}

onMounted(async () => {
  const projectId = route.params.id
  try {
    const res = await request.get(`/projects/${projectId}/channel`)
    if (res.code === 1) {
      posts.value = res.data.map(post => ({
        ...post,
        messages: post.messages.sort((a, b) => new Date(a.createTime) - new Date(b.createTime))
      })).sort((a, b) => b.id - a.id)
      channelId.value = res.data.id
    }
    
    // 初始化 WebSocket 连接
    initWebSocket()
  } catch (error) {
    ElMessage.error('Failed to get channel information')
    console.error('Failed to get channel information:', error)
  }

  await nextTick()
  const container = postsScrollArea.value
  if (container) {
    container.scrollTop = container.scrollHeight
  }
})

// 组件卸载时关闭 WebSocket 连接
onUnmounted(() => {
  if (ws.value) {
    ws.value.close()
  }
})
</script>

<style scoped>
.channel-container {
  background: #fff;
  padding: 24px;
  height: 100%;
  position: relative;
  display: flex;
  flex-direction: column;
}

.posts-scroll-area {
  flex: 1 1 auto;
  overflow-y: auto;
  min-height: 0;
  border-radius: 12px;
  margin-bottom: 16px;
  padding: 0 0 0 0;
}

.post-block {
  margin-bottom: 32px;
  padding: 24px;
  background-color: #fafafa;
  border-radius: 8px;
}


.post-header {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 16px;
  color: #666;
  margin-bottom: 8px;
}

.post-creator {
  font-weight: bold;
}

.post-date {
  color: #999;
}

.post-title {
  font-size: 1.3em;
  font-weight: bold;
  margin: 0 0 12px 0;
}

.post-desc {
  border-radius: 6px;
  padding: 14px 18px;
  font-size: 16px;
  color: #333;
  background: #f7f7f7;
}

.post-divider {
  margin-top: 5px;
  margin-bottom: 5px;
}


.messages {
  margin-top: 8px;
}

.message-item {
  display: flex;
  flex-direction: column;
  position: relative;
  border-radius: 8px;
  margin-bottom: 16px;
  transition: background 0.2s, color 0.2s;
  padding-left: 16px;
  border-left: 6px solid var(--msg-color, #409eff);
}
.message-item:hover {
  background: rgb(195, 194, 194);
}

.msg-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: inherit;
  margin-bottom: 4px;
}

.msg-avatar {
  margin-right: 2px;
}

.msg-name {
  font-weight: bold;
}

.msg-date {
  color: #454343;
}


.msg-content {
  font-size: 16px;
  color: inherit;
  padding-left: 2px;
}

.reply-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 12px;
}

.reply-input-area {
  width: 100%;
  display: flex;
  align-items: flex-start;
  gap: 12px;
  margin-top: 12px;
}
.reply-input {
  flex: 1;
}

.create-post-area {
  display: flex;
  flex-direction: column;
  align-items: stretch;
  gap: 12px;
  margin-top: 0;
  margin-bottom: 0;
  position: relative;
}

.create-post-btn {
  align-self: flex-start;
  margin-left: 0;
  margin-bottom: 16px;
  margin-top: 16px;
  background: #5865f2;
  color: #fff;
  border: none;
  font-weight: bold;
  box-shadow: 0 2px 8px rgba(0,0,0,0.12);
  height: 35px;
  border-radius: 8px;
  font-size: 16px;
}

.create-post-card {
  background: #fafafa;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.12);
  padding: 24px 24px 10px 24px;
  display: flex;
  flex-direction: column;
  margin-bottom: 16px;
  min-height: 300px;
}

.create-post-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-name {
  font-weight: bold;
  font-size: 16px;
  color: #000000;
}

.close-btn {
  color: #888 !important;
  background: none !important;
  border: none !important;
  box-shadow: none !important;
  transition: color 0.2s;
}
.close-btn:hover {
  color: #f56c6c !important;
  background: #f5f5f5 !important;
}

.create-post-divider {
  margin: 2px 0;
  background: #444;
}

.create-post-title-input,
.create-post-desc-input {
  width: 100%;
  border: none;
  background: #fafafa;
}

.create-post-desc-input{
  font-size: 14px;
}

.create-post-footer {
  display: flex;
  flex: 1;
  justify-content: flex-end;
  width: 100%;
}

.send-btn {
  align-self: flex-end;
  height: 30px;
  width: 80px;
  background: #5865f2;
  color: #fff;
  border: none;
  font-weight: bold;
  border-radius: 8px;
  font-size: 16px;
}

/* 针对 el-input 的 input 框 */
.create-post-title-input :deep(.el-input__wrapper),
.create-post-desc-input :deep(.el-input__wrapper) {
  box-shadow: none !important;
  border: none !important;
  background: transparent !important;
  padding: 0 !important;
}

/* 针对 textarea */
.create-post-desc-input :deep(.el-textarea__inner) {
  box-shadow: none !important;
  border: none !important;
  background: transparent !important;
  resize: none;
  padding: 0 !important;
  color: #222;
}

/* 针对 input */
.create-post-title-input :deep(.el-input__inner) {
  box-shadow: none !important;
  border: none !important;
  background: transparent !important;
  color: #222;
  padding: 0 !important;
}
</style>
