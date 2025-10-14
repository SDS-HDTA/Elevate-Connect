<template>
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
        <el-button class="btn-primary" @click="handleSearch">Search</el-button>
      </div>
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
      </div>
    </div>

    <div class="projects-grid">
      <el-card
        v-for="project in projects"
        :key="project.id"
        class="project-card"
        @click="$router.push(`/my-projects/${project.id}`)"
      >
        <div class="project-header">
          <h2 class="pe-3" style="font-weight: bold">
            {{ project.name }}
          </h2>
          <el-tag :type="getStageType(project.currentStage)">{{
            getProjectStageText(project.currentStage)
          }}</el-tag>
        </div>

        <div class="project-info">
          <p>
            <strong style="font-weight: bold; color: #2f4e73">Country:</strong>
            {{ project.country }}
          </p>
          <p>
            <strong style="font-weight: bold; color: #2f4e73">Category:</strong>
            {{ getProjectCategoryText(project.category) }}
          </p>
        </div>

        <div class="project-image" v-if="project.project_image_id">
          <el-image :src="project.project_image_id" fit="fill" />
        </div>
        <div v-else class="project-image-placeholder">
          <el-empty description="No image" :image-size="100">
            <template #image>
              <el-icon :size="60" style="color: #909399"><Picture /></el-icon>
            </template>
          </el-empty>
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
      </el-card>
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

const updateScreen = () => {
  isTablet.value = window.innerWidth <= 768;
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
.project-list {
  height: 100%;
  padding: 20px;
}

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

.projects-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.project-card {
  transition: transform 0.3s;
}

.project-card:hover {
  transform: translateY(-5px);
}

.project-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.project-header h2 {
  margin: 0;
  font-size: 18px;
}

.project-info {
  margin-bottom: 15px;
}

.project-info p {
  margin: 5px 0;
}

.project-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
  border-radius: 4px;
}

.project-image .el-image {
  width: 100%;
  height: 100%;
}

.project-image-placeholder {
  width: 100%;
  height: 200px;
  overflow: hidden;
  border-radius: 4px;
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
</style>
