<template>
  <div class="member-container">
    <div v-for="group in groupedMembers" :key="group.role" class="member-group">
      <div class="group-header">
        {{ getUserRole(Number(group.role)) }}
      </div>
      <div v-for="member in group.members" :key="member.id" class="member-row">
        <div class="member-info">
          <Avatar
            v-if="!isSmallScreen"
            :full-name="member.firstName + ' ' + member.lastName"
            :size="40"
          />
          <div class="member-details">
            <span class="name">{{
              member.firstName + ' ' + member.lastName
            }}</span>
            <span class="email">{{ member.email }}</span>
          </div>
          <div class="member-actions">
            <el-tag :type="getUserRoleClass(member.role)" class="member-type">
              {{ getUserRole(member.role) }}
            </el-tag>
            <el-tooltip placement="top" content="Remove Member">
              <el-button
                v-if="userRole === 3"
                class="btn-icon-danger"
                @click="handleRemoveMember(member)"
              >
                <el-icon><Remove /></el-icon>
              </el-button>
            </el-tooltip>
          </div>
        </div>
      </div>
    </div>

    <!-- Remove Confirmation Dialog -->
    <el-dialog
      v-model="removeDialogVisible"
      title="Confirm Removal"
      width="30%"
    >
      <span>Are you sure you want to remove this member?</span>
      <template #footer>
        <span class="dialog-footer">
          <el-button class="btn-secondary" @click="removeDialogVisible = false"
            >Cancel</el-button
          >
          <el-button class="btn-danger" @click="confirmRemove"
            >Confirm</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, onUnmounted } from 'vue';
import { ElMessage } from 'element-plus';
import { useRoute } from 'vue-router';
import { Remove } from '@element-plus/icons-vue';
import Avatar from '@/components/Avatar.vue';
import request from '@/utils/request';
import { useUserStore } from '@/stores/userStore';
import { getUserRole, getUserRoleClass } from '@/utils/roleHelper';

const route = useRoute();
const projectId = route.params.id;

const members = ref([]);
const removeDialogVisible = ref(false);
const selectedMember = ref(null);
const userStore = useUserStore();
const isSmallScreen = computed(() => window.innerWidth <= 600);
const userRole = computed(() => {
  const t = userStore.userInfo?.role ?? 0;
  return Number(t);
});
const groupedMembers = computed(() => {
  const groups = {};
  members.value.forEach((member) => {
    if (!groups[member.role]) groups[member.role] = [];
    groups[member.role].push(member);
  });

  const roleOrder = [3, 2, 1, 0]; // Elevate Lead, Humanitarian, Country, Community

  return roleOrder
    .filter((role) => groups[role])
    .map((role) => ({
      role,
      members: groups[role].sort((a, b) => {
        // Optional: sort members alphabetically by name
        const nameA = a.firstName + ' ' + a.lastName;
        const nameB = b.firstName + ' ' + b.lastName;
        return nameA.localeCompare(nameB);
      }),
    }));
});

// Get project members
const fetchMembers = () => {
  try {
    const storedMembers = localStorage.getItem(`project_${projectId}_members`);
    if (!storedMembers) {
      console.warn('No members found in localStorage');
      members.value = [];
      return;
    }
    members.value = JSON.parse(storedMembers);
  } catch (error) {
    console.error('Error fetching members:', error);
    members.value = [];
  }
};

// Handle member removal
const handleRemoveMember = (member) => {
  selectedMember.value = member;
  removeDialogVisible.value = true;
};

// Confirm member removal
const confirmRemove = async () => {
  try {
    const currentUserId = localStorage.getItem('userId');
    const res = await request.delete(
      `/projects/${projectId}/members/${selectedMember.value.userId}`,
      {
        params: {
          userId: currentUserId,
        },
      }
    );
    if (res.code === 1) {
      ElMessage.success('Member removed successfully');
      removeDialogVisible.value = false;
      localStorage.setItem(
        `project_${projectId}_members`,
        JSON.stringify(res.data)
      );
      members.value = res.data;
    } else {
      ElMessage.error('Failed to remove member');
    }
  } catch (error) {
    ElMessage.error('Failed to remove member');
  }
};

const updateScreen = () => {
  isSmallScreen.value = window.innerWidth <= 600;
};

onMounted(() => {
  window.addEventListener('resize', updateScreen);
  fetchMembers();
});

onUnmounted(() => {
  window.removeEventListener('resize', updateScreen);
});
</script>

<style scoped>
.group-header {
  font-weight: 600;
  margin: 15px 0 10px;
  font-size: 14px;
  color: #333;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.member-container {
  padding: 20px;
  max-height: calc(100vh - 200px);
  overflow-y: auto;
}

.loading {
  text-align: center;
  padding: 20px;
  color: #666;
}

.member-row {
  padding: 15px;
  border-bottom: 1px solid #eee;

  @media (max-width: 600px) {
    padding: 15px 0;
  }
}

.member-row:last-child {
  border-bottom: none;
}

.member-row.owner {
  background-color: #f8f9fa;
}

.member-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.member-details {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.name {
  font-weight: 600;
  font-size: 16px;
}

.email {
  color: #666;
  font-size: 14px;
}

.member-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 220px;
  justify-content: flex-end;
}

.member-type {
  min-width: 120px;
  text-align: center;
}

.member-type :deep(.el-tag__content) {
  font-weight: bold;
}

.owner-tag {
  margin-left: 10px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
