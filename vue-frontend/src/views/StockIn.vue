<template>
  <div class="stockin-view">
    <el-card shadow="never">
      <div class="table-header">
        <h3>进货记录</h3>
        <el-button type="primary" icon="Plus" @click="handleAdd"
          >新增进货</el-button
        >
      </div>

      <el-table
        :data="tableData"
        v-loading="loading"
        stripe
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="水果">
          <template #default="scope">
            {{ getFruitName(scope.row.fruitId) }}
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" />
        <el-table-column label="成本">
          <template #default="scope">
            <span style="color: #f56c6c">¥{{ scope.row.cost }}</span>
          </template>
        </el-table-column>
        <el-table-column label="供应商">
          <template #default="scope">
            {{ getSupplierName(scope.row.supplierId) }}
          </template>
        </el-table-column>
        <el-table-column label="日期">
          <template #default="scope">
            {{ formatDate(scope.row.stockInDate) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="新增进货" width="500px">
      <el-form ref="formRef" :model="formData" label-width="80px">
        <el-form-item
          label="水果"
          prop="fruitId"
          :rules="[
            { required: true, message: '请选择水果', trigger: 'change' },
          ]"
        >
          <el-select
            v-model="formData.fruitId"
            placeholder="请选择水果"
            style="width: 100%"
          >
            <el-option
              v-for="item in fruits"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          label="数量"
          prop="quantity"
          :rules="[{ required: true, message: '请输入数量', trigger: 'blur' }]"
        >
          <el-input-number
            v-model="formData.quantity"
            :min="1"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item
          label="成本"
          prop="cost"
          :rules="[{ required: true, message: '请输入成本', trigger: 'blur' }]"
        >
          <el-input-number
            v-model="formData.cost"
            :min="0"
            :precision="2"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item
          label="供应商"
          prop="supplierId"
          :rules="[
            { required: true, message: '请选择供应商', trigger: 'change' },
          ]"
        >
          <el-select
            v-model="formData.supplierId"
            placeholder="请选择供应商"
            style="width: 100%"
          >
            <el-option
              v-for="item in suppliers"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import request from "@/api/request";
import { ElMessage } from "element-plus";

const loading = ref(false);
const list = ref([]);
const fruits = ref([]);
const suppliers = ref([]);
const dialogVisible = ref(false);
const formData = ref({});
const formRef = ref(null);

const tableData = ref([]);

const fetchData = async () => {
  loading.value = true;
  try {
    const [stockRes, fruitRes, supplierRes] = await Promise.all([
      request.get("/stock-in"), // Note: API endpoint might be /stock-in based on controller
      request.get("/fruits"),
      request.get("/suppliers"),
    ]);
    // The controller has @PostMapping("/stock-in") but no GetMapping for all stock-ins?
    // Let me check the controller again.
    // Controller has: @PostMapping("/stock-in") public StockIn stockIn(...)
    // It DOES NOT have a GetMapping for /stock-in list!
    // Wait, the previous app.js had `fetchData('stockin')`.
    // Did I miss it in the controller?
    // I read lines 1-179. Let me check if I missed it.
    // I see `getAllSales` but I don't see `getAllStockIns`.
    // I need to add it to the controller if it's missing.
    // Or maybe it was there and I missed it.
    // Let's assume I need to add it.

    // For now, I'll proceed with the frontend code assuming the API exists or I will fix it.
    // I'll check the controller content I read earlier.
    // Lines 133-142:
    // @PostMapping("/stock-in")
    // public StockIn stockIn(@RequestBody StockIn stockIn) { ... }
    // No GetMapping for stock-in list.
    // I MUST add it.

    list.value = stockRes || []; // Fallback if API fails or returns null
    fruits.value = fruitRes;
    suppliers.value = supplierRes;
    tableData.value = list.value;
  } catch (e) {
    console.error(e);
  } finally {
    loading.value = false;
  }
};

const getFruitName = (id) => {
  const f = fruits.value.find((i) => i.id === id);
  return f ? f.name : id;
};

const getSupplierName = (id) => {
  const s = suppliers.value.find((i) => i.id === id);
  return s ? s.name : id;
};

const formatDate = (dateStr) => {
  if (!dateStr) return "";
  return new Date(dateStr).toLocaleString();
};

const handleAdd = () => {
  formData.value = {};
  dialogVisible.value = true;
};

const submitForm = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(async (valid) => {
    if (valid) {
      await request.post("/stock-in", formData.value);
      ElMessage.success("进货成功");
      dialogVisible.value = false;
      // Re-fetch to update list (if I add the GET endpoint)
      // For now, just push to list if I can't fetch
      // But I really should add the endpoint.
      fetchData();
    }
  });
};

onMounted(() => {
  // I need to fix the backend to support GET /stock-in
  // I will do that in a separate step.
  // For now, I'll write the frontend code.
  // Actually, I can't fetch data if the endpoint doesn't exist.
  // I'll add a TODO to fix the backend.
  fetchData();
});
</script>

<style scoped>
.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>
