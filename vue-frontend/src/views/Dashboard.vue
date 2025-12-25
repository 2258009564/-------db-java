<template>
  <div class="dashboard-container">
    <!-- Top Stats Cards -->
    <el-row :gutter="24">
      <el-col :span="8">
        <div class="stat-card luxury-gold">
          <div class="stat-content">
            <div class="stat-icon-wrapper">
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
          <div class="card-bg-pattern"></div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card luxury-blue">
          <div class="stat-content">
            <div class="stat-icon-wrapper">
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
          <div class="card-bg-pattern"></div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card luxury-dark">
          <div class="stat-content">
            <div class="stat-icon-wrapper">
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
          <div class="card-bg-pattern"></div>
        </div>
      </el-col>
    </el-row>

    <!-- Charts Section -->
    <el-row :gutter="24" class="charts-row">
      <el-col :span="12">
        <el-card shadow="never" class="chart-card glass-panel">
          <template #header>
            <div class="card-header">
              <span class="header-title">水果销售占比</span>
              <el-tag
                size="small"
                effect="dark"
                color="#d4af37"
                style="border: none"
                >本月</el-tag
              >
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
        <el-card shadow="never" class="chart-card glass-panel">
          <template #header>
            <div class="card-header">
              <span class="header-title">员工销售业绩排行</span>
              <el-tag
                size="small"
                effect="dark"
                color="#2c3e50"
                style="border: none"
                >Top 10</el-tag
              >
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
  border-radius: 16px;
  color: white;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  overflow: hidden;
  position: relative;
  height: 140px;
  box-shadow: 0 10px 20px -10px rgba(0, 0, 0, 0.3);
  cursor: pointer;
}

.stat-card:hover {
  transform: translateY(-8px) scale(1.02);
  box-shadow: 0 20px 30px -10px rgba(0, 0, 0, 0.4);
}

.luxury-gold {
  background: linear-gradient(135deg, #d4af37 0%, #aa8c2c 100%);
}

.luxury-blue {
  background: linear-gradient(135deg, #2c3e50 0%, #4ca1af 100%);
}

.luxury-dark {
  background: linear-gradient(135deg, #232526 0%, #414345 100%);
}

.card-bg-pattern {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-image: radial-gradient(
    circle at 100% 0%,
    rgba(255, 255, 255, 0.1) 0%,
    transparent 20%
  );
  pointer-events: none;
}

.stat-content {
  display: flex;
  align-items: center;
  padding: 24px;
  position: relative;
  z-index: 1;
  height: 100%;
  box-sizing: border-box;
}

.stat-icon-wrapper {
  font-size: 32px;
  margin-right: 24px;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(5px);
  border-radius: 16px;
  width: 64px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
  margin-bottom: 8px;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-weight: 500;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 4px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.unit {
  font-size: 16px;
  font-weight: 500;
  opacity: 0.8;
}

.stat-trend {
  font-size: 13px;
  opacity: 0.85;
  display: flex;
  align-items: center;
  gap: 8px;
  background: rgba(0, 0, 0, 0.1);
  padding: 2px 8px;
  border-radius: 12px;
  width: fit-content;
}

.trend-up {
  color: #fff;
  font-weight: 600;
  display: flex;
  align-items: center;
}

.charts-row {
  margin-top: 30px;
}

.chart-card {
  border-radius: 16px;
  border: none;
  background: rgba(255, 255, 255, 0.8);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 5px 0;
}

.header-title {
  font-size: 18px;
  font-weight: 700;
  color: #2c3e50;
  position: relative;
  padding-left: 16px;
}

.header-title::before {
  content: "";
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 18px;
  background: #d4af37;
  border-radius: 2px;
}

.chart-wrapper {
  height: 320px;
  position: relative;
  padding: 10px;
}

.loading-placeholder {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
}
</style>
