<template>
  <el-container class="layout-container">
    <el-header class="layout-header">
      <div class="logo">
        <h3>智能粮食管理系统</h3>
      </div>
      <div class="user-info">
        <span>{{ userInfo.fullName }} ({{ userInfo.role }})</span>
        <el-button type="text" @click="handleLogout">退出登录</el-button>
      </div>
    </el-header>
    <el-container>
      <el-aside width="200px" class="layout-sidebar">
        <el-menu :default-active="activeMenu" router>
          <el-menu-item index="/dashboard">
            <i class="el-icon-menu"></i>
            <span slot="title">统计看板</span>
          </el-menu-item>
          <el-menu-item index="/transaction-entry">
            <i class="el-icon-document"></i>
            <span slot="title">交易录入</span>
          </el-menu-item>
          <el-menu-item index="/data-details">
            <i class="el-icon-tickets"></i>
            <span slot="title">数据明细</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="layout-main">
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'Layout',
  computed: {
    activeMenu() {
      return this.$route.path
    },
    userInfo() {
      return this.$store.state.auth.userInfo || {}
    }
  },
  methods: {
    handleLogout() {
      this.$confirm('确认退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('auth/logout')
        this.$router.push('/login')
        this.$message.success('已退出登录')
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;

  .layout-header {
    background: #303133;
    color: #fff;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;

    .logo {
      h3 {
        margin: 0;
      }
    }

    .user-info {
      span {
        margin-right: 15px;
      }
    }
  }

  .layout-sidebar {
    background: #fff;
    border-right: 1px solid #e6e6e6;
  }

  .layout-main {
    background: #f5f7fa;
    padding: 20px;
  }
}
</style>