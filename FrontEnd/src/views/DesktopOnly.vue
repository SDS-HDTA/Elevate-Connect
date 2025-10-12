<template>
  <div class="not-found-container">
    <el-result
      icon="warning"
      sub-title="Users, Communities and Projects can only be viewed on desktop"
    >
      <template #extra>
        <el-button class="btn-primary" @click="router.push('/my-projects')"
          >This feature is available on desktop only</el-button
        >
      </template>
    </el-result>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();
</script>

<style scoped>
.not-found-container {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--color-background-light);
  padding: 20px;
}
</style>
