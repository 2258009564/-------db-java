<template>
  <div class="login-container">
    <div class="login-content">
      <div class="login-left">
        <div class="login-info">
          <div class="logo">
            <el-icon :size="40"><Basket /></el-icon>
            <h1>FruitSys Pro</h1>
          </div>
          <p class="subtitle">新一代智能水果销售管理系统</p>
          <ul class="features">
            <li>
              <el-icon><DataLine /></el-icon> 实时数据分析
            </li>
            <li>
              <el-icon><Goods /></el-icon> 智能库存预警
            </li>
            <li>
              <el-icon><User /></el-icon> 全员绩效管理
            </li>
          </ul>
        </div>
        <div class="login-bg-circle"></div>
      </div>

      <div class="login-right">
        <div class="login-form-box">
          <h2>欢迎登录</h2>
          <p class="login-tip">请输入您的账号和密码</p>

          <el-form
            ref="loginFormRef"
            :model="loginForm"
            :rules="loginRules"
            size="large"
          >
            <el-form-item prop="username">
              <el-input
                v-model="loginForm.username"
                placeholder="用户名"
                prefix-icon="User"
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
              />
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                :loading="loading"
                class="login-btn"
                @click="handleLogin"
              >
                登 录
              </el-button>
            </el-form-item>
          </el-form>

          <div class="login-footer">
            <span>忘记密码？</span>
            <span>注册账号</span>
          </div>
        </div>
      </div>
    </div>
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
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  overflow: hidden;
}

.login-content {
  display: flex;
  width: 900px;
  height: 550px;
  background: #fff;
  border-radius: 20px;
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.login-left {
  flex: 1;
  background: linear-gradient(135deg, #304156 0%, #1f2d3d 100%);
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  color: #fff;
  position: relative;
  overflow: hidden;
}

.login-bg-circle {
  position: absolute;
  width: 300px;
  height: 300px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 50%;
  top: -50px;
  right: -50px;
}

.logo {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.logo h1 {
  margin: 0 0 0 15px;
  font-size: 32px;
  font-weight: bold;
}

.subtitle {
  font-size: 18px;
  opacity: 0.8;
  margin-bottom: 40px;
}

.features {
  list-style: none;
  padding: 0;
}

.features li {
  margin-bottom: 20px;
  font-size: 16px;
  display: flex;
  align-items: center;
}

.features li .el-icon {
  margin-right: 10px;
  background: rgba(255, 255, 255, 0.2);
  padding: 8px;
  border-radius: 8px;
}

.login-right {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px;
}

.login-form-box {
  width: 100%;
  max-width: 320px;
}

.login-form-box h2 {
  margin: 0 0 10px 0;
  font-size: 28px;
  color: #333;
}

.login-tip {
  color: #999;
  margin-bottom: 30px;
}

.login-btn {
  width: 100%;
  font-size: 16px;
  padding: 12px 0;
  border-radius: 8px;
  background: linear-gradient(90deg, #409eff 0%, #3a8ee6 100%);
  border: none;
  transition: all 0.3s;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(64, 158, 255, 0.3);
}

.login-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  font-size: 14px;
  color: #909399;
}

.login-footer span {
  cursor: pointer;
}

.login-footer span:hover {
  color: #409eff;
}
</style>
