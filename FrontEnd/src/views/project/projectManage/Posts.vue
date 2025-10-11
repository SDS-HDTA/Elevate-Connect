<template>
  <div class="channel-container">
    <div class="posts-scroll-area" ref="postsScrollArea">
      <div v-for="post in posts" :key="post.id" class="post-block">
        <!-- Topic initiator and date -->
        <div class="post-header">
          <span> <Avatar :full-name="post.creatorName" :size="28" /> </span>
          <span class="post-creator">{{ post.creatorName }}</span>
          <span class="post-date">{{ formatMsgDate(post.createTime) }}</span>
        </div>
        <!-- Topic title -->
        <h3 class="post-title">{{ post.title }}</h3>
        <!-- Topic description -->
        <div class="post-desc">{{ post.content }}</div>
        <!-- Reply section -->
        <el-divider class="post-divider" />
        <div class="replies">
          <template v-for="(msg, idx) in post.replies" :key="msg.id">
            <div class="reply-item">
              <div class="msg-header">
                <div class="msg-avatar">
                  <Avatar :full-name="msg.senderName" :size="28" />
                </div>
                <span class="msg-name">{{ msg.senderName }}</span>
                <span class="msg-date">{{
                  formatMsgDate(msg.createTime)
                }}</span>
              </div>
              <div class="msg-content">{{ msg.content }}</div>
            </div>
          </template>
        </div>
        <!-- Reply button and input box -->
        <div
          v-if="replyingPostId !== post.id"
          v-require-permission="permissions.CreateReply"
          class="reply-row"
        >
          <el-button class="btn-icon-primary" @click="showReplyInput(post.id)"
            ><el-icon><CirclePlusFilled /></el-icon
            ><span class="ms-1">Reply</span></el-button
          >
        </div>
        <div v-if="replyingPostId === post.id" class="reply-input-area">
          <el-input
            v-model="replyContent"
            placeholder="Type your reply here..."
            size="large"
            class="reply-input"
            @keyup.enter="submitReply(post)"
            :autosize="{ minRows: 1, maxRows: 3 }"
            type="textarea"
          />
          <el-button class="btn-icon-primary" @click="submitReply(post)">
            <el-icon><Promotion /></el-icon>
          </el-button>
          <el-tooltip content="Cancel reply" placement="top">
            <el-button class="btn-icon-danger" @click="cancelReply">
              <el-icon><CircleCloseFilled /></el-icon>
            </el-button>
          </el-tooltip>
        </div>
      </div>
    </div>

    <!-- Post input box/button area -->
    <div class="create-post-area">
      <template v-if="creatingPost">
        <div class="create-post-card">
          <div class="create-post-header">
            <div class="user-info">
              <Avatar :full-name="fullName" :size="32" />
              <span class="user-name">{{ fullName }}</span>
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
            placeholder="Type your reply here..."
            size="large"
            class="create-post-desc-input"
            :autosize="{ minRows: 4, maxRows: 8 }"
            type="textarea"
            maxlength="200"
          />
          <div class="create-post-footer">
            <el-button size="large" class="send-btn" @click="submitNewPost"
              >Post</el-button
            >
          </div>
        </div>
      </template>
      <el-button
        v-require-permission="permissions.CreatePost"
        v-else
        class="btn-primary create-post-btn"
        size="large"
        @click="onCreatePost"
      >
        <i class="el-icon-edit" style="margin-right: 6px"></i>
        <el-icon><Plus /></el-icon><span class="ms-1">Start a post</span>
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue';
import { ElDivider, ElButton, ElInput, ElMessage } from 'element-plus';
import Avatar from '@/components/Avatar.vue';
import { useRoute } from 'vue-router';
import {
  CircleCloseFilled,
  CirclePlusFilled,
  Close,
  Message,
  Plus,
  Promotion,
} from '@element-plus/icons-vue';
import { getWebSocketURL } from '@/utils/constants';
import { MessageType } from '@/utils/constants';
import {
  apiGetPosts,
  apiNewPost,
  apiNewReply,
} from '@/utils/api/post-api-utils';
import { permissions } from '@/models/permission';

const route = useRoute();
const postsScrollArea = ref(null);
const ws = ref(null);
const replyingPostId = ref(null);
const replyContent = ref('');
const creatingPost = ref(false);
const newPostTitle = ref('');
const newPostDescription = ref('');
const fullName = ref(localStorage.getItem('fullName'));
const posts = ref([]);

const projectId = route.params.id;

// WebSocket connection management
function initWebSocket() {
  const wsUrl = getWebSocketURL(projectId);
  ws.value = new WebSocket(wsUrl);

  ws.value.onopen = () => {
    console.log('WebSocket Connection established');
  };

  ws.value.onmessage = (event) => {
    const data = JSON.parse(event.data);
    const message = JSON.parse(data.message);

    console.log(`New WebSocket message: ${message}`);

    handleWebSocketMessage(message);
  };

  ws.value.onerror = (error) => {
    console.error('WebSocket error:', error);
    ElMessage.error(`Communication error.`);
  };

  ws.value.onclose = () => {
    console.log('WebSocket connection closed');
  };
}

// Handle received WebSocket messages
function handleWebSocketMessage(message) {
  switch (message.type) {
    case MessageType.NEW_POST:
      const newPost =
        typeof message.data === 'string'
          ? JSON.parse(message.data)
          : message.data;

      posts.value.push(newPost);

      nextTick(() => {
        if (postsScrollArea.value) {
          postsScrollArea.value.scrollTop = postsScrollArea.value.scrollHeight;
        }
      });

      break;
    case MessageType.NEW_REPLY:
      const post = posts.value.find(
        (post) => post.id === JSON.parse(message.data).postId
      );

      if (post) {
        post.replies.push(JSON.parse(message.data));
      }

      break;

    // ! I think this case is useless
    case MessageType.DELETE_POST:
      posts.value = posts.value.filter((post) => post.id !== data.postId);
      break;
    // ! Same here, as this only seems to be removing them from the frontend
    case MessageType.DELETE_REPLY:
      const targetPost = posts.value.find((p) => p.id === data.postId);

      if (targetPost) {
        targetPost.replies = targetPost.replies.filter(
          (m) => m.id !== data.replyId
        );
      }

      break;
  }
}

function sendWebSocketMessage(type, data) {
  if (ws.value && ws.value.readyState === WebSocket.OPEN) {
    ws.value.send(
      JSON.stringify({
        toName: null,
        message: JSON.stringify({ type, data }),
      })
    );
  } else {
    ElMessage.warning(
      'Real-time communication not connected, please refresh the page and try again'
    );
  }
}

function showReplyInput(postId) {
  replyingPostId.value = postId;
  replyContent.value = '';

  nextTick(() => {
    // Auto focus input box
    const input = document.querySelector('.reply-input input');

    if (input) {
      input.focus();
    }
  });
}

function cancelReply() {
  replyingPostId.value = null;
  replyContent.value = '';
}

// Format time to YYYY-MM-DD HH:mm:ss format
function formatDateTime(date) {
  const d = new Date(date);
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}:${String(d.getSeconds()).padStart(2, '0')}`;
}

async function submitReply(post) {
  const content = replyContent.value.trim();

  if (!content) {
    ElMessage.warning('Reply content cannot be empty');
    return;
  }

  try {
    const formData = new URLSearchParams();

    //Data for reply object
    formData.append('postId', post.id);
    formData.append('authorId', localStorage.getItem('userId'));
    formData.append('content', content);
    formData.append('createTime', formatDateTime(new Date()));

    const res = await apiNewReply(projectId, formData);

    if (res.code === 1) {
      // Send new message via WebSocket, message is type+res.data
      sendWebSocketMessage(MessageType.NEW_REPLY, JSON.stringify(res.data));
    } else {
      ElMessage.error('Failed to send reply to server.');
    }

    replyContent.value = '';
    replyingPostId.value = null;
  } catch (e) {
    console.log(e);
    ElMessage.error('Reply failed, please try again.');
  }
}

// Format message date: only display month-day hour:minute
function formatMsgDate(dateStr) {
  const d = new Date(dateStr);
  return `${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`;
}

function onCreatePost() {
  creatingPost.value = true;
  newPostTitle.value = '';
  newPostDescription.value = '';

  nextTick(() => {
    const input = document.querySelector('.create-post-title-input input');
    if (input) input.focus();
  });
}

function cancelCreatePost() {
  creatingPost.value = false;
  newPostTitle.value = '';
  newPostDescription.value = '';
}

async function submitNewPost() {
  const title = newPostTitle.value.trim();
  const content = newPostDescription.value.trim();

  // Data validation
  if (!title) {
    ElMessage.warning('Title cannot be empty');
    return;
  }
  if (!content) {
    ElMessage.warning('Description cannot be empty');
    return;
  }

  try {
    const formData = new URLSearchParams();

    formData.append('projectId', projectId);
    formData.append('authorId', localStorage.getItem('userId'));
    formData.append('title', title);
    formData.append('content', content);
    formData.append('createTime', formatDateTime(new Date()));

    const res = await apiNewPost(projectId, formData);

    if (res.code === 1) {
      // Send new post via WebSocket
      sendWebSocketMessage(MessageType.NEW_POST, JSON.stringify(res.data));
    } else {
      ElMessage.error('Failed to send post to server.');
    }

    cancelCreatePost();
  } catch (e) {
    ElMessage.error('Post failed, please try again.');
  }
}

onMounted(async () => {
  try {
    const postData = await apiGetPosts(projectId);

    if (postData) {
      // Map response data to posts and their replies, and sort posts and replies
      posts.value = postData
        .map((post) => ({
          ...post,
          replies: post.replies.sort(
            (a, b) => new Date(a.createTime) - new Date(b.createTime)
          ),
        }))
        .sort((a, b) => a.id - b.id);
    }

    // Initialize WebSocket connection
    initWebSocket();
  } catch (error) {
    console.error('Failed to fetch posts from API:', error);
    ElMessage.error('Failed to fetch posts');
  }

  await nextTick();

  const container = postsScrollArea.value;
  if (container) {
    container.scrollTop = container.scrollHeight;
  }
});

// Close WebSocket connection when component unmounts
onUnmounted(() => {
  if (ws.value) {
    ws.value.close();
  }
});
</script>

<style scoped>
.channel-container {
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
  gap: 5px;
  font-size: 16px;
  color: #666;
  margin-bottom: 8px;
}

.post-creator {
  font-weight: bold;
  margin-right: 10px;
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

.replies {
  margin-top: 8px;
}

.reply-item {
  display: flex;
  flex-direction: column;
  position: relative;
  border-radius: 8px;
  margin-bottom: 16px;
  transition:
    background 0.2s,
    color 0.2s;
  padding-left: 16px;
  border-left: 6px solid var(--msg-color, #409eff);
}
.reply-item:hover {
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
  align-items: center;
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
  align-self: flex-end;
  margin-left: 0;
  margin-bottom: 16px;
  margin-top: 16px;
  border: none;
  font-weight: bold;
}

.create-post-card {
  background: #fafafa;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.12);
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

.create-post-desc-input {
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

/* For el-input input box */
.create-post-title-input :deep(.el-input__wrapper),
.create-post-desc-input :deep(.el-input__wrapper) {
  box-shadow: none !important;
  border: none !important;
  background: transparent !important;
  padding: 0 !important;
}

/* For textarea */
.create-post-desc-input :deep(.el-textarea__inner) {
  box-shadow: none !important;
  border: none !important;
  background: transparent !important;
  resize: none;
  padding: 0 !important;
  color: #222;
}

/* For input */
.create-post-title-input :deep(.el-input__inner) {
  box-shadow: none !important;
  border: none !important;
  background: transparent !important;
  color: #222;
  padding: 0 !important;
}
</style>
