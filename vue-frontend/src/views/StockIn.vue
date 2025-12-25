<template>
  <div class="stockin-view">
    <el-card shadow="never" class="glass-panel">
      <div class="table-header">
        <h3>进货记录</h3>
        <el-button type="primary" icon="Plus" @click="handleAdd"
          >新增进货</el-button
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
        <el-table-column prop="quantity" label="数量" />
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
      :title="isEdit ? '编辑进货' : '新增进货'"
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
import { ElMessage, ElMessageBox } from "element-plus";

const loading = ref(false);
const list = ref([]);
const fruits = ref([]);
const suppliers = ref([]);
const dialogVisible = ref(false);
const formData = ref({});
const formRef = ref(null);
const isEdit = ref(false);

const tableData = ref([]);

const fetchData = async () => {
  loading.value = true;
  try {
    const [stockRes, fruitRes, supplierRes] = await Promise.all([
      request.get("/stockin"),
      request.get("/fruits"),
      request.get("/suppliers"),
    ]);
    list.value = stockRes || [];
    fruits.value = fruitRes || [];
    suppliers.value = supplierRes || [];
    tableData.value = list.value;
  } catch (e) {
    console.error(e);
    ElMessage.error("获取数据失败");
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
  isEdit.value = false;
  formData.value = {
    stockInDate: new Date().toISOString(),
  };
  dialogVisible.value = true;
};

const handleEdit = (row) => {
  isEdit.value = true;
  formData.value = { ...row };
  dialogVisible.value = true;
};

const handleDelete = (row) => {
  ElMessageBox.confirm("确定要删除这条进货记录吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(async () => {
    try {
      await request.delete(`/stockin/${row.id}`);
      ElMessage.success("删除成功");
      fetchData();
    } catch (e) {
      console.error(e);
      ElMessage.error("删除失败");
    }
  });
};

const submitForm = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await request.put("/stockin", formData.value);
          ElMessage.success("修改成功");
        } else {
          await request.post("/stockin", formData.value);
          ElMessage.success("进货成功");
        }
        dialogVisible.value = false;
        fetchData();
      } catch (e) {
        console.error(e);
        ElMessage.error(isEdit.value ? "修改失败" : "进货失败");
      }
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
