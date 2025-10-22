<template>
  <div
    :class="
      'flex flex-' +
      (isTablet ? 'column' : 'row') +
      ' justify-content-between align-items-' +
      (isTablet ? 'start' : 'center') +
      ''
    "
  >
    <div :class="'project-info w-' + (isTablet ? '100' : '50') + ' me-5'">
      <div v-if="isTablet" class="w-100">
        <div class="project-image w-100" v-if="project.imageUrl">
          <el-image :src="project.imageUrl" fit="scale-down" />
        </div>
        <div v-else class="project-image-placeholder w-100">
          <el-empty description="No image" :image-size="100">
            <template #image>
              <el-icon :size="60" style="color: #909399"><Picture /></el-icon>
            </template>
          </el-empty>
        </div>
      </div>
      <div class="info-item">
        <div
          v-if="isTablet"
          class="flex justify-content-between align-items-center"
        >
          <h3 style="color: #2f4e73">Description</h3>
          <el-tag class="mb-2" :type="getStageType(project.currentStage)">{{
            getProjectStageText(project.currentStage)
          }}</el-tag>
        </div>
        <h3 v-if="!isTablet" style="color: #2f4e73">Description</h3>
        <p>{{ project.description }}</p>
      </div>
      <div class="info-item">
        <h3 style="color: #2f4e73">Community</h3>
        <p>{{ community.name }}</p>
      </div>
      <div class="info-item">
        <h3 style="color: #2f4e73">Country</h3>
        <p>{{ community.country }}</p>
      </div>
      <div class="info-item">
        <h3 style="color: #2f4e73">Category</h3>
        <p>{{ getProjectCategoryText(project.category) }}</p>
      </div>
      <div class="info-item">
        <h3 style="color: #2f4e73">Target Date</h3>
        <p>{{ project.targetDate }}</p>
      </div>
      <el-progress
        :stroke-width="22"
        :percentage="getProgressPercentage(project.currentStage)"
        :text-inside="true"
        :status="getStageType(project.currentStage)"
        :format="(percentage) => percentage + '%'"
        :class="[
          'mt-3',
          getProgressPercentage(project.currentStage) === 100
            ? 'completed-progress'
            : '',
        ]"
      />
    </div>

    <div v-if="!isTablet" class="flex flex-column align-items-end w-50">
      <el-tag class="mb-3" :type="getStageType(project.currentStage)">{{
        getProjectStageText(project.currentStage)
      }}</el-tag>
      <div class="project-image w-100" v-if="project.imageUrl">
        <el-image :src="project.imageUrl" fit="scale-down" />
      </div>
      <div v-else class="project-image-placeholder w-100">
        <el-empty description="No image" :image-size="100">
          <template #image>
            <el-icon :size="60" style="color: #909399"><Picture /></el-icon>
          </template>
        </el-empty>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { useRoute } from 'vue-router';
import { Picture } from '@element-plus/icons-vue';
import { getProjectCategoryText } from '@/utils/projectCategoryHelper';
import { getProjectStageText, getStageType } from '@/utils/projectStageHelper';
import { getProgressPercentage } from '@/utils/getProgressPercentage';

const route = useRoute();
const project = ref({});
const community = ref({});
const isTablet = ref(window.innerWidth <= 768);

const updateScreen = () => {
  isTablet.value = window.innerWidth <= 768;
};

onUnmounted(() => {
  window.removeEventListener('resize', updateScreen);
});

onMounted(() => {
  fetchProjectDetail();
  window.addEventListener('resize', updateScreen);
});

// Fetch project details
const fetchProjectDetail = async () => {
  try {
    const projectId = route.params.id;
    const cachedProjectInfo = localStorage.getItem(`project_${projectId}_info`);
    if (cachedProjectInfo) {
      project.value = JSON.parse(cachedProjectInfo);
    }

    const cachedCommunityInfo = localStorage.getItem(
      `project_${projectId}_community`
    );
    if (cachedCommunityInfo) {
      community.value = JSON.parse(cachedCommunityInfo);
    }
  } catch (error) {
    console.error('Failed to fetch project details:', error);
  }
};
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
  height: 200px;
  margin-bottom: 20px;
  border-radius: 4px;
  overflow: hidden;
  position: relative;
}

.project-image .el-image {
  height: 100%;
  object-fit: cover;
  position: absolute;
  top: 0;
  left: 0;
}

.project-image-placeholder {
  height: 200px;
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
