<template>
  <div v-if="project" class="project-detail">
    <h2 class="ps-3 pt-3">{{ project.name }}</h2>
    <div class="ps-3 nav-links">
      <router-link :to="`/my-projects/${project.id}/info`" class="nav-link"
        >Info</router-link
      >
      <router-link :to="`/my-projects/${project.id}/posts`" class="nav-link"
        >Posts</router-link
      >
      <router-link
        :to="`/my-projects/${project.id}/activities`"
        class="nav-link"
        >Activities</router-link
      >
      <router-link :to="`/my-projects/${project.id}/resources`" class="nav-link"
        >Resources</router-link
      >
      <router-link
        :to="`/my-projects/${project.id}/participants`"
        class="nav-link"
        >Participants</router-link
      >
      <router-link :to="`/my-projects/${project.id}/map`" class="nav-link"
        >Map</router-link
      >
    </div>

    <!-- Content area -->
    <div class="content-area">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import request from '@/utils/request';

const route = useRoute();
const router = useRouter();
const project = ref({});
const members = ref([]);
const community = ref({});
const creatorId = ref(0);
const country = ref('');
const isCreator = ref(false);

// Fetch project details
const fetchProjectDetail = async () => {
  try {
    const projectId = route.params.id;
    const res = await request.get(`/projects/${projectId}`);
    if (res.code === 1) {
      project.value = res.data['project'];
      community.value = res.data['community'];
      members.value = res.data['members'];
      creatorId.value = res.data['project']['creatorId'];

      localStorage.setItem(`project_${projectId}_creatorId`, creatorId.value);
      localStorage.setItem(
        `project_${projectId}_community`,
        JSON.stringify(community.value)
      );
      localStorage.setItem(
        `project_${projectId}_info`,
        JSON.stringify(project.value)
      );
      localStorage.setItem(
        `project_${projectId}_members`,
        JSON.stringify(members.value)
      );

      checkIsCreator();
    }
  } catch (error) {
    console.error('Failed to fetch project details:', error);
  }
};

// Check if current user is the creator
const checkIsCreator = () => {
  const currentUserId = Number(localStorage.getItem('userId'));
  isCreator.value = currentUserId === project.value.creatorId;
};

// Clear project-related storage
const clearProjectStorage = () => {
  const projectId = route.params.id;
  localStorage.removeItem(`project_${projectId}_creatorId`);
  localStorage.removeItem(`project_${projectId}_info`);
  localStorage.removeItem(`project_${projectId}_members`);
};

// Clear storage when component unmounts
onUnmounted(() => {
  clearProjectStorage();
});

onMounted(() => {
  fetchProjectDetail();
  // If current path only contains project ID, redirect to info page
  if (route.path === `/my-projects/${route.params.id}`) {
    router.push(`/my-projects/${route.params.id}/info`);
  }
});
</script>

<style scoped>
.project-detail {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  background-color: var(--color-background-light);
}

.left-panel {
  flex: 0 0 300px;
  padding: 20px;
  border-right: 1px solid #e4e7ed;
  min-width: 300px;
  max-width: 300px;
  background-color: #fff;
}

.project-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e4e7ed;
}

.project-header h1 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

.project-image {
  width: 100%;
  height: 200px;
  margin-bottom: 20px;
  border-radius: 4px;
  overflow: hidden;
  position: relative;
}

.project-image .el-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  position: absolute;
  top: 0;
  left: 0;
}

.project-image-placeholder {
  width: 100%;
  height: 200px;
  margin-bottom: 20px;
  border-radius: 4px;
  background-color: var(--color-background-light);
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px dashed #dcdfe6;
}

.project-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-item h3 {
  margin: 0 0 4px 0;
  font-size: 14px;
  color: #303133;
  font-weight: bold;
  letter-spacing: 0.5px;
}

.info-item p {
  margin: 0;
  color: #333;
  line-height: 1.5;
}

.content-area {
  flex: 1;
  padding: 20px;
  height: 100%;
  overflow: hidden;
  background-color: var(--color-background-light);
}

.top-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.back-button:hover {
  color: #2f4e73;
}

.back-button .el-icon {
  font-size: 16px;
}

.action-buttons {
  padding: 10px;
  display: flex;
  gap: 5px;
  justify-content: flex-end;
  width: 100%;
}

.action-buttons .custom-button {
  flex: 1;
  max-width: 120px;
  font-size: 15px;
  font-weight: 600;
  border-radius: 8px;
  padding: 10px 20px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.action-buttons .custom-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.action-buttons .custom-button:active {
  transform: translateY(0);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>
