<template>
  <el-container class="layout-container">
    <el-aside width="240px" class="sidebar">
      <div class="logo">
        <div class="logo-icon">
          <el-icon><Basket /></el-icon>
        </div>
        <span class="logo-text">FruitSys Pro</span>
      </div>
      <el-menu
        active-text-color="#d4af37"
        background-color="transparent"
        class="el-menu-vertical-demo"
        :default-active="activeMenu"
        text-color="#a0a0a0"
        router
        :border="false"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataLine /></el-icon>
          <span>数据统计</span>
        </el-menu-item>
        <el-menu-item index="/fruits">
          <el-icon><Apple /></el-icon>
          <span>水果库存</span>
        </el-menu-item>
        <el-menu-item index="/employees">
          <el-icon><User /></el-icon>
          <span>员工管理</span>
        </el-menu-item>
        <el-menu-item index="/customers">
          <el-icon><Avatar /></el-icon>
          <span>客户管理</span>
        </el-menu-item>
        <el-menu-item index="/suppliers">
          <el-icon><Van /></el-icon>
          <span>供应商管理</span>
        </el-menu-item>
        <el-menu-item index="/stockin">
          <el-icon><SoldOut /></el-icon>
          <span>进货记录</span>
        </el-menu-item>
        <el-menu-item index="/sales">
          <el-icon><Sell /></el-icon>
          <span>销售记录</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="glass-header">
        <div class="header-content">
          <h3 class="page-title">{{ currentRouteName }}</h3>
          <div class="header-right">
            <el-tag class="status-tag" type="success" effect="dark" round>
              <span class="dot"></span> 系统运行正常
            </el-tag>
            <el-dropdown @command="handleCommand" trigger="click">
              <div class="user-profile">
                <el-avatar
                  :size="36"
                  src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                  class="user-avatar"
                />
                <span class="username">Admin</span>
                <el-icon class="el-icon--right"><arrow-down /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu class="custom-dropdown">
                  <el-dropdown-item command="profile">
                    <el-icon><User /></el-icon>个人中心
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <el-icon><SwitchButton /></el-icon>退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>

      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessageBox } from "element-plus";

const route = useRoute();
const router = useRouter();

const activeMenu = computed(() => route.path);
const currentRouteName = computed(() => route.meta.title || "首页");

const handleCommand = (command) => {
  if (command === "logout") {
    ElMessageBox.confirm("确定要退出登录吗?", "提示", {
      type: "warning",
    }).then(() => {
      localStorage.removeItem("isAuthenticated");
      router.push("/login");
    });
  } else if (command === "profile") {
    ElMessageBox.alert("个人中心功能开发中...", "提示");
  }
};
</script>

<style scoped>
.layout-container {
  height: 100vh;
  background-color: #f5f7fa;
}

.sidebar {
  background-color: #1a1c20;
  color: #fff;
  transition: width 0.3s;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.15);
  z-index: 10;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  background: linear-gradient(90deg, #1a1c20 0%, #2c3e50 100%);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo-icon {
  width: 32px;
  height: 32px;
  background: var(--primary-color);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  color: #fff;
  font-size: 20px;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
  color: #fff;
  letter-spacing: 0.5px;
}

.el-menu {
  border-right: none;
  padding-top: 10px;
}

.el-menu-item {
  margin: 4px 10px;
  border-radius: 8px;
  height: 50px;
  line-height: 50px;
}

.el-menu-item.is-active {
  background-color: rgba(212, 175, 55, 0.15) !important;
  color: var(--primary-color) !important;
  font-weight: 600;
}

.el-menu-item:hover {
  background-color: rgba(255, 255, 255, 0.05) !important;
}

.glass-header {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  padding: 0 20px;
  height: 60px;
  position: sticky;
  top: 0;
  z-index: 9;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.page-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.status-tag {
  background: rgba(103, 194, 58, 0.1);
  border: 1px solid rgba(103, 194, 58, 0.2);
  color: #67c23a;
  display: flex;
  align-items: center;
  gap: 6px;
}

.dot {
  width: 6px;
  height: 6px;
  background: #67c23a;
  border-radius: 50%;
  display: inline-block;
}

.user-profile {
  cursor: pointer;
  display: flex;
  align-items: center;
  padding: 4px 8px;
  border-radius: 20px;
  transition: background 0.3s;
}

.user-profile:hover {
  background: rgba(0, 0, 0, 0.03);
}

.user-avatar {
  border: 2px solid #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.username {
  margin-left: 10px;
  font-weight: 500;
  color: #2c3e50;
}

.main-content {
  padding: 20px;
  background-color: #f5f7fa;
}
</style>
