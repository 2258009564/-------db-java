import { createRouter, createWebHistory } from "vue-router";
import MainLayout from "@/layout/MainLayout.vue";

const routes = [
  {
    path: "/",
    component: MainLayout,
    redirect: "/dashboard",
    children: [
      {
        path: "dashboard",
        name: "Dashboard",
        component: () => import("@/views/Dashboard.vue"),
        meta: { title: "首页" },
      },
      {
        path: "customers",
        name: "Customers",
        component: () => import("@/views/CustomerList.vue"),
        meta: { title: "客户管理" },
      },
      {
        path: "employees",
        name: "Employees",
        component: () => import("@/views/EmployeeList.vue"),
        meta: { title: "员工管理" },
      },
      {
        path: "suppliers",
        name: "Suppliers",
        component: () => import("@/views/SupplierList.vue"),
        meta: { title: "供应商管理" },
      },
      {
        path: "fruits",
        name: "Fruits",
        component: () => import("@/views/FruitList.vue"),
        meta: { title: "商品管理" },
      },
      {
        path: "stockin",
        name: "StockIn",
        component: () => import("@/views/StockIn.vue"),
        meta: { title: "入库管理" },
      },
      {
        path: "sales",
        name: "Sales",
        component: () => import("@/views/Sales.vue"),
        meta: { title: "销售管理" },
      },
      {
        path: "stats",
        name: "Statistics",
        component: () => import("@/views/Statistics.vue"),
        meta: { title: "销售统计" },
      },
    ],
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("@/views/Login.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
