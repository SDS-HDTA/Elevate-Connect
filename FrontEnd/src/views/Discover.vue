<template>
  <div class="home-page">
    <Header class="header" />
    <div class="main-content">
      <Sidebar v-if="!isTablet" class="sidebar" />
      <div class="content">
        <div class="project-list-vertical">
          <div class="header-container">
            <h2>Discover Projects</h2>
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
                <el-option label="Area" :value="2" />
              </el-select>
            </div> -->
          </div>

          <div v-if="projects.length" class="projects-list">
            <el-card
              v-for="project in projects"
              :key="project.id"
              class="project-card"
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
                  <div v-if="!isSmallScreen">
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
                    <div
                      v-if="userRole === 2"
                      class="mt-3 flex flex-column align-items-center justify-content-center"
                    >
                      <div class="flex align-items-center">
                        <el-icon class="me-1"><Message /></el-icon>
                        <span>Want to explore this opportunity further?</span>
                      </div>
                      <a
                        class="btn-link-primary ms-1"
                        :href="createInterestedEmail(project)"
                        >Contact the Elevate team here</a
                      >
                    </div>
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
                <div v-if="isSmallScreen">
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
                  <div
                    v-if="userRole === 2"
                    class="mt-3 flex flex-column align-items-center justify-content-center"
                  >
                    <div class="flex align-items-center">
                      <el-icon class="me-1"><Message /></el-icon>
                      <span>Want to explore this opportunity further?</span>
                    </div>
                    <a
                      class="btn-link-primary ms-1"
                      :href="createInterestedEmail(project)"
                      >Contact the Elevate team here</a
                    >
                  </div>
                </div>
              </div>
            </el-card>
          </div>

          <div v-if="projects.length" class="pagination-container">
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :page-sizes="[5, 10, 20, 50]"
              :total="total"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </div>
        <div v-if="!projects.length && !loading">
          <el-empty
            :description="
              searchQuery
                ? 'No projects found matching your search criteria.'
                : 'No projects are available yet.'
            "
            :image-size="150"
          >
            <template #image>
              <el-icon :size="80" style="color: #909399"
                ><WarningFilled
              /></el-icon>
            </template>
          </el-empty>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { Picture, Message } from '@element-plus/icons-vue';
import Header from '@/components/Header.vue';
import Sidebar from '@/components/Sidebar.vue';
import request from '@/utils/request';
import { getProgressPercentage } from '@/utils/getProgressPercentage';
import { getStageType, getProjectStageText } from '@/utils/projectStageHelper';
import { getProjectCategoryText } from '@/utils/projectCategoryHelper';
import { createMailTo } from '@/utils/createMailTo';
import { useUserStore } from '@/stores/userStore';

const searchType = ref(0); // 0-Name, 1-Category, 2-Area
const searchQuery = ref('');
const projects = ref([]);
const currentPage = ref(1);
const pageSize = ref(5);
const total = ref(0);
const loading = ref(false);
const isTablet = ref(window.innerWidth <= 768);
const isSmallScreen = ref(window.innerWidth <= 600);
const userStore = useUserStore();
const userRole = computed(() => {
  const t = userStore.userInfo?.role ?? 0;
  return Number(t);
});
const userName = computed(() => userStore?.userInfo?.fullName || '');

const updateScreen = () => {
  isTablet.value = window.innerWidth <= 768;
  isSmallScreen.value = window.innerWidth <= 600;
};

onMounted(async () => {
  window.addEventListener('resize', updateScreen);
});

onUnmounted(() => {
  window.removeEventListener('resize', updateScreen);
});

// Fetch project list
const fetchProjects = async () => {
  loading.value = true;
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      searchType: searchType.value,
      searchValue: searchQuery.value,
    };
    const res = await request.get('/projects/all', { params });
    if (res.code === 1) {
      projects.value = res.data.records.map((projectResponse) => ({
        ...projectResponse.project,
        country: projectResponse.community.country,
        imageSrc: projectResponse.projectImageSrc,
      }));
      total.value = res.data.total;
    }
  } catch (error) {
    console.error('Failed to fetch projects:', error);
  } finally {
    loading.value = false;
  }
};

// Handle search input
const handleSearch = () => {
  currentPage.value = 1;
  fetchProjects();
};

// Handle page number change
const handleCurrentChange = (val) => {
  currentPage.value = val;
  fetchProjects();
};

// Handle page size change
const handleSizeChange = (val) => {
  pageSize.value = val;
  currentPage.value = 1;
  fetchProjects();
};

const handleClear = () => {
  searchQuery.value = '';
  searchType.value = 0;
  currentPage.value = 1;
  fetchProjects();
};

onMounted(() => {
  fetchProjects();
  // Global error handling can be added here
  window.addEventListener('unhandledrejection', (event) => {
    ElMessage.error('An error occurred. Please try again later.');
    console.error('Unhandled promise rejection:', event.reason);
  });
});

const createInterestedEmail = (project) => {
  return createMailTo(
    `Interest in ${project.name} - Inquiry via Elevate`,
    `Dear Elevate Team,\n\nI'm reaching out to express my interest in the ${project.name}. I came across the opportunity through your platform and would love to learn more about how I can get involved or support this work.\n\nPlease let me know the next steps or any additional information you can share.\n\nThank you,\n${userName.value}\n${userStore.userInfo?.organization}\nContact Info:\n${userStore.userInfo?.email}\n${userStore.userInfo?.phone}`
  );
};
</script>

<style scoped>
.home-page {
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

.sidebar {
  position: fixed;
  left: 0;
  top: 60px;
  bottom: 0;
}

.content {
  flex: 1;

  @media screen and (min-width: 769px) {
    margin-left: 200px; /* Adjust based on sidebar width */
  }
  background-color: var(--color-background-light);
  min-height: calc(100vh - 60px);
}

.project-list-vertical {
  padding: 20px;
  margin: 0 auto;
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

.pagination-container {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

.image-container {
  display: flex;
  justify-content: center;
  align-items: center;
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>
