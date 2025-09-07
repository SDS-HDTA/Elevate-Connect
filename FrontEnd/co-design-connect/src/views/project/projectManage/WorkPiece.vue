<template>
  <div class="work-piece-container">
    <el-loading v-if="loading" />
    <div
      v-else
      class="status-section"
      v-for="status in folderData"
      :key="status.statusId"
    >
      <h2 class="status-title">Status: {{ getStatusName(status.statusId) }}</h2>
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

const folderData = ref([]);
const route = useRoute();
const router = useRouter();

// Status mapping
const statusMap = {
  0: 'Empathise',
  1: 'Discover',
  2: 'Define',
  3: 'Ideate',
  4: 'Prototype',
  5: 'Feedback',
};

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

    console.log(formattedData);
    folderData.value = formattedData;
    console.log(folderData.value);
  } catch (error) {
    console.error('Failed to fetch data:', error);
    ElMessage.error('Failed to fetch data, please try again later');
    folderData.value = mockData.value;
  }
};

// Get status name
const getStatusName = (statusId) => {
  return statusMap[statusId] || `Status ${statusId}`;
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

// Mock data (can be deleted)
const mockData = ref([
  {
    statusId: 1,
    iterations: [
      {
        id: 1,
        iterationId: 1,
        statusId: 1,
      },
      {
        id: 2,
        iterationId: 2,
        statusId: 1,
      },
      {
        id: 3,
        iterationId: 3,
        statusId: 1,
      },
      {
        id: 4,
        iterationId: 4,
        statusId: 1,
      },
      {
        id: 5,
        iterationId: 5,
        statusId: 1,
      },
      {
        id: 6,
        iterationId: 6,
        statusId: 1,
      },
      {
        id: 7,
        iterationId: 7,
        statusId: 1,
      },
      {
        id: 8,
        iterationId: 8,
        statusId: 1,
      },
      {
        id: 9,
        iterationId: 9,
        statusId: 1,
      },
      {
        id: 10,
        iterationId: 10,
        statusId: 1,
      },
    ],
  },
  {
    statusId: 2,
    iterations: [
      {
        id: 11,
        iterationId: 4,
        statusId: 2,
      },
      {
        id: 12,
        iterationId: 5,
        statusId: 2,
      },
    ],
  },
  {
    statusId: 3,
    iterations: [
      {
        id: 13,
        iterationId: 6,
        statusId: 3,
      },
      {
        id: 14,
        iterationId: 7,
        statusId: 3,
      },
      {
        id: 15,
        iterationId: 8,
        statusId: 3,
      },
    ],
  },
  {
    statusId: 4,
    iterations: [
      {
        id: 16,
        iterationId: 9,
        statusId: 4,
      },
      {
        id: 17,
        iterationId: 10,
        statusId: 4,
      },
      {
        id: 18,
        iterationId: 11,
        statusId: 4,
      },
    ],
  },
  {
    statusId: 5,
    iterations: [
      {
        id: 19,
        iterationId: 12,
        statusId: 5,
      },
      {
        id: 20,
        iterationId: 13,
        statusId: 5,
      },
    ],
  },
  {
    statusId: 6,
    iterations: [
      {
        id: 21,
        iterationId: 14,
        statusId: 6,
      },
      {
        id: 22,
        iterationId: 15,
        statusId: 6,
      },
      {
        id: 23,
        iterationId: 16,
        statusId: 6,
      },
    ],
  },
]);
</script>

<style scoped>
.work-piece-container {
  padding: 20px;
  height: 100%;
  overflow-y: auto;
}

.status-section {
  margin-bottom: 30px;
  padding: 20px;
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
