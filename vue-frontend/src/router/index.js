import { createRouter, createWebHashHistory } from "vue-router";
import MainLayout from "@/layout/MainLayout.vue";
import Login from "@/views/Login.vue";

const routes = [
  {
    path: "/login",
    name: "Login",
    component: Login,
  },
  {
    path: "/",
    component: MainLayout,
    redirect: "/dashboard",
    children: [
      {
        path: "dashboard",
        name: "Dashboard",
        component: () => import("@/views/Dashboard.vue"),
        meta: { title: "数据统计" },
      },
      {
        path: "fruits",
        name: "Fruits",
        component: () => import("@/components/BaseTable.vue"),
        props: { type: "fruits" },
        meta: { title: "水果库存管理" },
      },
      {
        path: "employees",
        name: "Employees",
        component: () => import("@/components/BaseTable.vue"),
        props: { type: "employees" },
        meta: { title: "员工管理" },
      },
      {
        path: "customers",
        name: "Customers",
        component: () => import("@/components/BaseTable.vue"),
        props: { type: "customers" },
        meta: { title: "客户管理" },
      },
      {
        path: "suppliers",
        name: "Suppliers",
        component: () => import("@/components/BaseTable.vue"),
        props: { type: "suppliers" },
        meta: { title: "供应商管理" },
      },
      {
        path: "stockin",
        name: "StockIn",
        component: () => import("@/views/StockIn.vue"),
        meta: { title: "进货记录" },
      },
      {
        path: "sales",
        name: "Sales",
        component: () => import("@/views/Sales.vue"),
        meta: { title: "销售记录" },
      },
    ],
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem("isAuthenticated");
  if (to.name !== "Login" && !isAuthenticated) {
    next({ name: "Login" });
  } else {
    next();
  }
});

export default router;
