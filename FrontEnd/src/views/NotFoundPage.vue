<template>
  <div class="not-found-container">
    <el-result
      icon="error"
      title="Page Not Found"
      sub-title="The page you are looking for does not exist"
    >
      <template #extra>
        <el-button class="btn-primary" @click="router.push('/my-projects')"
          >Go to home</el-button
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
