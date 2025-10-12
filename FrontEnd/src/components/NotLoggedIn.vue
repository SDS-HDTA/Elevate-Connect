<template>
  <div class="not-logged-in-container">
    <el-result
      icon="warning"
      title="Please Login First"
      sub-title="You need to login to access this page"
    >
      <template #extra>
        <el-button class="btn-primary" @click="goToLogin"
          >Go to Login</el-button
        >
      </template>
    </el-result>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const goToLogin = () => {
  router.push('/login');
};
</script>

<style scoped>
.not-logged-in-container {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--color-background-light);
  padding: 20px;
}
</style>
