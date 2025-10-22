<template>
  <div class="work-piece-container">
    <el-loading v-if="loading" />
    <div
      v-else
      class="status-section"
      v-for="status in folderData"
      :key="status.statusId"
    >
      <h2 class="status-title">{{ getProjectStageText(status.statusId) }}</h2>
      <div class="folders-container">
        <el-card
          v-for="iteration in status.iterations"
          :key="iteration.iterationId"
          class="folder-card"
          shadow="hover"
          @click="handleCardClick(iteration)"
        >
          <div class="folder-content">
            <el-icon class="folder-icon"><Folder /></el-icon>
            <span class="folder-name"
              >Iteration {{ iteration.iterationId }}</span
            >
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { Folder } from '@element-plus/icons-vue';
import request from '@/utils/request';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { getProjectStageText } from '../../../utils/projectStageHelper';

const folderData = ref([]);
const route = useRoute();
const router = useRouter();

// Get iteration list
const getFolders = async () => {
  const projectId = route.params.id;
  const res = await request.get(`/projects/${projectId}/folders`);
  if (res.code === 1) {
    return res.data;
  } else {
    ElMessage.error('Failed to get iteration list' + res.message);
  }
};

// Get all data
const fetchAllData = async () => {
  try {
    // Get all iterations
    const iterationList = await getFolders();

    // Group by statusId
    const groupedByStatus = iterationList.reduce((acc, iteration) => {
      const statusId = iteration.status;
      if (!acc[statusId]) {
        acc[statusId] = [];
      }
      acc[statusId].push({
        iterationId: iteration.iterationId,
        statusId: iteration.status,
      });
      return acc;
    }, {});

    // Convert to required format
    const formattedData = Object.entries(groupedByStatus).map(
      ([statusId, iterations]) => ({
        statusId: parseInt(statusId),
        iterations: iterations,
      })
    );

    folderData.value = formattedData;
  } catch (error) {
    console.error('Failed to fetch data:', error);
    ElMessage.error('Failed to fetch data, please try again later');
  }
};

// Handle card click event
const handleCardClick = (iteration) => {
  router.push(
    `/my-projects/workpiece/${route.params.id}/${iteration.statusId}/${iteration.iterationId}`
  );
};

// Load data when page mounts
onMounted(() => {
  fetchAllData();
  console.log(folderData.value);
});
</script>

<style scoped>
.work-piece-container {
  padding: 20px;
  height: 100%;
  overflow-y: auto;
}

.status-section {
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 8px;
  background-color: var(--color-background-light);
}

.status-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #303133;
  padding-left: 10px;
  border-left: 4px solid #2f4e73;
}

.folders-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  padding-bottom: 10px;
}

.folder-card {
  min-width: 150px;
  max-width: 150px;
  cursor: pointer;
}

.folder-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px;
}

.folder-icon {
  font-size: 40px;
  color: #2f4e73;
  margin-bottom: 10px;
}

.folder-name {
  font-size: 14px;
  color: #606266;
}

/* Custom scrollbar styles */
.folders-container::-webkit-scrollbar {
  height: 6px;
}

.folders-container::-webkit-scrollbar-thumb {
  background-color: #909399;
  border-radius: 3px;
}

.folders-container::-webkit-scrollbar-track {
  background-color: var(--color-background-light);
}
</style>
