<template>
  <div class="login-page">
    <Header :show-logo="false" class="header" />
    <div class="main-content">
      <div class="landing-container">
        <img alt="Elevate Connect Logo" src="/logo.png" class="logo mb-2" />
        <h1 class="title">Elevate Connect</h1>
        <h2 class="subtitle mb-3">
          Bridging the Gap in Humanitarian Initiatives
        </h2>
        <div class="button-container">
          <el-button @click="router.push('/login')" class="btn-primary"
            >Log in</el-button
          >
          <el-button
            @click="router.push('/register')"
            class="btn-secondary ms-0"
            >I have a code</el-button
          >
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import Header from '@/components/Header.vue';

const router = useRouter();
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
}

.main-content {
  margin-top: 60px;
  overflow-y: auto;
}

.landing-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: var(--color-background-light);
  min-height: calc(100vh - 60px);
}

.logo {
  width: 120px;
  height: 120px;
}

.button-container {
  display: flex;
  gap: 1rem;
}

.title {
  font-size: 2rem;
  color: var(--color-dark);
  text-align: center;
}

.subtitle {
  color: var(--color-dark);
  text-align: center;

  @media screen and (max-width: 600px) {
    padding-left: 1rem;
    padding-right: 1rem;
  }
}

@media screen and (max-width: 480px) {
  .button-container {
    flex-direction: column;
    align-items: center;
  }
  .button-container .el-button:first-child {
    width: auto; /* default */
    min-width: 100%; /* match the "I have a code" width */
  }
}
</style>
