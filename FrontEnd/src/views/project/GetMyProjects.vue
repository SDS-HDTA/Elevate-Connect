<template>
  <div class="project-list">
    <div class="header-container">
      <h2>My Projects</h2>
      <div class="button-group">
        <!-- TODO: Move this to manager view, only managers can assign projects -->
        <el-button
          class="btn-secondary"
          @click="$router.push('/my-projects/join')"
          >Join Project</el-button
        >
      </div>
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
          <el-option label="Area" :value="2" />
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
          <h2 style="font-weight: bold">{{ project.name }}</h2>
          <el-tag :type="getStatusType(project.status)">{{
            getStatusText(project.status)
          }}</el-tag>
        </div>

        <div class="project-info">
          <p>
            <strong style="font-weight: bold; color: #2f4e73">Area:</strong>
            {{ project.area }}
          </p>
          <p>
            <strong style="font-weight: bold; color: #2f4e73">Category:</strong>
            {{ project.category }}
          </p>
        </div>

        <div class="project-image" v-if="project.imageUrl">
          <el-image :src="project.imageUrl" fit="fill" />
        </div>
        <div v-else class="project-image-placeholder">
          <el-empty description="No image" :image-size="100">
            <template #image>
              <el-icon :size="60" style="color: #909399"><Picture /></el-icon>
            </template>
          </el-empty>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { Search, Picture, Menu } from '@element-plus/icons-vue';
import request from '@/utils/request';

const searchType = ref(0); // 0-Name, 1-Category, 2-Area
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

// Sample data
const mockProjects = [
  {
    id: '1',
    title: 'Emergency Shelter and Relief Distribution for Flood Victims',
    status: 'Completed',
    area: 'Assam, India',
    category: 'Disaster Relief / Shelter and Basic Needs',
    imageUrl: '/images/project1.jpg',
  },
  {
    id: '2',
    title: 'Mobile Health Clinics for Displaced Communities',
    status: 'Ongoing',
    area: 'Gaziantep, Türkiye',
    category: 'Healthcare Access / Conflict Response',
    imageUrl: '/images/project2.jpg',
  },
  {
    id: '3',
    title: 'School Meals and Nutrition Program',
    status: 'Planned',
    area: 'Tigray, Ethiopia',
    category: 'Food Security / Child Welfare',
    imageUrl: '/images/project3.jpg',
  },
];

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
      projects.value = res.data;
    }
  } catch (error) {
    console.error('Failed to fetch projects:', error);
    // Use sample data as fallback
    projects.value = mockProjects;
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

// Get tag type for status
const getStatusType = (status) => {
  const types = {
    0: 'info', // Empathise
    1: 'warning', // Discover
    2: 'success', // Define
    3: 'primary', // Ideate
    4: 'danger', // Prototype
    5: 'success', // Feedback
  };
  return types[status] || 'info';
};

// Get status display text
const getStatusText = (status) => {
  const texts = {
    0: 'Empathise',
    1: 'Discover',
    2: 'Define',
    3: 'Ideate',
    4: 'Prototype',
    5: 'Feedback',
  };
  return texts[status] || 'Unknown';
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
