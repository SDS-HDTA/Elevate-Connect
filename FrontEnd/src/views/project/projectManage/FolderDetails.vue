<template>
  <div class="folder-container">
    <div class="header">
      <el-button class="back-button" @click="handleBack" :icon="ArrowLeft" text>
        <span class="title-text"
          >{{ getProjectStageText(projectStatus) }} Iteration-{{
            iterationId
          }}</span
        >
      </el-button>
    </div>
    <div class="content">
      <div
        class="section"
        v-for="(section, index) in filteredSections"
        :key="section.title"
        :class="`section-${index % 4}`"
      >
        <div class="section-header">
          <div class="section-title">
            <span>{{ section.title }}</span>
          </div>
          <div class="section-search">
            <el-input
              v-model="searchTexts[index]"
              placeholder="Search..."
              :prefix-icon="Search"
              clearable
            />
          </div>
          <div class="section-action">
            <el-button
              v-require-permission="permissions.UploadFile"
              class="btn-primary"
              :icon="Plus"
              @click="handleNewClick(index)"
              >Add file</el-button
            >
          </div>
        </div>
        <div class="section-content">
          <el-card
            v-loading="loading"
            v-for="file in section.filteredFiles"
            :key="file.id"
            class="file-card"
            :class="{ highlighted: file.isHighlighted }"
            shadow="hover"
            @click="handleFileClick(file)"
          >
            <div class="file-content">
              <el-icon class="file-icon">
                <component :is="getFileIcon(file.type)" />
              </el-icon>
              <span class="file-name">{{ file.name }}</span>
            </div>
            <div
              v-require-permission="permissions.DeleteFile"
              class="delete-button"
              @click.stop="handleDeleteFile(file)"
            >
              <el-icon><Close /></el-icon>
            </div>
          </el-card>
        </div>
      </div>
    </div>
    <!-- File Upload Dialog -->
    <el-dialog
      v-model="fileDialogVisible"
      :title="dialogTitle"
      width="500px"
      :close-on-click-modal="false"
      @close="handleFileDialogClose"
    >
      <div class="file-upload-form">
        <el-upload
          class="file-uploader"
          :auto-upload="false"
          :show-file-list="true"
          :limit="1"
          :accept="fileAccept"
          :on-change="handleFileChange"
          :on-remove="handleFileRemove"
          :file-list="fileList"
        >
          <div class="flex flex-column">
            <el-empty
              v-if="fileList.length === 0"
              class="pt-3 pb-3"
              description="Choose a file to upload"
              :image-size="100"
            >
              <template #image>
                <el-icon :size="80" style="color: #909399"><Upload /></el-icon>
              </template>
            </el-empty>
            <el-button class="btn-secondary" :disabled="!!fileForm.file"
              ><el-icon> <Plus /> </el-icon><span>Select File</span></el-button
            >
          </div>
        </el-upload>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button class="btn-secondary" @click="handleFileDialogClose"
            >Cancel</el-button
          >
          <el-button
            class="btn-primary"
            @click="handleFileSubmit"
            :loading="uploading"
            :disabled="!fileForm.file"
          >
            <el-icon>
              <Upload v-if="!uploading" />
            </el-icon>
            <span>Upload</span>
          </el-button>
        </span>
      </template>
    </el-dialog>
    <!-- Add image preview component -->
    <el-image-viewer
      v-if="showImageViewer"
      :url-list="[previewImageUrl]"
      :initial-index="0"
      :show-progress="true"
      :hide-on-click-modal="true"
      :close-on-press-escape="true"
      @close="showImageViewer = false"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import {
  ArrowLeft,
  Search,
  Plus,
  Document,
  DataBoard,
  Picture,
  VideoPlay,
  Close,
  Upload,
} from '@element-plus/icons-vue';
import { miroApi } from '@/utils/mirorequest';
import { docApi } from '@/utils/docrequest';
import { ElMessage, ElMessageBox } from 'element-plus';
import request from '@/utils/request';
import { ElImageViewer } from 'element-plus';
import { permissions } from '@/models/permission';
import { getProjectStageText } from '../../../utils/projectStageHelper';
import { useUserStore } from '@/stores/userStore';
import { getLegibleFileSize } from '@/utils/imageHelper';

const router = useRouter();
const route = useRoute();
const isAuthenticated = ref(false);

const projectStatus = ref(route.params.statusId || '');
const iterationId = ref(route.params.iterationId || '');
const projectId = ref(route.params.projectId || '');

const loading = ref(false);
const userStore = useUserStore();
const userId = computed(() => userStore.userInfo?.id || null);

const sections = ref([
  { title: 'Documents', searchText: '', files: [] },
  //   { title: 'Whiteboard', searchText: '', files: [] }, TODO: Restore Miro functionality
  { title: 'Picture', searchText: '', files: [] },
  { title: 'Video', searchText: '', files: [] },
]);

// Add reactive reference for search text
const searchTexts = ref(sections.value.map(() => ''));

// Modify computed property for filtered files
const filteredSections = computed(() => {
  // Iterate through each section (Page, Whiteboard, Picture, Video)
  return sections.value.map((section, index) => {
    // Get current section's search text and convert to lowercase
    const searchText = searchTexts.value[index].toLowerCase();

    // If no search text, return original file list
    if (!searchText) {
      return {
        ...section,
        filteredFiles: section.files, // Return original file list
        hasHighlight: false, // No highlighting
      };
    }

    // If there is search text, process file list
    const filteredFiles = section.files.map((file) => ({
      ...file,
      isHighlighted: file.name.toLowerCase().includes(searchText), // Mark if highlighted
    }));

    return {
      ...section,
      filteredFiles, // Return processed file list
      hasHighlight: filteredFiles.some((file) => file.isHighlighted), // Whether there are highlighted files
    };
  });
});

const files = ref([]);

// Get all files
const fetchAllFiles = async () => {
  try {
    const res = await request.get(`/projects/${projectId.value}/files`, {
      params: {
        projectStatus: projectStatus.value,
        iterationId: iterationId.value,
      },
    });
    if (res.code === 1) {
      files.value = res.data;
      sections.value.forEach((section, index) => {
        section.files = files.value.filter((file) => file.type === index);
      });
    } else {
      throw new Error(res.message || 'Failed to fetch files');
    }
  } catch (error) {
    console.error('Failed to fetch files:', error);
    ElMessage.error('Failed to fetch files');
  }
};

// Check authentication status
const checkAuthStatus = async () => {
  try {
    const [miroSuccess, docSuccess] = await Promise.all([
      miroApi.getMiroTokens(),
      docApi.getDocTokens(),
    ]);
    isAuthenticated.value = miroSuccess && docSuccess;
    if (!miroSuccess || !docSuccess) {
      ElMessage.error('Failed to get authentication');
    }
  } catch (error) {
    console.error('Authentication check failed:', error);
    ElMessage.error('Authentication check failed');
  }
};

// Go back to previous page
const handleBack = () => {
  miroApi.clearMiroTokens();
  docApi.clearDocTokens();
  router.back();
};

// Add image preview related state variables
const showImageViewer = ref(false);
const previewImageUrl = ref('');

// Handle file click event
const handleFileClick = (file) => {
  if (file.type === 0) {
    // Documents type
    // Get file extension
    const fileExtension = file.name.split('.').pop().toLowerCase();

    if (fileExtension) {
      window.open(file.source, '_blank');
    }
  } else if (file.type === 1) {
    // Picture type
    // Show image preview
    previewImageUrl.value = file.source;
    showImageViewer.value = true;
  } else if (file.type === 2) {
    // Video type
    // Directly open video in new tab
    window.open(file.source, '_blank');
  } else if (file.type === 3) {
    // Whiteboard type
    // Extract boardId from source
    const boardId = file.source;
    // Navigate to whiteboard page
    router.push({
      name: 'miro-board',
      params: { boardId },
    });
  }
};

// Get corresponding icon based on file type
const getFileIcon = (type) => {
  const icons = {
    0: Document,
    1: Picture,
    2: VideoPlay,
    3: DataBoard,
  };
  return icons[type] || Document;
};

// Handle create new button click
const handleNewClick = (sectionIndex) => {
  switch (sectionIndex) {
    case 0: // Documents
      handleNewDocument();
      break;
    case 1: // Picture
      handleNewPicture();
      break;
    case 2: // Video
      handleNewVideo();
      break;
    case 3: // Whiteboard
      handleNewWhiteboard();
      break;
  }
};

// Create new Documents
const handleNewDocument = () => {
  fileForm.value.type = 0;
  fileDialogVisible.value = true;
};

// Create new Whiteboard
const handleNewWhiteboard = async () => {
  try {
    const { value: whiteboardName } = await ElMessageBox.prompt(
      'Enter whiteboard name',
      'New Whiteboard',
      {
        confirmButtonClass: 'btn-primary',
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        inputPattern: /\S+/,
        inputErrorMessage: 'Whiteboard name cannot be empty',
      }
    );

    if (whiteboardName) {
      const miroResponse = await miroApi.createBoard({
        name: whiteboardName,
      });

      if (miroResponse && miroResponse.id) {
        const result = await request.post('/projects/files/whiteboard', {
          name: whiteboardName,
          source: miroResponse.id,
          creatorId: localStorage.getItem('userId'),
          projectStatus: projectStatus.value,
          iterationId: iterationId.value,
          projectId: projectId.value,
          type: 1,
        });

        if (result.code === 1) {
          const newWhiteboard = {
            id: result.data.id,
            name: whiteboardName,
            type: 1,
            source: miroResponse.id,
            creator: result.data.creator,
            creatorId: result.data.creatorId,
            projectStatus: result.data.projectStatus,
            iterationId: result.data.iterationId,
            projectId: result.data.projectId,
          };

          sections.value[1].files.push(newWhiteboard);

          ElMessage.success('Whiteboard created successfully');
        } else {
          throw new Error(
            result.message || 'Failed to save whiteboard information'
          );
        }
      } else {
        throw new Error('Failed to create Miro whiteboard');
      }
    }
  } catch (error) {
    console.error('Failed to create whiteboard:', error);
    ElMessage.error(error.message || 'Failed to create whiteboard');
  }
};

// File upload related
const fileDialogVisible = ref(false);
const fileForm = ref({
  name: '',
  file: null,
  type: null,
});
const fileList = ref([]);
const uploading = ref(false);

// Computed property: dialog title
const dialogTitle = computed(() => {
  switch (fileForm.value.type) {
    case 0:
      return 'New Document';
    case 2:
      return 'New Picture';
    case 3:
      return 'New Video';
    default:
      return '';
  }
});

// Computed property: file accept types
const fileAccept = computed(() => {
  switch (fileForm.value.type) {
    case 0: // Documents
      return '.pdf,.docx,.doc';
    case 1: // Picture
      return 'image/*';
    case 2: // Video
      return 'video/*';
    default:
      return '';
  }
});

// Handle file selection
const handleFileChange = (file) => {
  if (file.size <= MAX_FILE_SIZE_BYTES) {
    fileForm.value.file = file.raw;
    fileList.value = [file];
  } else {
    ElMessage.error(
      `File is too large, the max size is ${getLegibleFileSize()}MB`
    );
    handleFileRemove();
    return;
  }
};

// Handle file removal
const handleFileRemove = () => {
  fileForm.value.file = null;
  fileList.value = [];
};

// Handle dialog close
const handleFileDialogClose = () => {
  fileForm.value = {
    name: '',
    file: null,
    type: null,
  };
  fileList.value = [];
  fileDialogVisible.value = false;
};

const getFileTypeName = (type) => {
  switch (type) {
    case 0:
      return 'Document';
    case 1:
      return 'Picture';
    case 2:
      return 'Video';
  }
};

const isFileValidFormat = (file, type) => {
  switch (type) {
    case 0:
      return file.name.match(/.*\.(pdf|docx|doc)$/i);
    case 1:
      return file.type.startsWith('image/');
    case 2:
      return file.type.startsWith('video/');
  }
};

// Handle file submission
const handleFileSubmit = async () => {
  if (!fileForm.value.file) {
    ElMessage.warning('Please select a file');
    return;
  }

  if (!isFileValidFormat(fileForm.value.file, fileForm.value.type)) {
    ElMessage.warning(
      `File has invalid extension for ${getFileTypeName(fileForm.value.type)}.`
    );
    return;
  }

  try {
    uploading.value = true;
    const uploadData = new FormData();
    uploadData.append('creatorId', userId.value);
    uploadData.append('iterationId', iterationId.value);
    uploadData.append('type', fileForm.value.type);
    uploadData.append('file', fileForm.value.file);

    const result = await request.post(`/projects/files`, uploadData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    if (result.code === 1) {
      const newFile = {
        id: result.data.id,
        name: result.data.name,
        type: fileForm.value.type,
        creator: result.data.creator,
        creatorId: result.data.creatorId,
        projectStatus: result.data.projectStatus,
        iterationId: result.data.iterationId,
        projectId: result.data.projectId,
        source: result.data.source,
      };

      sections.value[fileForm.value.type].files.push(newFile);

      let successMessage = getFileTypeName(fileForm.value.type);

      ElMessage.success(`${successMessage} uploaded successfully`);
      handleFileDialogClose();
    } else {
      throw new Error(result.message || 'Upload failed');
    }
  } catch (error) {
    console.error('Upload failed:', error);
    ElMessage.error(error.message || 'Upload failed');
  } finally {
    uploading.value = false;
  }
};

// Create new Picture
const handleNewPicture = () => {
  fileForm.value.type = 1;
  fileDialogVisible.value = true;
};

// Create new Video
const handleNewVideo = () => {
  fileForm.value.type = 2;
  fileDialogVisible.value = true;
};

// Show delete confirmation dialog
const showDeleteConfirm = async () => {
  try {
    await ElMessageBox.confirm(
      'Are you sure you want to delete this file? This action cannot be undone.',
      'Confirm',
      {
        confirmButtonClass: 'btn-danger',
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
      }
    );
    return true;
  } catch (error) {
    return false;
  }
};

// Remove file from list
const removeFileFromList = (file) => {
  const sectionIndex = file.type;
  sections.value[sectionIndex].files = sections.value[
    sectionIndex
  ].files.filter((f) => f.id !== file.id);
};

// Base method for deleting files
const deleteFileBase = async (file) => {
  loading.value = true;
  try {
    const result = await request.delete(`/projects/files/${file.id}`);
    if (result.code === 1) {
      removeFileFromList(file);
      ElMessage.success('File deleted successfully');
      return true;
    } else {
      throw new Error(result.message || 'Delete failed');
    }
  } catch (error) {
    console.error('Failed to delete file:', error);
    ElMessage.error(error.message || 'Failed to delete file');
    return false;
  } finally {
    loading.value = false;
  }
};

// Delete Miro whiteboard
const deleteMiroWhiteboard = async (file) => {
  try {
    // Extract boardId from source
    const boardId = file.source.split(':')[1];
    // Call Miro API to delete whiteboard
    await miroApi.deleteBoard(boardId);
    // After successful deletion, remove database record
    return await deleteFileBase(file);
  } catch (error) {
    console.error('Delete Miro whiteboard failed:', error);
    ElMessage.error('Delete Miro whiteboard failed');
    return false;
  }
};

// Handle file deletion
const handleDeleteFile = async (file) => {
  // Prevent event bubbling
  event.stopPropagation();

  try {
    // Show confirmation dialog
    if (!(await showDeleteConfirm())) {
      return;
    }

    // Call different deletion methods based on file type
    switch (file.type) {
      case 0: // Documents
      case 1: // Picture
      case 2: // Video
        await deleteFileBase(file);
        break;
      case 3: // Whiteboard
        await deleteMiroWhiteboard(file);
        break;
    }
  } catch (error) {
    console.error('Delete file failed:', error);
    ElMessage.error(error.message || 'Delete file failed');
  }
};

onMounted(async () => {
  //   await checkAuthStatus();
  await fetchAllFiles();
});

// onBeforeUnmount(() => {
//   miroApi.clearMiroTokens();
//   docApi.clearDocTokens();
// });
</script>

<style scoped>
.folder-container {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
}

.header {
  padding: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.back-button {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 0;
  height: auto;
  transition: all 0.3s ease;
}

.back-button:hover {
  transform: translateX(-4px);
}

.back-button:hover :deep(.el-icon) {
  color: #409eff;
}

.back-button:hover .title-text {
  color: #409eff;
}

.back-button :deep(.el-icon) {
  font-size: 20px;
  color: #606266;
  transition: color 0.3s ease;
}

.title-text {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  transition: color 0.3s ease;
}

.content {
  flex: 1;
  padding: 20px;
  background-color: var(--color-background-light);
  display: flex;
  flex-direction: column;
  gap: 20px;
  overflow-y: auto;
}

.section {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.section:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.1);
}

.section-header {
  padding: 16px 20px;
  background-color: var(--color-white);
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  align-items: center;
  gap: 20px;
  border-radius: 8px 8px 0 0;
  position: relative;
}

.section-header::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 4px;
  background-color: var(--color-primary);
  border-radius: 4px 0 0 4px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  min-width: 120px;
  display: flex;
  align-items: center;
  gap: 8px;
  padding-left: 12px;
}

.section-title span {
  font-weight: 600;
}

.section-search {
  flex: 1;
  max-width: 300px;
}

.section-action {
  margin-left: auto;
}

.section-content {
  padding: 20px;
  min-height: 200px;
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

/* Custom scrollbar styles */
.content::-webkit-scrollbar {
  width: 6px;
}

.content::-webkit-scrollbar-thumb {
  background-color: #909399;
  border-radius: 3px;
}

.content::-webkit-scrollbar-track {
  background-color: var(--color-background-light);
}

.file-card {
  width: 200px;
  cursor: pointer;
  transition: all 0.3s ease;
  border-radius: 8px;
  overflow: hidden;
  position: relative;
}

.file-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.file-card:hover .delete-button {
  opacity: 1;
}

.delete-button {
  position: absolute;
  top: 8px;
  right: 8px;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: #f56c6c;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  opacity: 0;
  transition: all 0.3s ease;
  z-index: 1;
}

.delete-button:hover {
  transform: scale(1.1);
  background-color: #e64242;
}

.delete-button .el-icon {
  font-size: 14px;
}

.file-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  padding: 10px;
}

.file-icon {
  font-size: 32px;
  color: #409eff;
}

.file-name {
  font-size: 14px;
  color: #606266;
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 100%;
}

.file-upload-form {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.file-uploader {
  display: flex;
  flex-direction: column-reverse;
  align-items: center;
  justify-content: center;
  width: 100%;
}

.file-card.highlighted {
  background-color: #fff7e6;
  border: 2px solid #ffa940;
  animation: highlight-pulse 1.5s infinite;
}

@keyframes highlight-pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(255, 169, 64, 0.4);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(255, 169, 64, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(255, 169, 64, 0);
  }
}
</style>
