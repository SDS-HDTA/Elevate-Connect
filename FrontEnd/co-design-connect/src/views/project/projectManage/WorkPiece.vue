<template>
  <div class="workpiece-container">
    <h2>WorkPiece</h2>
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
.workpiece-container {
  padding: 20px;
}
</style> 