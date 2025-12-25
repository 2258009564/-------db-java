<template>
  <BaseTable
    title="客户管理"
    entity="customers"
    :columns="columns"
    :formItems="formItems"
    searchPlaceholder="输入姓名或电话搜索..."
    searchParam="keyword"
  />
</template>

<script setup>
import BaseTable from "@/components/BaseTable.vue";

const columns = [
  { prop: "id", label: "ID", width: "80" },
  { prop: "name", label: "姓名" },
  { prop: "phone", label: "电话" },
  { prop: "address", label: "地址" },
];

const formItems = [
  { prop: "name", label: "姓名", type: "text" },
  { prop: "phone", label: "电话", type: "text" },
  { prop: "address", label: "地址", type: "text" },
];
</script>
