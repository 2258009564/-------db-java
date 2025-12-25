<template>
  <div class="login-container">
    <div class="login-card glass-panel">
      <div class="login-header">
        <div class="logo-icon">
          <el-icon :size="32"><Basket /></el-icon>
        </div>
        <h1>FruitSys Pro</h1>
        <p class="subtitle">高端水果销售管理系统</p>
      </div>

      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        size="large"
        class="login-form"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            prefix-icon="User"
            class="custom-input"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
            prefix-icon="Lock"
            show-password
            @keyup.enter="handleLogin"
            class="custom-input"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            class="login-btn"
            @click="handleLogin"
            round
          >
            立即登录
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <span class="footer-link">忘记密码？</span>
        <span class="divider">|</span>
        <span class="footer-link">联系管理员</span>
      </div>
    </div>

    <!-- Background Elements -->
    <div class="bg-circle circle-1"></div>
    <div class="bg-circle circle-2"></div>
  </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";

const router = useRouter();
const loginFormRef = ref(null);
const loading = ref(false);

const loginForm = reactive({
  username: "admin",
  password: "",
});

const loginRules = {
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
};

const handleLogin = async () => {
  if (!loginFormRef.value) return;

  await loginFormRef.value.validate((valid) => {
    if (valid) {
      loading.value = true;
      // Simulate login delay
      setTimeout(() => {
        loading.value = false;
        if (loginForm.username === "admin" && loginForm.password === "123456") {
          localStorage.setItem("isAuthenticated", "true");
          ElMessage.success("登录成功");
          router.push("/");
        } else {
          ElMessage.error("用户名或密码错误 (admin/123456)");
        }
      }, 1000);
    }
  });
};
</script>

<style scoped>
.login-container {
  height: 100vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #1a1c20 0%, #0f1012 100%);
  position: relative;
  overflow: hidden;
}

.bg-circle {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.4;
  z-index: 0;
}

.circle-1 {
  width: 400px;
  height: 400px;
  background: #d4af37;
  top: -100px;
  left: -100px;
}

.circle-2 {
  width: 500px;
  height: 500px;
  background: #2c3e50;
  bottom: -150px;
  right: -150px;
}

.login-card {
  width: 420px;
  padding: 50px 40px;
  border-radius: 24px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
  position: relative;
  z-index: 1;
  text-align: center;
  background: rgba(255, 255, 255, 0.95);
}

.login-header {
  margin-bottom: 40px;
}

.logo-icon {
  width: 64px;
  height: 64px;
  background: linear-gradient(135deg, #d4af37 0%, #aa8c2c 100%);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  margin: 0 auto 20px;
  box-shadow: 0 10px 20px rgba(212, 175, 55, 0.3);
}

.login-header h1 {
  margin: 0;
  font-size: 28px;
  color: #2c3e50;
  font-weight: 700;
  letter-spacing: 1px;
}

.subtitle {
  margin: 10px 0 0;
  color: #909399;
  font-size: 14px;
  letter-spacing: 2px;
  text-transform: uppercase;
}

.login-form {
  margin-bottom: 30px;
}

.custom-input :deep(.el-input__wrapper) {
  background-color: #f5f7fa;
  box-shadow: none !important;
  border: 1px solid transparent;
  transition: all 0.3s;
  border-radius: 12px;
  padding: 8px 15px;
}

.custom-input :deep(.el-input__wrapper:hover),
.custom-input :deep(.el-input__wrapper.is-focus) {
  background-color: #fff;
  border-color: #d4af37;
  box-shadow: 0 0 0 1px #d4af37 !important;
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 2px;
  background: linear-gradient(90deg, #d4af37 0%, #aa8c2c 100%);
  border: none;
  transition: all 0.3s;
  margin-top: 10px;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(212, 175, 55, 0.4);
}

.login-footer {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  font-size: 13px;
  color: #909399;
}

.footer-link {
  cursor: pointer;
  transition: color 0.3s;
}

.footer-link:hover {
  color: #d4af37;
}

.divider {
  color: #e4e7ed;
}
</style>
