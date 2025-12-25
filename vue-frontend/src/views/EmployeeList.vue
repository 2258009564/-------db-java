<template>
  <BaseTable
    title="员工管理"
    entity="employees"
    :columns="columns"
    :formItems="formItems"
    searchPlaceholder="输入姓名搜索..."
    searchParam="name"
  />
</template>

<script setup>
import BaseTable from "@/components/BaseTable.vue";

const columns = [
  { prop: "id", label: "ID", width: "80" },
  { prop: "name", label: "姓名" },
  { prop: "phone", label: "电话" },
  { prop: "position", label: "职位" },
];

const formItems = [
  { prop: "name", label: "姓名", type: "text" },
  { prop: "phone", label: "电话", type: "text" },
  { prop: "position", label: "职位", type: "text" },
];
</script>
