<template>
  <BaseTable
    title="商品管理"
    entity="fruits"
    :columns="columns"
    :formItems="formItems"
    searchPlaceholder="输入商品名称搜索..."
    searchParam="name"
  />
</template>

<script setup>
import { ref, onMounted } from "vue";
import BaseTable from "@/components/BaseTable.vue";
import request from "@/api/request";

const suppliers = ref([]);
const supplierOptions = ref([]);

const columns = [
  { prop: "id", label: "ID", width: "80" },
  { prop: "name", label: "名称" },
  { prop: "price", label: "价格(元)", formatter: (row) => `¥${row.price}` },
  { prop: "stock", label: "库存" },
  {
    prop: "supplierId",
    label: "供应商",
    formatter: (row) => getSupplierName(row.supplierId),
  },
];

const formItems = ref([
  { prop: "name", label: "名称", type: "text" },
  { prop: "price", label: "价格", type: "number", step: 0.01 },
  { prop: "stock", label: "初始库存", type: "number" },
  { prop: "supplierId", label: "供应商", type: "select", options: [] },
]);

const getSupplierName = (id) => {
  const s = suppliers.value.find((s) => s.id === id);
  return s ? s.name : "未知";
};

onMounted(async () => {
  try {
    const res = await request.get("/suppliers");
    suppliers.value = res;
    supplierOptions.value = res.map((s) => ({ label: s.name, value: s.id }));
    // Update options in formItems
    formItems.value.find((item) => item.prop === "supplierId").options =
      supplierOptions.value;
  } catch (error) {
    console.error(error);
  }
});
</script>
