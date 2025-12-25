<template>
  <el-container>
    <el-aside width="220px">
      <div class="logo">
        <el-icon><Basket /></el-icon>
        <span>FruitSys Pro</span>
      </div>
      <el-menu
        active-text-color="#409EFF"
        background-color="#304156"
        class="el-menu-vertical-demo"
        :default-active="activeMenu"
        text-color="#bfcbd9"
        router
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
      <el-header>
        <div
          style="
            display: flex;
            justify-content: space-between;
            align-items: center;
            height: 100%;
          "
        >
          <h3 style="margin: 0">{{ currentRouteName }}</h3>
          <div style="display: flex; align-items: center">
            <el-tag type="success" effect="dark" style="margin-right: 15px"
              >系统运行正常</el-tag
            >
            <el-dropdown @command="handleCommand">
              <span
                class="el-dropdown-link"
                style="cursor: pointer; display: flex; align-items: center"
              >
                <el-avatar
                  :size="32"
                  src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                />
                <span style="margin-left: 8px">Admin</span>
                <el-icon class="el-icon--right"><arrow-down /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile"
                    >个人中心</el-dropdown-item
                  >
                  <el-dropdown-item divided command="logout"
                    >退出登录</el-dropdown-item
                  >
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>

      <el-main>
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
.el-container {
  height: 100vh;
}
</style>
