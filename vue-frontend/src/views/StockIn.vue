<template>
  <el-card class="box-card" style="max-width: 800px; margin: 0 auto">
    <template #header>
      <div class="card-header">
        <span
          ><el-icon><Box /></el-icon> 商品入库</span
        >
      </div>
    </template>
    <el-form :model="form" label-width="100px" ref="formRef">
      <el-form-item
        label="选择商品"
        prop="fruitId"
        :rules="[{ required: true, message: '请选择商品', trigger: 'change' }]"
      >
        <el-select
          v-model="form.fruitId"
          placeholder="请选择商品"
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
        <div v-if="currentFruit" class="stock-info">
          当前库存: {{ currentFruit.stock }} | 建议供应商:
          {{ getSupplierName(currentFruit.supplierId) }}
        </div>
      </el-form-item>
      <el-form-item
        label="入库数量"
        prop="quantity"
        :rules="[{ required: true, message: '请输入数量', trigger: 'blur' }]"
      >
        <el-input-number v-model="form.quantity" :min="1" style="width: 100%" />
      </el-form-item>
      <el-form-item
        label="供应商"
        prop="supplierId"
        :rules="[
          { required: true, message: '请选择供应商', trigger: 'change' },
        ]"
      >
        <el-select
          v-model="form.supplierId"
          placeholder="请选择供应商"
          filterable
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
      <el-form-item>
        <el-button type="primary" @click="submitForm" :loading="loading"
          >确认入库</el-button
        >
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { ref, onMounted, reactive } from "vue";
import request from "@/api/request";
import { ElMessage } from "element-plus";

const form = reactive({
  fruitId: "",
  quantity: 1,
  supplierId: "",
});
const formRef = ref(null);
const fruits = ref([]);
const suppliers = ref([]);
const currentFruit = ref(null);
const loading = ref(false);

const loadData = async () => {
  try {
    const [fruitsRes, suppliersRes] = await Promise.all([
      request.get("/fruits"),
      request.get("/suppliers"),
    ]);
    fruits.value = fruitsRes;
    suppliers.value = suppliersRes;
  } catch (error) {
    console.error(error);
  }
};

const handleFruitChange = (val) => {
  currentFruit.value = fruits.value.find((f) => f.id === val);
  if (currentFruit.value) {
    form.supplierId = currentFruit.value.supplierId;
  }
};

const getSupplierName = (id) => {
  const s = suppliers.value.find((s) => s.id === id);
  return s ? s.name : "无";
};

const submitForm = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true;
      try {
        await request.post("/stock-in", form);
        ElMessage.success("入库成功");
        form.fruitId = "";
        form.quantity = 1;
        form.supplierId = "";
        currentFruit.value = null;
        // Refresh fruits to get updated stock
        const fruitsRes = await request.get("/fruits");
        fruits.value = fruitsRes;
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
.stock-info {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}
</style>
