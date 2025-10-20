<template>
  <div class="project-list-vertical">
    <div class="project-list">
      <div class="header-container">
        <h2>My Projects</h2>
      </div>

      <div class="search-container">
        <div class="search-bar">
          <el-input
            v-model="searchQuery"
            placeholder="Search projects..."
            @keyup.enter="handleSearch"
            clearable
            @clear="handleClear"
            class="custom-search"
          />
          <el-button class="btn-primary" @click="handleSearch"
            >Search</el-button
          >
        </div>
        <!-- Hiding filter as functionality is currently broken
         TODO: Fix filtering
        <div class="filter-container">
          <span class="filter-label">Filter:</span>
          <el-select
            v-model="searchType"
            placeholder="Search by"
            class="filter-select"
          >
            <el-option label="Name" :value="0" />
            <el-option label="Category" :value="1" />
            <el-option label="Country" :value="2" />
          </el-select>
        </div> -->
      </div>
      <div class="projects-list">
        <el-card
          v-for="project in projects"
          :key="project.id"
          class="project-card"
          @click="$router.push(`/my-projects/${project.id}`)"
        >
          <div class="project-content">
            <div class="project-info">
              <div class="project-header">
                <h2 class="pe-3" style="font-weight: bold">
                  {{ project.name }}
                </h2>
                <el-tag :type="getStageType(project.currentStage)">{{
                  getProjectStageText(project.currentStage)
                }}</el-tag>
              </div>
              <div v-if="!isSmallScreen" class="project-details">
                <p>
                  <strong style="font-weight: bold; color: #2f4e73"
                    >Country:</strong
                  >
                  {{ project.country }}
                </p>
                <p class="mt-1">
                  <strong style="font-weight: bold; color: #2f4e73"
                    >Category:</strong
                  >
                  {{ getProjectCategoryText(project.category) }}
                </p>
                <p class="mt-1">
                  <strong style="font-weight: bold; color: #2f4e73"
                    >Description:</strong
                  >
                  {{ project.description }}
                </p>
                <el-progress
                  :stroke-width="24"
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
            </div>
            <div class="image-container">
              <div class="project-image" v-if="project.projectImageId">
                <el-image :src="project.imageSrc" fit="fill" />
              </div>
              <div v-else class="project-image-placeholder">
                <el-empty description="No image" :image-size="100">
                  <template #image>
                    <el-icon :size="60" style="color: #909399"
                      ><Picture
                    /></el-icon>
                  </template>
                </el-empty>
              </div>
            </div>
            <div v-if="isSmallScreen" class="project-details">
              <p>
                <strong style="font-weight: bold; color: #2f4e73"
                  >Country:</strong
                >
                {{ project.country }}
              </p>
              <p class="mt-1">
                <strong style="font-weight: bold; color: #2f4e73"
                  >Category:</strong
                >
                {{ getProjectCategoryText(project.category) }}
              </p>
              <p class="mt-1">
                <strong style="font-weight: bold; color: #2f4e73"
                  >Description:</strong
                >
                {{ project.description }}
              </p>
              <el-progress
                :stroke-width="24"
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
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { Picture } from '@element-plus/icons-vue';
import request from '@/utils/request';
import { getProgressPercentage } from '@/utils/getProgressPercentage';
import { getStageType, getProjectStageText } from '@/utils/projectStageHelper';
import { getProjectCategoryText } from '@/utils/projectCategoryHelper';

const searchType = ref(0); // 0-Name, 1-Category, 2-Country
const searchQuery = ref('');
const projects = ref([]);
const isTablet = ref(window.innerWidth <= 768);
const isSmallScreen = ref(window.innerWidth <= 600);

const updateScreen = () => {
  isTablet.value = window.innerWidth <= 768;
  isSmallScreen.value = window.innerWidth <= 600;
};

onMounted(() => {
  window.addEventListener('resize', updateScreen);
});

onUnmounted(() => {
  window.removeEventListener('resize', updateScreen);
});

// Get project list
const fetchProjects = async (type = null, value = '') => {
  try {
    const userId = localStorage.getItem('userId');
    const params = { userId };
    if (type !== null && value) {
      params.searchType = type;
      params.searchValue = value;
    }
    const res = await request.get('/projects/my', { params });
    if (res.code === 1) {
      projects.value = res.data.map((projectResponse) => ({
        ...projectResponse.project,
        country: projectResponse.community.country,
        imageSrc: projectResponse.projectImageSrc,
      }));
    }
  } catch (error) {
    console.error('Failed to fetch projects:', error);
  }
};

const handleSearch = () => {
  fetchProjects(searchType.value, searchQuery.value);
};

const handleClear = () => {
  searchQuery.value = '';
  searchType.value = 0;
  fetchProjects();
};

onMounted(() => {
  fetchProjects();
});
</script>

<style scoped>
.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-container h2 {
  margin: 0;
}

.button-group {
  display: flex;
  gap: 10px;
}

.search-container {
  width: 100%;
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
  align-items: start;
  flex-direction: column;

  @media screen and (min-width: 601px) {
    flex-direction: row;
    padding-right: 0;
    justify-content: space-between;
  }
}

.filter-container {
  display: flex;
  width: 100%;
  justify-content: center;
  align-items: center;
  margin-top: 10px;

  @media screen and (min-width: 601px) {
    margin-top: 0;
    justify-content: flex-end;
  }
}

.filter-label {
  margin-right: 5px;
}

.filter-select {
  width: 100%;

  @media screen and (min-width: 601px) {
    width: 140px;
    margin-left: 10px;
  }
}

.search-button {
  @media screen and (min-width: 601px) {
    margin-left: 10px;
  }
}

.custom-search {
  width: 100%;
  margin-right: 10px;

  @media screen and (min-width: 601px) {
    width: 250px;
  }
}

.search-bar {
  width: 100%;
  display: flex;
}

.project-list-vertical {
  padding: 20px;
  overflow-x: hidden;
  margin: 0 auto;
}

.projects-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.project-card {
  transition: transform 0.3s;
}

.project-card:hover {
  transform: translateX(5px);
}

.project-content {
  display: flex;
  gap: 20px;
  flex-direction: column;

  @media screen and (min-width: 601px) {
    flex-direction: row;
  }
}

.project-info {
  flex: 1;
}

.project-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.project-header h2 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

.project-image {
  width: 300px;
  height: 200px;
  overflow: hidden;
  border-radius: 8px;
}

.project-image .el-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.project-image-placeholder {
  width: 300px;
  height: 200px;
  overflow: hidden;
  border-radius: 8px;
  background-color: var(--color-background-light);
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px dashed #dcdfe6;
}

.menu {
  font-size: 26px;
  cursor: pointer;
}

.image-container {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
