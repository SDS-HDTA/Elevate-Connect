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
                  <Avatar :firstName="msg.senderName[0]" :lastName="msg.senderName[1] || ''" :size="28" />
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
            placeholder="请输入回复内容"
            size="large"
            class="reply-input"
            @keyup.enter="submitReply(post)"
            :autosize="{ minRows: 2, maxRows: 4 }"
            type="textarea"
          />
          <el-button size="large" type="success" @click="submitReply(post)">发送</el-button>
          <el-button size="large" @click="cancelReply">取消</el-button>
        </div>
      </div>
    </div>
    <el-button
      class="create-post-btn"
      type="primary"
      size="large"
      @click="onCreatePost"
    >
      <i class="el-icon-edit" style="margin-right:6px;"></i>
      Start a post
    </el-button>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { ElDivider, ElButton, ElInput, ElMessage } from 'element-plus'
import Avatar from '@/components/Avatar.vue'
import request from '@/utils/request'
import { useRoute } from 'vue-router'

const route = useRoute()
const postsScrollArea = ref(null)

// 示例数据
const posts = ref([
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
      },
      {
        id: 102,
        content: '补充一下，实际利率 = 名义利率 - 通货膨胀率',
        senderName: '王五',
        createTime: '2024-06-01T10:15:00'
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
      },
      {
        id: 203,
        content: '建议根据资产使用情况选择，如果资产前期使用强度大，用加速折旧更合理。',
        senderName: '孙八',
        createTime: '2024-06-01T11:30:00'
      }
    ]
  },
  {
    id: 3,
    title: '项目风险评估方法',
    description: '大家平时都用什么方法做项目风险评估？',
    creatorName: '李四',
    createTime: '2024-06-02T09:00:00',
    messages: [
      {
        id: 301,
        content: '我们公司主要用德尔菲法，邀请专家打分。',
        senderName: '周九',
        createTime: '2024-06-02T09:10:00'
      },
      {
        id: 302,
        content: '我们用的是蒙特卡洛模拟，可以量化风险。',
        senderName: '吴十',
        createTime: '2024-06-02T09:20:00'
      }
    ]
  },
  {
    id: 4,
    title: '投资回收期计算',
    description: '动态投资回收期和静态投资回收期哪个更准确？',
    creatorName: '赵六',
    createTime: '2024-06-02T14:00:00',
    messages: [
      {
        id: 401,
        content: '动态回收期考虑了时间价值，更准确。',
        senderName: '郑十一',
        createTime: '2024-06-02T14:05:00'
      },
      {
        id: 402,
        content: '但静态回收期计算简单，适合快速评估。',
        senderName: '王十二',
        createTime: '2024-06-02T14:10:00'
      },
      {
        id: 403,
        content: '建议两个都算，互相验证。',
        senderName: '李十三',
        createTime: '2024-06-02T14:15:00'
      }
    ]
  },
  {
    id: 5,
    title: '敏感性分析问题',
    description: '做敏感性分析时，关键变量如何选择？',
    creatorName: '钱七',
    createTime: '2024-06-03T10:00:00',
    messages: [
      {
        id: 501,
        content: '通常选择对项目影响最大的变量，如价格、成本等。',
        senderName: '孙十四',
        createTime: '2024-06-03T10:10:00'
      },
      {
        id: 502,
        content: '还要考虑变量的不确定性，波动大的变量要重点分析。',
        senderName: '周十五',
        createTime: '2024-06-03T10:20:00'
      },
      {
        id: 503,
        content: '建议用蛛网图展示多变量敏感性分析结果。',
        senderName: '吴十六',
        createTime: '2024-06-03T10:30:00'
      }
    ]
  }
])

const replyingPostId = ref(null)
const replyContent = ref('')

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

async function submitReply(post) {
  const content = replyContent.value.trim()
  if (!content) {
    ElMessage.warning('回复内容不能为空')
    return
  }
  try {
    // 假设后端返回新消息对象
    const res = await request.post('/api/project/channel/reply', {
      postId: post.id,
      content
    })
    // 将后端返回的新消息添加到本地
    post.messages.push(res)
    replyContent.value = ''
    replyingPostId.value = null
    nextTick(() => {
      if (postsScrollArea.value) {
        postsScrollArea.value.scrollTop = postsScrollArea.value.scrollHeight
      }
    })
  } catch (e) {
    ElMessage.error('回复失败，请重试')
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

onMounted(async () => {
  const projectId = route.params.projectId
  try {
    const res = await request.get(`/projects/${projectId}/channel`)
    if (res.code === 1) {
      posts.value = res.data.map(post => ({
        ...post,
        messages: post.messages.sort((a, b) => new Date(a.createTime) - new Date(b.createTime))
      })).sort((a, b) => b.id - a.id)
    }
  } catch (error) {
    ElMessage.error('获取频道信息失败')
    console.error('获取频道信息失败:', error)
  }

  // 等待DOM更新后滚动到底部
  await nextTick()
  const container = postsScrollArea.value
  if (container) {
    container.scrollTop = container.scrollHeight
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
  display: flex;
  align-items: center;
  height: 48px;
  min-width: 120px;
  border-radius: 8px;
  font-size: 16px;
}
</style>
