<template>
  <div class="manager-container">
    <div v-if="!isManager" class="access-denied">
      <h2>Access Denied</h2>
      <p>Sorry, you don't have permission to access this page.</p>
      <div class="invite-form">
        <h3>Request Admin Access</h3>
        <el-form :model="inviteForm" :rules="rules" ref="inviteForm">
          <el-form-item prop="email">
            <el-input
              v-model="inviteForm.email"
              placeholder="Please enter your email address"
              prefix-icon="el-icon-message"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="requestInvite" :loading="loading">
              Send Invitation Code
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div v-else>
      <!-- Admin dashboard content -->
      <h2>Admin Dashboard</h2>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ManagerView',
  data() {
    return {
      isManager: false,
      loading: false,
      inviteForm: {
        email: ''
      },
      rules: {
        email: [
          { required: true, message: 'Please enter your email address', trigger: 'blur' },
          { type: 'email', message: 'Please enter a valid email address', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.checkManagerStatus()
  },
  methods: {
    async checkManagerStatus() {
      try {
        // Add logic to check if user is manager
        // const response = await this.$api.checkManagerStatus()
        // this.isManager = response.isManager
      } catch (error) {
        console.error('Failed to check manager status:', error)
      }
    },
    async requestInvite() {
      try {
        this.loading = true
        await this.$refs.inviteForm.validate()
        // Add API call to send invitation code
        // await this.$api.requestManagerInvite(this.inviteForm.email)
        this.$message.success('Invitation code has been sent to your email')
      } catch (error) {
        if (error === false) {
          this.$message.error('Please check your form input')
        } else {
          this.$message.error('Failed to send invitation code, please try again later')
        }
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.manager-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.access-denied {
  text-align: center;
  padding: 40px 20px;
}

.invite-form {
  max-width: 400px;
  margin: 30px auto;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

h2 {
  color: #303133;
  margin-bottom: 20px;
}

h3 {
  color: #606266;
  margin-bottom: 20px;
}
</style>
