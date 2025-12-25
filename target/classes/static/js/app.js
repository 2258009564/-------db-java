const { createApp } = Vue;

createApp({
  data() {
    return {
      currentTab: "stats",
      tabs: [
        { id: "stats", name: "数据统计", icon: "bi-bar-chart-fill" },
        { id: "fruits", name: "水果库存", icon: "bi-box-seam" },
        { id: "employees", name: "员工管理", icon: "bi-people-fill" },
        { id: "customers", name: "客户管理", icon: "bi-person-badge-fill" },
        { id: "suppliers", name: "供应商", icon: "bi-truck" },
        { id: "stockin", name: "进货记录", icon: "bi-arrow-down-square-fill" },
        { id: "sales", name: "销售记录", icon: "bi-cart-check-fill" },
      ],
      // Data Lists
      fruits: [],
      employees: [],
      customers: [],
      suppliers: [],
      stockin: [],
      sales: [],
      stats: {
        fruitStats: {},
        employeeStats: {},
        customerStats: {},
      },

      // UI State
      searchQuery: "",
      modalTitle: "",
      formData: {},
      toastMessage: "",
      currentTime: new Date().toLocaleString(),

      // Chart Instances
      charts: {},
    };
  },
  computed: {
    filteredList() {
      const list = this[this.currentTab] || [];
      if (!this.searchQuery) return list;
      const query = this.searchQuery.toLowerCase();
      return list.filter((item) => {
        return Object.values(item).some((val) =>
          String(val).toLowerCase().includes(query)
        );
      });
    },
    totalRevenue() {
      // Calculate from sales or use stats if available
      // Assuming stats has a total or we sum it up
      // For now, let's sum up sales if available, or 0
      return this.sales
        .reduce((sum, sale) => sum + (sale.totalPrice || 0), 0)
        .toFixed(2);
    },
    totalQuantity() {
      return this.sales.reduce((sum, sale) => sum + (sale.quantity || 0), 0);
    },
    topEmployee() {
      // Simple logic to find top employee from stats
      const stats = this.stats.employeeStats || {};
      let max = 0;
      let name = "暂无";
      for (const [emp, val] of Object.entries(stats)) {
        if (val > max) {
          max = val;
          name = emp;
        }
      }
      return name;
    },
    currentFields() {
      const fields = {
        fruits: [
          { key: "name", label: "水果名称" },
          { key: "price", label: "单价", type: "number" },
          { key: "unit", label: "单位" },
          { key: "stock", label: "库存", type: "number" },
        ],
        employees: [
          { key: "name", label: "姓名" },
          { key: "phone", label: "电话" },
          { key: "position", label: "职位" },
        ],
        customers: [
          { key: "name", label: "姓名" },
          { key: "phone", label: "电话" },
          { key: "membershipId", label: "会员号" },
        ],
        suppliers: [
          { key: "name", label: "名称" },
          { key: "phone", label: "电话" },
          { key: "address", label: "地址" },
        ],
        stockin: [
          {
            key: "fruitId",
            label: "水果",
            type: "select",
            options: this.fruits,
          },
          { key: "quantity", label: "数量", type: "number" },
          { key: "cost", label: "进货价", type: "number" },
          {
            key: "supplierId",
            label: "供应商",
            type: "select",
            options: this.suppliers,
          },
        ],
        sales: [
          {
            key: "fruitId",
            label: "水果",
            type: "select",
            options: this.fruits,
          },
          {
            key: "employeeId",
            label: "销售员",
            type: "select",
            options: this.employees,
          },
          {
            key: "customerId",
            label: "客户",
            type: "select",
            options: this.customers,
          },
          { key: "quantity", label: "数量", type: "number" },
        ],
      };
      return fields[this.currentTab] || [];
    },
  },
  watch: {
    currentTab(newTab) {
      this.searchQuery = "";
      if (newTab === "stats") {
        this.$nextTick(() => {
          this.fetchStats();
        });
      } else {
        this.fetchData(newTab);
        // Pre-fetch options for selects
        if (newTab === "stockin" || newTab === "sales") {
          this.fetchData("fruits");
          if (newTab === "stockin") this.fetchData("suppliers");
          if (newTab === "sales") {
            this.fetchData("employees");
            this.fetchData("customers");
          }
        }
      }
    },
  },
  mounted() {
    this.fetchStats();
    this.fetchData("sales"); // Pre-load for dashboard numbers

    // Clock
    setInterval(() => {
      this.currentTime = new Date().toLocaleString();
    }, 1000);
  },
  methods: {
    getCurrentTabName() {
      const tab = this.tabs.find((t) => t.id === this.currentTab);
      return tab ? tab.name : "";
    },
    getColumns() {
      const cols = {
        fruits: [
          { key: "id", label: "ID" },
          { key: "name", label: "名称" },
          { key: "price", label: "单价", type: "price" },
          { key: "unit", label: "单位" },
          { key: "stock", label: "库存", type: "stock" },
        ],
        employees: [
          { key: "id", label: "ID" },
          { key: "name", label: "姓名" },
          { key: "phone", label: "电话" },
          { key: "position", label: "职位" },
        ],
        customers: [
          { key: "id", label: "ID" },
          { key: "name", label: "姓名" },
          { key: "phone", label: "电话" },
          { key: "membershipId", label: "会员号" },
        ],
        suppliers: [
          { key: "id", label: "ID" },
          { key: "name", label: "名称" },
          { key: "phone", label: "电话" },
          { key: "address", label: "地址" },
        ],
        stockin: [
          { key: "id", label: "ID" },
          {
            key: "fruitId",
            label: "水果",
            format: (val) => this.getName("fruits", val),
          },
          { key: "quantity", label: "数量" },
          { key: "cost", label: "成本", type: "price" },
          {
            key: "supplierId",
            label: "供应商",
            format: (val) => this.getName("suppliers", val),
          },
          {
            key: "stockInDate",
            label: "日期",
            format: (val) => new Date(val).toLocaleDateString(),
          },
        ],
        sales: [
          { key: "id", label: "ID" },
          {
            key: "fruitId",
            label: "水果",
            format: (val) => this.getName("fruits", val),
          },
          {
            key: "employeeId",
            label: "销售员",
            format: (val) => this.getName("employees", val),
          },
          {
            key: "customerId",
            label: "客户",
            format: (val) => this.getName("customers", val),
          },
          { key: "quantity", label: "数量" },
          { key: "totalPrice", label: "总价", type: "price" },
          {
            key: "saleDate",
            label: "日期",
            format: (val) => new Date(val).toLocaleDateString(),
          },
        ],
      };
      return cols[this.currentTab] || [];
    },
    getName(listName, id) {
      const list = this[listName] || [];
      const item = list.find((i) => i.id === id);
      return item ? item.name : "ID:" + id;
    },
    async fetchData(tab) {
      try {
        const response = await axios.get(`/api/${tab}`);
        this[tab] = response.data;
      } catch (error) {
        console.error(`Error fetching ${tab}:`, error);
        this.showToast("数据加载失败");
      }
    },
    async fetchStats() {
      try {
        const response = await axios.get("/api/stats");
        this.stats = response.data;
        this.initCharts();
      } catch (error) {
        console.error("Error fetching stats:", error);
      }
    },
    openModal(tab) {
      this.modalTitle = "新增 " + this.getCurrentTabName();
      this.formData = {};
      const modal = new bootstrap.Modal(document.getElementById("dataModal"));
      modal.show();
    },
    editItem(tab, item) {
      this.modalTitle = "编辑 " + this.getCurrentTabName();
      this.formData = { ...item };
      const modal = new bootstrap.Modal(document.getElementById("dataModal"));
      modal.show();
    },
    async saveData() {
      try {
        const tab = this.currentTab;
        const isEdit = !!this.formData.id;
        let response;

        // Handle specific logic for sales/stockin which might need ID mapping
        // Assuming backend handles ID mapping or we send objects
        // Simple implementation:

        if (isEdit) {
          response = await axios.put(
            `/api/${tab}/${this.formData.id}`,
            this.formData
          );
        } else {
          response = await axios.post(`/api/${tab}`, this.formData);
        }

        if (response.status === 200 || response.status === 201) {
          this.showToast("保存成功");
          bootstrap.Modal.getInstance(
            document.getElementById("dataModal")
          ).hide();
          this.fetchData(tab);
        }
      } catch (error) {
        console.error("Save error:", error);
        this.showToast("保存失败: " + error.message);
      }
    },
    async deleteItem(tab, id) {
      if (!confirm("确定要删除这条记录吗？")) return;
      try {
        await axios.delete(`/api/${tab}/${id}`);
        this.showToast("删除成功");
        this.fetchData(tab);
      } catch (error) {
        console.error("Delete error:", error);
        this.showToast("删除失败");
      }
    },
    showToast(msg) {
      this.toastMessage = msg;
      const toast = new bootstrap.Toast(document.getElementById("liveToast"));
      toast.show();
    },
    initCharts() {
      if (this.charts.fruit) this.charts.fruit.destroy();
      if (this.charts.employee) this.charts.employee.destroy();

      const fruitCtx = document.getElementById("fruitChart");
      const employeeCtx = document.getElementById("employeeChart");

      if (fruitCtx && this.stats.fruitStats) {
        this.charts.fruit = new Chart(fruitCtx, {
          type: "doughnut",
          data: {
            labels: Object.keys(this.stats.fruitStats),
            datasets: [
              {
                data: Object.values(this.stats.fruitStats),
                backgroundColor: [
                  "#4e73df",
                  "#1cc88a",
                  "#36b9cc",
                  "#f6c23e",
                  "#e74a3b",
                ],
              },
            ],
          },
        });
      }

      if (employeeCtx && this.stats.employeeStats) {
        this.charts.employee = new Chart(employeeCtx, {
          type: "bar",
          data: {
            labels: Object.keys(this.stats.employeeStats),
            datasets: [
              {
                label: "销售额",
                data: Object.values(this.stats.employeeStats),
                backgroundColor: "#4e73df",
              },
            ],
          },
          options: {
            scales: {
              y: { beginAtZero: true },
            },
          },
        });
      }
    },
  },
}).mount("#app");
