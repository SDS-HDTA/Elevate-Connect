<template>
  <div class="member-container">
    <div v-for="member in members" :key="member.id" class="member-row">
      <div class="member-info">
        <Avatar :username="member.username" :size="40" />
        <div class="member-details">
          <span class="name">{{ member.username }}</span>
          <span class="email">{{ member.email }}</span>
        </div>
        <div class="member-type">{{ member.type }}</div>
        <el-button 
          v-if="isProjectOwner && String(member.id) !== String(creatorId)" 
          type="danger" 
          size="small" 
          @click="handleRemoveMember(member)"
        >
          Remove
        </el-button>
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
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'
import Avatar from '@/components/Avatar.vue'
import request from '@/utils/request'

const route = useRoute()
const projectId = route.params.id
const creatorId = ref(localStorage.getItem(`project_${projectId}_creatorId`))

const members = ref([])
const removeDialogVisible = ref(false)
const selectedMember = ref(null)
const isProjectOwner = ref(false)

// 检查是否为项目创建者
const checkIsProjectOwner = () => {
  const currentUserId = localStorage.getItem('userId')
  isProjectOwner.value = creatorId.value === currentUserId
  return isProjectOwner.value
}

// 获取项目成员
const fetchMembers = () => {
  try {
    const storedMembers = localStorage.getItem(`project_${projectId}_members`)
    if (!storedMembers) {
      console.warn('No members found in localStorage')
      members.value = []
      return
    }
    members.value = JSON.parse(storedMembers)
  } catch (error) {
    console.error('Error fetching members:', error)
    members.value = []
  }
}

// 处理成员移除
const handleRemoveMember = (member) => {
  selectedMember.value = member
  removeDialogVisible.value = true
}

// 确认移除成员
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
    await fetchMembers()
  } catch (error) {
    ElMessage.error('Failed to remove member')
  }
}

onMounted(() => {
  checkIsProjectOwner()
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