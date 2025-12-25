<template>
  <div class="sales-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span
                ><el-icon><ShoppingCart /></el-icon> 新销售单</span
              >
            </div>
          </template>
          <el-form :model="form" label-width="80px" ref="formRef">
            <el-form-item
              label="商品"
              prop="fruitId"
              :rules="[
                { required: true, message: '请选择商品', trigger: 'change' },
              ]"
            >
              <el-select
                v-model="form.fruitId"
                placeholder="请选择"
                filterable
                @change="handleFruitChange"
                style="width: 100%"
              >
                <el-option
                  v-for="item in fruits"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
              <div v-if="currentFruit" class="sale-info">
                单价: <span class="price">¥{{ currentFruit.price }}</span> |
                库存:
                <span :class="{ 'low-stock': currentFruit.stock < 10 }">{{
                  currentFruit.stock
                }}</span>
              </div>
            </el-form-item>
            <el-form-item
              label="客户"
              prop="customerId"
              :rules="[
                { required: true, message: '请选择客户', trigger: 'change' },
              ]"
            >
              <el-select
                v-model="form.customerId"
                placeholder="请选择"
                filterable
                style="width: 100%"
              >
                <el-option
                  v-for="item in customers"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item
              label="销售员"
              prop="employeeId"
              :rules="[
                { required: true, message: '请选择销售员', trigger: 'change' },
              ]"
            >
              <el-select
                v-model="form.employeeId"
                placeholder="请选择"
                filterable
                style="width: 100%"
              >
                <el-option
                  v-for="item in employees"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item
              label="数量"
              prop="quantity"
              :rules="[
                { required: true, message: '请输入数量', trigger: 'blur' },
              ]"
            >
              <el-input-number
                v-model="form.quantity"
                :min="1"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                @click="submitForm"
                :loading="loading"
                style="width: 100%"
                >提交订单</el-button
              >
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card>
          <template #header>
            <div class="card-header">
              <span
                ><el-icon><List /></el-icon> 销售记录</span
              >
            </div>
          </template>
          <el-table :data="salesHistory" stripe height="500">
            <el-table-column prop="id" label="ID" width="60" />
            <el-table-column label="商品" width="120">
              <template #default="scope">{{
                getFruitName(scope.row.fruitId)
              }}</template>
            </el-table-column>
            <el-table-column label="客户" width="100">
              <template #default="scope">{{
                getCustomerName(scope.row.customerId)
              }}</template>
            </el-table-column>
            <el-table-column label="员工" width="100">
              <template #default="scope">{{
                getEmployeeName(scope.row.employeeId)
              }}</template>
            </el-table-column>
            <el-table-column prop="quantity" label="数量" width="80" />
            <el-table-column label="总价" width="100">
              <template #default="scope">
                <span class="price">¥{{ scope.row.totalPrice }}</span>
              </template>
            </el-table-column>
            <el-table-column label="时间" width="180">
              <template #default="scope">{{
                new Date(scope.row.saleDate).toLocaleString()
              }}</template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from "vue";
import request from "@/api/request";
import { ElMessage } from "element-plus";

const form = reactive({
  fruitId: "",
  customerId: "",
  employeeId: "",
  quantity: 1,
});
const formRef = ref(null);
const fruits = ref([]);
const customers = ref([]);
const employees = ref([]);
const salesHistory = ref([]);
const currentFruit = ref(null);
const loading = ref(false);

const loadData = async () => {
  try {
    const [fruitsRes, customersRes, employeesRes, salesRes] = await Promise.all(
      [
        request.get("/fruits"),
        request.get("/customers"),
        request.get("/employees"),
        request.get("/sales"),
      ]
    );
    fruits.value = fruitsRes;
    customers.value = customersRes;
    employees.value = employeesRes;
    salesHistory.value = salesRes.sort(
      (a, b) => new Date(b.saleDate) - new Date(a.saleDate)
    );
  } catch (error) {
    console.error(error);
  }
};

const handleFruitChange = (val) => {
  currentFruit.value = fruits.value.find((f) => f.id === val);
};

const getFruitName = (id) => fruits.value.find((f) => f.id === id)?.name || id;
const getCustomerName = (id) =>
  customers.value.find((c) => c.id === id)?.name || id;
const getEmployeeName = (id) =>
  employees.value.find((e) => e.id === id)?.name || id;

const submitForm = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(async (valid) => {
    if (valid) {
      if (currentFruit.value && form.quantity > currentFruit.value.stock) {
        ElMessage.error(`库存不足，当前仅剩 ${currentFruit.value.stock}`);
        return;
      }

      loading.value = true;
      try {
        await request.post("/sales", form);
        ElMessage.success("销售录入成功");
        form.quantity = 1;
        // Refresh data
        loadData();
      } catch (error) {
        console.error(error);
      } finally {
        loading.value = false;
      }
    }
  });
};

onMounted(() => {
  loadData();
});
</script>

<style scoped>
.sale-info {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}
.price {
  color: #67c23a;
  font-weight: bold;
}
.low-stock {
  color: #f56c6c;
  font-weight: bold;
}
</style>
