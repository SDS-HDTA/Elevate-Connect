<template>
  <div class="backlog-container">
    <h2>Backlog</h2>
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
.backlog-container {
  padding: 20px;
}
</style> 