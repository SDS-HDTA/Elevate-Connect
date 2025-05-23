<template>
  <div class="member-container">
    <div v-if="loading" class="loading">
      Loading...
    </div>
    <div v-else>
      <!-- Project Owner -->
      <div v-if="projectOwner" class="member-row owner">
        <div class="member-info">
          <Avatar :firstName="projectOwner.firstName" :lastName="projectOwner.lastName" :size="40" />
          <div class="member-details">
            <span class="name">{{ projectOwner.firstName }} {{ projectOwner.lastName }}</span>
            <span class="email">{{ projectOwner.email }}</span>
          </div>
          <div class="member-type">Project Owner</div>
          <el-tag type="success" class="owner-tag">Owner</el-tag>
        </div>
      </div>

      <!-- Other Members -->
      <div v-for="member in otherMembers" :key="member.id" class="member-row">
        <div class="member-info">
          <Avatar :firstName="member.firstName" :lastName="member.lastName" :size="40" />
          <div class="member-details">
            <span class="name">{{ member.firstName }} {{ member.lastName }}</span>
            <span class="email">{{ member.email }}</span>
          </div>
          <div class="member-type">{{ member.type }}</div>
          <el-button v-if="isProjectOwner" type="danger" size="small" @click="handleRemoveMember(member)">
            Remove
          </el-button>
        </div>
      </div>
    </div>

    <!-- Remove Confirmation Dialog -->
    <el-dialog v-model="removeDialogVisible" title="Confirm Removal" width="30%">
      <span>Are you sure you want to remove this member?</span>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="removeDialogVisible = false">Cancel</el-button>
          <el-button type="danger" @click="confirmRemove">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'
import Avatar from '@/components/Avatar.vue'
import request from '@/utils/request'

const route = useRoute()
const projectId = route.params.id

const loading = ref(true)
const members = ref([])
const removeDialogVisible = ref(false)
const selectedMember = ref(null)
const isProjectOwner = ref(false)
const creatorId = ref(null)

// Split userName into firstName and lastName
const splitName = (userName) => {
  const nameParts = userName.split(' ')
  return {
    firstName: nameParts[0] || '',
    lastName: nameParts.slice(1).join(' ') || ''
  }
}

// Fetch project members
const fetchMembers = () => {
  loading.value = true
  try {
    const localMembers = JSON.parse(localStorage.getItem(`project_${projectId}_members`) || '[]')
    members.value = localMembers.map(member => ({
      ...member,
      ...splitName(member.username)
    }))
    // 判断是否为项目拥有者
    creatorId.value = localMembers.find(m => m.isOwner)?.userId
    if (creatorId.value && creatorId.value == localStorage.getItem('userId')) {
      isProjectOwner.value = true
    }
  } catch (error) {
    ElMessage.error('Failed to load members from localStorage')
  } finally {
    loading.value = false
  }
}

// Computed properties: separate project owner and other members
const projectOwner = computed(() => {
  return members.value.find(member => member.isOwner)
})

const otherMembers = computed(() => {
  return members.value.filter(member => !member.isOwner)
})

// Handle member removal
const handleRemoveMember = (member) => {
  selectedMember.value = member
  removeDialogVisible.value = true
}

// Confirm member removal
const confirmRemove = async () => {
  try {
    const currentUserId = localStorage.getItem('userId')
    await request.delete(`/projects/${projectId}/members/${selectedMember.value.userId}`, {
      params: {
        userId: currentUserId
      }
    })
    ElMessage.success('Member removed successfully')
    removeDialogVisible.value = false
    await fetchMembers() // Refresh member list
  } catch (error) {
    ElMessage.error('Failed to remove member')
  }
}

onMounted(() => {
  fetchMembers()
})
</script>

<style scoped>
.member-container {
  padding: 20px;
}

.loading {
  text-align: center;
  padding: 20px;
  color: #666;
}

.member-row {
  padding: 15px;
  border-bottom: 1px solid #eee;
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
  font-weight: bold;
  font-size: 16px;
}

.email {
  color: #666;
  font-size: 14px;
}

.member-type {
  color: #666;
  min-width: 100px;
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