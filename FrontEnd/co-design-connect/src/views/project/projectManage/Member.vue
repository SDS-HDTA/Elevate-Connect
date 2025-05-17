<template>
  <div class="member-container">
    <h2>Member</h2>
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
.member-container {
  padding: 20px;
}
</style> 