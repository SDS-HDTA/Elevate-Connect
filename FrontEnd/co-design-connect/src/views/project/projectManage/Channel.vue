<template>
  <div class="channel-container">
    <h2>Channel</h2>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

onMounted(() => {
  // 初始化逻辑
})
</script>

<style scoped>
.channel-container {
  padding: 20px;
}
</style> 