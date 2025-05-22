<template>
  <div class="files-container">
    <h2>Files</h2>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const socket = new WebSocket("ws://localhost:8080/projects/37/channel");

socket.onopen = () => {
  console.log("WebSocket 连接已建立");
}

socket.onmessage = (event) => {
  console.log("收到消息:", event.data);
}

socket.onerror = (event) => {
  console.log("WebSocket 错误:", event);
}

socket.onclose = () => {
  console.log("WebSocket 连接已关闭");
}

onMounted(() => {
  // 初始化逻辑
})
</script>

<style scoped>
.files-container {
  padding: 20px;
}
</style> 