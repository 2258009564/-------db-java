<template>
  <div class="dashboard-container">
    <!-- Top Stats Cards -->
    <el-row :gutter="24">
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card gradient-blue">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><Money /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">总销售额</div>
              <div class="stat-value">¥{{ totalRevenue }}</div>
              <div class="stat-trend">
                <span>较上月</span>
                <span class="trend-up"
                  >+12.5% <el-icon><Top /></el-icon
                ></span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card gradient-purple">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><Goods /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">总销量</div>
              <div class="stat-value">
                {{ totalQuantity }} <span class="unit">件</span>
              </div>
              <div class="stat-trend">
                <span>较上月</span>
                <span class="trend-up"
                  >+5.8% <el-icon><Top /></el-icon
                ></span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card gradient-orange">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><Trophy /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">销售冠军</div>
              <div class="stat-value">{{ topEmployee }}</div>
              <div class="stat-trend">
                <span>业绩占比</span>
                <span class="trend-neutral">18%</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Charts Section -->
    <el-row :gutter="24" class="charts-row">
      <el-col :span="12">
        <el-card shadow="always" class="chart-card">
          <template #header>
            <div class="card-header">
              <span class="header-title">水果销售占比</span>
              <el-tag size="small" effect="plain">本月</el-tag>
            </div>
          </template>
          <div class="chart-wrapper">
            <Doughnut
              v-if="loaded"
              :data="fruitChartData"
              :options="doughnutOptions"
            />
            <div v-else class="loading-placeholder">加载中...</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="always" class="chart-card">
          <template #header>
            <div class="card-header">
              <span class="header-title">员工销售业绩排行</span>
              <el-tag size="small" effect="plain">Top 10</el-tag>
            </div>
          </template>
          <div class="chart-wrapper">
            <Bar
              v-if="loaded"
              :data="employeeChartData"
              :options="barOptions"
            />
            <div v-else class="loading-placeholder">加载中...</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import request from "@/api/request";
import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
  Title,
} from "chart.js";
import { Doughnut, Bar } from "vue-chartjs";

ChartJS.register(
  ArcElement,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
  Title
);

const loaded = ref(false);
const fruitStats = ref([]);
const employeeStats = ref([]);

const totalRevenue = computed(() => {
  return fruitStats.value
    .reduce((sum, item) => sum + (item.totalRevenue || 0), 0)
    .toFixed(2);
});

const totalQuantity = computed(() => {
  return fruitStats.value.reduce(
    (sum, item) => sum + (item.totalQuantity || 0),
    0
  );
});

const topEmployee = computed(() => {
  if (employeeStats.value.length === 0) return "暂无";
  // Assuming backend returns sorted or we sort it
  const sorted = [...employeeStats.value].sort(
    (a, b) => b.totalRevenue - a.totalRevenue
  );
  return sorted[0]?.empName?.split(" ")[0] || "暂无";
});

const fruitChartData = computed(() => {
  const labels = fruitStats.value.map((item) => item.fruitName?.split(" ")[0]);
  const data = fruitStats.value.map((item) => item.totalRevenue);

  return {
    labels: labels,
    datasets: [
      {
        backgroundColor: [
          "#409EFF",
          "#67C23A",
          "#E6A23C",
          "#F56C6C",
          "#909399",
          "#95d475",
          "#79bbff",
        ],
        data: data,
      },
    ],
  };
});

const employeeChartData = computed(() => {
  const labels = employeeStats.value.map((item) => item.empName?.split(" ")[0]);
  const data = employeeStats.value.map((item) => item.totalRevenue);

  return {
    labels: labels,
    datasets: [
      {
        label: "销售额 (¥)",
        backgroundColor: "#409EFF",
        data: data,
        barThickness: 30,
        borderRadius: 4,
      },
    ],
  };
});

const doughnutOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: "right",
      labels: {
        usePointStyle: true,
        padding: 20,
        font: {
          family: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif",
          size: 12,
        },
      },
    },
  },
};

const barOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      display: false,
    },
  },
  scales: {
    y: {
      beginAtZero: true,
      grid: {
        color: "#f0f0f0",
      },
    },
    x: {
      grid: {
        display: false,
      },
    },
  },
};

onMounted(async () => {
  try {
    const [fruitRes, empRes] = await Promise.all([
      request.get("/sales/stats/fruit"),
      request.get("/sales/stats/employee"),
    ]);

    fruitStats.value = fruitRes;
    employeeStats.value = empRes;
    loaded.value = true;
  } catch (error) {
    console.error("Failed to load stats:", error);
  }
});
</script>

<style scoped>
.dashboard-container {
  padding: 10px;
}

.stat-card {
  border: none;
  border-radius: 12px;
  color: white;
  transition: transform 0.3s, box-shadow 0.3s;
  overflow: hidden;
  position: relative;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.15);
}

.gradient-blue {
  background: linear-gradient(135deg, #3a8ee6 0%, #005cbf 100%);
}

.gradient-purple {
  background: linear-gradient(135deg, #b37feb 0%, #722ed1 100%);
}

.gradient-orange {
  background: linear-gradient(135deg, #ffbf00 0%, #d46b08 100%);
}

.stat-content {
  display: flex;
  align-items: center;
  padding: 10px;
}

.stat-icon {
  font-size: 48px;
  opacity: 0.8;
  margin-right: 20px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 5px;
}

.unit {
  font-size: 14px;
  font-weight: normal;
}

.stat-trend {
  font-size: 12px;
  opacity: 0.8;
  display: flex;
  align-items: center;
  gap: 5px;
}

.trend-up {
  color: #e6f7ff;
  font-weight: bold;
  display: flex;
  align-items: center;
}

.charts-row {
  margin-top: 30px;
}

.chart-card {
  border-radius: 12px;
  border: 1px solid #ebeef5;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  border-left: 4px solid #409eff;
  padding-left: 10px;
}

.chart-wrapper {
  height: 300px;
  position: relative;
}

.loading-placeholder {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
}
</style>
