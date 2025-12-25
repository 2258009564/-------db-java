<template>
  <div class="stats-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span
            ><el-icon><TrendCharts /></el-icon> 销售统计报表</span
          >
        </div>
      </template>

      <el-row :gutter="20" style="margin-bottom: 20px">
        <el-col :span="24">
          <canvas id="salesChart" height="100"></canvas>
        </el-col>
      </el-row>

      <el-table :data="tableData" stripe show-summary>
        <el-table-column prop="fruitName" label="商品名称" />
        <el-table-column prop="totalQuantity" label="总销售数量" sortable />
        <el-table-column prop="totalRevenue" label="总销售额 (元)" sortable>
          <template #default="scope">
            <span class="price">¥{{ scope.row.totalRevenue }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import request from "@/api/request";
import Chart from "chart.js/auto";

const tableData = ref([]);
let chartInstance = null;

const initChart = () => {
  const ctx = document.getElementById("salesChart").getContext("2d");
  const labels = tableData.value.map((item) => item.fruitName);
  const data = tableData.value.map((item) => item.totalRevenue);

  if (chartInstance) {
    chartInstance.destroy();
  }

  chartInstance = new Chart(ctx, {
    type: "bar",
    data: {
      labels: labels,
      datasets: [
        {
          label: "销售额 (元)",
          data: data,
          backgroundColor: "rgba(64, 158, 255, 0.5)",
          borderColor: "rgba(64, 158, 255, 1)",
          borderWidth: 1,
        },
      ],
    },
    options: {
      responsive: true,
      scales: {
        y: {
          beginAtZero: true,
        },
      },
    },
  });
};

onMounted(async () => {
  try {
    const res = await request.get("/stats");
    tableData.value = res;
    initChart();
  } catch (error) {
    console.error(error);
  }
});
</script>

<style scoped>
.price {
  color: #67c23a;
  font-weight: bold;
}
</style>
