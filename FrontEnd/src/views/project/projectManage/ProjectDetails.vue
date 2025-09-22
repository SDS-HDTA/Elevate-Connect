<template>
  <div class="project-detail">
    <!-- Left project information -->
    <div v-if="project" class="left-panel">
      <div class="top-container">
        <div class="back-button" @click="$router.push('/my-projects')">
          <el-icon><ArrowLeft /></el-icon>
          <span>Back</span>
        </div>
        <div class="action-buttons">
          <el-tooltip content="Leave project" placement="top">
            <el-button
              @click="handleLeaveProject"
              :loading="loading.leave"
              class="btn-icon-danger"
            >
              <el-icon><Remove /></el-icon>
            </el-button>
          </el-tooltip>

          <el-tooltip content="Delete project" placement="top">
            <el-button
              v-if="isCreator"
              @click="handleDismissProject"
              :loading="loading.dismiss"
              class="btn-icon-danger"
            >
              <el-icon><Delete /></el-icon>
            </el-button>
          </el-tooltip>
        </div>
      </div>
      <div class="project-header">
        <h1>{{ project.name }}</h1>
        <!-- <el-tag :type="getStatusType(project.status)" size="large">
          {{ getStatusText(project.status) }}
        </el-tag> -->
      </div>

      <div class="project-image" v-if="project.imageUrl">
        <el-image :src="project.imageUrl" fit="scale-down" />
      </div>
      <div v-else class="project-image-placeholder">
        <el-empty description="No image" :image-size="100">
          <template #image>
            <el-icon :size="60" style="color: #909399"><Picture /></el-icon>
          </template>
        </el-empty>
      </div>

      <div class="project-info">
        <div class="info-item">
          <h3 style="color: #2f4e73">Project Owner</h3>
          <p>{{ creatorName }}</p>
        </div>
        <div class="info-item">
          <h3 style="color: #2f4e73">Target Date</h3>
          <p>{{ project.deadline }}</p>
        </div>
        <div class="info-item">
          <h3 style="color: #2f4e73">Area</h3>
          <p>{{ project.area }}</p>
        </div>
        <div class="info-item">
          <h3 style="color: #2f4e73">Category</h3>
          <p>{{ project.category }}</p>
        </div>
        <div class="info-item">
          <h3 style="color: #2f4e73">Description</h3>
          <p>{{ project.description }}</p>
        </div>
      </div>
    </div>

    <!-- Right function area -->
    <div class="right-panel">
      <div class="nav-links">
        <router-link :to="`/my-projects/${project.id}/channel`" class="nav-link"
          >Channel</router-link
        >
        <router-link :to="`/my-projects/${project.id}/backlog`" class="nav-link"
          >Backlog</router-link
        >
        <router-link
          :to="`/my-projects/${project.id}/workpiece`"
          class="nav-link"
          >WorkPiece</router-link
        >
        <router-link :to="`/my-projects/${project.id}/member`" class="nav-link"
          >Member</router-link
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
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ArrowLeft, Picture, Delete, Remove } from '@element-plus/icons-vue';
import request from '@/utils/request';
import { ElMessageBox, ElMessage } from 'element-plus';

const route = useRoute();
const router = useRouter();
const project = ref({});
const creatorName = ref('');
const members = ref([]);
const creatorId = ref(0);
const isCreator = ref(false);
const isTablet = ref(window.innerWidth <= 768);
const loading = ref({
  leave: false,
  dismiss: false,
});

// Fetch project details
const fetchProjectDetail = async () => {
  try {
    const projectId = route.params.id;
    const res = await request.get(`/projects/${projectId}`);
    if (res.code === 1) {
      project.value = res.data['project'];
      creatorName.value = res.data['creatorName'];
      members.value = res.data['members'];
      creatorId.value = res.data['project']['creatorId'];

      // Store project-related information to localStorage
      localStorage.setItem(`project_${projectId}_creatorId`, creatorId.value);
      localStorage.setItem(
        `project_${projectId}_info`,
        JSON.stringify(project.value)
      );
      localStorage.setItem(
        `project_${projectId}_members`,
        JSON.stringify(members.value)
      );

      // Get project details and immediately determine user identity
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

// Leave project
const handleLeaveProject = async () => {
  try {
    await ElMessageBox.confirm(
      'Are you sure you want to leave this project?',
      'Confirm',
      {
        confirmButtonClass: 'btn-danger',
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
      }
    );

    loading.value.leave = true;
    const userId = localStorage.getItem('userId');
    const res = await request.post(
      '/projects/leave',
      {
        projectId: route.params.id,
        userId: userId,
      },
      {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      }
    );
    if (res.code === 1) {
      ElMessage.success('Successfully left the project');
      router.push('/my-projects');
    } else {
      ElMessage.error(res.message || 'Failed to leave project');
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to leave project:', error);
      ElMessage.error('Failed to leave project');
    }
  } finally {
    loading.value.leave = false;
  }
};

// Dismiss project
const handleDismissProject = async () => {
  try {
    await ElMessageBox.confirm(
      'Are you sure you want to delete this project? This action cannot be undone.',
      'Confirm',
      {
        confirmButtonClass: 'btn-danger',
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
      }
    );

    loading.value.dismiss = true;
    const res = await request.delete(`/projects/${route.params.id}/dismiss`);
    if (res.code === 1) {
      ElMessage.success('Project has been successfully dismissed');
      router.push('/my-projects');
    } else {
      ElMessage.error(res.message || 'Failed to dismiss project');
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to dismiss project:', error);
      ElMessage.error('Failed to dismiss project');
    }
  } finally {
    loading.value.dismiss = false;
  }
};

// Clear project-related storage
const clearProjectStorage = () => {
  const projectId = route.params.id;
  localStorage.removeItem(`project_${projectId}_creatorId`);
  localStorage.removeItem(`project_${projectId}_info`);
  localStorage.removeItem(`project_${projectId}_members`);
};

const updateScreen = () => {
  isTablet.value = window.innerWidth <= 768;
};

// Clear storage when component unmounts
onUnmounted(() => {
  window.removeEventListener('resize', updateScreen);
  clearProjectStorage();
});

onMounted(() => {
  window.addEventListener('resize', updateScreen);
  fetchProjectDetail();
  // If current path only contains project ID, redirect to channel page
  if (route.path === `/my-projects/${route.params.id}`) {
    router.push(`/my-projects/${route.params.id}/channel`);
  }
});
</script>

<style scoped>
.project-detail {
  display: flex;
  width: 100%;
  height: 100%;
  background-color: #fff;
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

.right-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
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
