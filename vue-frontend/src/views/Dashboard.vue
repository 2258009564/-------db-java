<template>
  <div class="dashboard-container">
    <div class="welcome-section">
      <h1>
        <el-icon><Shop /></el-icon> 水果销售管理系统
      </h1>
      <p>欢迎回来！请从左侧菜单选择您要进行的操作。</p>
    </div>

    <el-row :gutter="40" class="action-cards">
      <el-col :span="8">
        <el-card
          shadow="hover"
          class="action-card"
          @click="$router.push('/sales')"
        >
          <el-icon class="card-icon success"><ShoppingCart /></el-icon>
          <h3>快速销售</h3>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card
          shadow="hover"
          class="action-card"
          @click="$router.push('/stockin')"
        >
          <el-icon class="card-icon warning"><Box /></el-icon>
          <h3>入库管理</h3>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card
          shadow="hover"
          class="action-card"
          @click="$router.push('/stats')"
        >
          <el-icon class="card-icon info"><TrendCharts /></el-icon>
          <h3>销售统计</h3>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.dashboard-container {
  text-align: center;
  padding: 40px;
}

.welcome-section h1 {
  font-size: 2.5rem;
  color: #409eff;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 15px;
}

.welcome-section p {
  font-size: 1.2rem;
  color: #909399;
  margin-bottom: 60px;
}

.action-card {
  cursor: pointer;
  transition: transform 0.3s;
  padding: 20px;
}

.action-card:hover {
  transform: translateY(-5px);
}

.card-icon {
  font-size: 4rem;
  margin-bottom: 20px;
}

.card-icon.success {
  color: #67c23a;
}
.card-icon.warning {
  color: #e6a23c;
}
.card-icon.info {
  color: #909399;
}
</style>
