<template>
  <BaseTable
    title="供应商管理"
    entity="suppliers"
    :columns="columns"
    :formItems="formItems"
    searchPlaceholder="输入供应商名称搜索..."
    searchParam="name"
  />
</template>

<script setup>
import BaseTable from "@/components/BaseTable.vue";

const columns = [
  { prop: "id", label: "ID", width: "80" },
  { prop: "name", label: "名称" },
  { prop: "phone", label: "电话" },
  { prop: "address", label: "地址" },
];

const formItems = [
  { prop: "name", label: "名称", type: "text" },
  { prop: "phone", label: "电话", type: "text" },
  { prop: "address", label: "地址", type: "text" },
];
</script>
