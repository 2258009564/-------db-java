<template>
  <div class="sales-view">
    <el-card shadow="never" class="glass-panel">
      <div class="table-header">
        <h3>销售记录</h3>
        <el-button type="primary" icon="Plus" @click="handleAdd"
          >新增销售</el-button
        >
      </div>

      <el-table
        :data="tableData"
        v-loading="loading"
        style="width: 100%"
        class="custom-table"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="水果">
          <template #default="scope">
            {{ getFruitName(scope.row.fruitId) }}
          </template>
        </el-table-column>
        <el-table-column label="销售员">
          <template #default="scope">
            {{ getEmployeeName(scope.row.employeeId) }}
          </template>
        </el-table-column>
        <el-table-column label="客户">
          <template #default="scope">
            {{ getCustomerName(scope.row.customerId) }}
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" />
        <el-table-column label="总价">
          <template #default="scope">
            <span style="color: #67c23a; font-weight: bold"
              >¥{{ scope.row.totalPrice }}</span
            >
          </template>
        </el-table-column>
        <el-table-column label="日期">
          <template #default="scope">
            {{ formatDate(scope.row.saleDate) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="right">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)"
              >编辑</el-button
            >
            <el-button
              size="small"
              type="danger"
              @click="handleDelete(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑销售' : '新增销售'"
      width="500px"
    >
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
          label="销售员"
          prop="employeeId"
          :rules="[
            { required: true, message: '请选择销售员', trigger: 'change' },
          ]"
        >
          <el-select
            v-model="formData.employeeId"
            placeholder="请选择销售员"
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
          label="客户"
          prop="customerId"
          :rules="[
            { required: true, message: '请选择客户', trigger: 'change' },
          ]"
        >
          <el-select
            v-model="formData.customerId"
            placeholder="请选择客户"
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
import { ElMessage, ElMessageBox } from "element-plus";

const loading = ref(false);
const list = ref([]);
const fruits = ref([]);
const employees = ref([]);
const customers = ref([]);
const dialogVisible = ref(false);
const formData = ref({});
const formRef = ref(null);
const isEdit = ref(false);

const tableData = ref([]);

const fetchData = async () => {
  loading.value = true;
  try {
    const [salesRes, fruitRes, empRes, custRes] = await Promise.all([
      request.get("/sales"),
      request.get("/fruits"),
      request.get("/employees"),
      request.get("/customers"),
    ]);
    list.value = salesRes;
    fruits.value = fruitRes;
    employees.value = empRes;
    customers.value = custRes;
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

const getEmployeeName = (id) => {
  const e = employees.value.find((i) => i.id === id);
  return e ? e.name : id;
};

const getCustomerName = (id) => {
  const c = customers.value.find((i) => i.id === id);
  return c ? c.name : id;
};

const formatDate = (dateStr) => {
  if (!dateStr) return "";
  return new Date(dateStr).toLocaleString();
};

const handleAdd = () => {
  isEdit.value = false;
  formData.value = {};
  dialogVisible.value = true;
};

const handleEdit = (row) => {
  isEdit.value = true;
  formData.value = { ...row };
  dialogVisible.value = true;
};

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm("确定要删除这条销售记录吗？", "提示", {
      type: "warning",
    });
    await request.delete(`/sales/${row.id}`);
    ElMessage.success("删除成功");
    fetchData();
  } catch (e) {
    // cancel
  }
};

const submitForm = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(async (valid) => {
    if (valid) {
      if (isEdit.value) {
        await request.put("/sales", formData.value);
        ElMessage.success("更新成功");
      } else {
        await request.post("/sales", formData.value);
        ElMessage.success("销售成功");
      }
      dialogVisible.value = false;
      fetchData();
    }
  });
};

onMounted(() => {
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
