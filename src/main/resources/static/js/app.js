const API_BASE = "/api";

// Global State Cache for ID->Name mapping
const state = {
  suppliers: {},
  fruits: {},
  customers: {},
  employees: {},
};

// --- Initialization ---
$(document).ready(function () {
  // Initial Load
  loadPage("home");

  // Pre-fetch metadata for mappings
  refreshMetadata();

  // Global AJAX Setup for Loading & Errors
  $.ajaxSetup({
    beforeSend: function () {
      $("#loading-overlay").css("display", "flex");
    },
    complete: function () {
      $("#loading-overlay").fadeOut(200);
    },
    error: function (xhr, status, error) {
      Swal.fire({
        icon: "error",
        title: "操作失败",
        text: xhr.responseJSON?.message || "服务器连接异常，请稍后重试",
        confirmButtonColor: "#4e73df",
      });
    },
  });
});

// --- Metadata Management ---
function refreshMetadata() {
  $.get(`${API_BASE}/suppliers`, (data) =>
    data.forEach((s) => (state.suppliers[s.id] = s))
  );
  $.get(`${API_BASE}/fruits`, (data) =>
    data.forEach((f) => (state.fruits[f.id] = f))
  );
  $.get(`${API_BASE}/customers`, (data) =>
    data.forEach((c) => (state.customers[c.id] = c))
  );
  $.get(`${API_BASE}/employees`, (data) =>
    data.forEach((e) => (state.employees[e.id] = e))
  );
}

// --- Navigation ---
function loadPage(page) {
  $(".nav-link").removeClass("active");
  $(`a[data-page="${page}"]`).addClass("active");

  const contentDiv = $("#main-content");
  contentDiv.hide().removeClass("fade-in");

  let html = "";
  switch (page) {
    case "home":
      html = renderHome();
      break;
    case "customer":
      html = renderCustomerPage();
      break;
    case "employee":
      html = renderEmployeePage();
      break;
    case "supplier":
      html = renderSupplierPage();
      break;
    case "fruit":
      html = renderFruitPage();
      break;
    case "stock":
      html = renderStockPage();
      break;
    case "sales":
      html = renderSalesPage();
      break;
    case "stats":
      html = renderStatsPage();
      break;
  }

  contentDiv.html(html).fadeIn(300).addClass("fade-in");

  // Post-render actions
  if (page !== "home") loadData(page);
}

// --- Render Functions ---

function renderHome() {
  return `
        <div class="text-center py-5">
            <h1 class="display-4 text-primary mb-4"><i class="bi bi-shop"></i> 水果销售管理系统</h1>
            <p class="lead text-secondary">欢迎回来！请从左侧菜单选择您要进行的操作。</p>
            <div class="row mt-5">
                <div class="col-md-4">
                    <div class="card p-4 text-center" onclick="loadPage('sales')" style="cursor:pointer">
                        <i class="bi bi-cart-check display-4 text-success mb-3"></i>
                        <h5>快速销售</h5>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card p-4 text-center" onclick="loadPage('stock')" style="cursor:pointer">
                        <i class="bi bi-box-seam display-4 text-warning mb-3"></i>
                        <h5>入库管理</h5>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card p-4 text-center" onclick="loadPage('stats')" style="cursor:pointer">
                        <i class="bi bi-graph-up display-4 text-info mb-3"></i>
                        <h5>销售统计</h5>
                    </div>
                </div>
            </div>
        </div>
    `;
}

function renderCustomerPage() {
  return `
        <div class="card">
            <div class="card-header">
                <span><i class="bi bi-people-fill"></i> 客户管理</span>
                <button class="btn btn-primary btn-sm" onclick="showModal('addCustomerModal')"><i class="bi bi-plus-lg"></i> 新增客户</button>
            </div>
            <div class="card-body">
                <div class="input-group mb-3 w-50">
                    <input type="text" id="searchCustomer" class="form-control" placeholder="输入姓名或电话搜索...">
                    <button class="btn btn-outline-primary" onclick="searchData('customer')"><i class="bi bi-search"></i> 查询</button>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead><tr><th>ID</th><th>姓名</th><th>电话</th><th>地址</th><th>操作</th></tr></thead>
                        <tbody id="customerTableBody"></tbody>
                    </table>
                </div>
            </div>
        </div>
        ${createModal(
          "addCustomerModal",
          "添加客户",
          `
            <div class="mb-3"><label>姓名</label><input type="text" id="custName" class="form-control" required></div>
            <div class="mb-3"><label>电话</label><input type="text" id="custPhone" class="form-control" required></div>
            <div class="mb-3"><label>地址</label><input type="text" id="custAddress" class="form-control"></div>
        `,
          "saveCustomer()"
        )}
    `;
}

function renderEmployeePage() {
  return `
        <div class="card">
            <div class="card-header">
                <span><i class="bi bi-person-badge"></i> 员工管理</span>
                <button class="btn btn-primary btn-sm" onclick="showModal('addEmployeeModal')"><i class="bi bi-plus-lg"></i> 新增员工</button>
            </div>
            <div class="card-body">
                <div class="input-group mb-3 w-50">
                    <input type="text" id="searchEmployee" class="form-control" placeholder="输入姓名搜索...">
                    <button class="btn btn-outline-primary" onclick="searchData('employee')"><i class="bi bi-search"></i> 查询</button>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead><tr><th>ID</th><th>姓名</th><th>电话</th><th>职位</th><th>操作</th></tr></thead>
                        <tbody id="employeeTableBody"></tbody>
                    </table>
                </div>
            </div>
        </div>
        ${createModal(
          "addEmployeeModal",
          "添加员工",
          `
            <div class="mb-3"><label>姓名</label><input type="text" id="empName" class="form-control" required></div>
            <div class="mb-3"><label>电话</label><input type="text" id="empPhone" class="form-control" required></div>
            <div class="mb-3"><label>职位</label><input type="text" id="empPosition" class="form-control"></div>
        `,
          "saveEmployee()"
        )}
    `;
}

function renderSupplierPage() {
  return `
        <div class="card">
            <div class="card-header">
                <span><i class="bi bi-truck"></i> 供应商管理</span>
                <button class="btn btn-primary btn-sm" onclick="showModal('addSupplierModal')"><i class="bi bi-plus-lg"></i> 新增供应商</button>
            </div>
            <div class="card-body">
                <div class="input-group mb-3 w-50">
                    <input type="text" id="searchSupplier" class="form-control" placeholder="输入供应商名称搜索...">
                    <button class="btn btn-outline-primary" onclick="searchData('supplier')"><i class="bi bi-search"></i> 查询</button>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead><tr><th>ID</th><th>名称</th><th>电话</th><th>地址</th><th>操作</th></tr></thead>
                        <tbody id="supplierTableBody"></tbody>
                    </table>
                </div>
            </div>
        </div>
        ${createModal(
          "addSupplierModal",
          "添加供应商",
          `
            <div class="mb-3"><label>名称</label><input type="text" id="supName" class="form-control" required></div>
            <div class="mb-3"><label>电话</label><input type="text" id="supPhone" class="form-control" required></div>
            <div class="mb-3"><label>地址</label><input type="text" id="supAddress" class="form-control"></div>
        `,
          "saveSupplier()"
        )}
    `;
}

function renderFruitPage() {
  return `
        <div class="card">
            <div class="card-header">
                <span><i class="bi bi-basket"></i> 商品管理</span>
                <button class="btn btn-primary btn-sm" onclick="showModal('addFruitModal')"><i class="bi bi-plus-lg"></i> 新增商品</button>
            </div>
            <div class="card-body">
                <div class="input-group mb-3 w-50">
                    <input type="text" id="searchFruit" class="form-control" placeholder="输入商品名称搜索...">
                    <button class="btn btn-outline-primary" onclick="searchData('fruit')"><i class="bi bi-search"></i> 查询</button>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead><tr><th>ID</th><th>名称</th><th>价格(元)</th><th>库存</th><th>供应商</th><th>操作</th></tr></thead>
                        <tbody id="fruitTableBody"></tbody>
                    </table>
                </div>
            </div>
        </div>
        ${createModal(
          "addFruitModal",
          "添加商品",
          `
            <div class="mb-3"><label>名称</label><input type="text" id="fruitName" class="form-control" required></div>
            <div class="mb-3"><label>价格</label><input type="number" step="0.01" id="fruitPrice" class="form-control" required></div>
            <div class="mb-3"><label>初始库存</label><input type="number" id="fruitStock" class="form-control" value="0"></div>
            <div class="mb-3"><label>供应商</label><select id="fruitSupplierId" class="form-select"></select></div>
        `,
          "saveFruit()"
        )}
    `;
}

function renderStockPage() {
  return `
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header"><i class="bi bi-box-arrow-in-down"></i> 商品入库</div>
                    <div class="card-body">
                        <form onsubmit="event.preventDefault(); submitStockIn();">
                            <div class="mb-3">
                                <label class="form-label">选择商品</label>
                                <select id="stockFruitId" class="form-select" required onchange="updateStockInfo(this.value)"></select>
                                <small class="text-muted" id="currentStockInfo"></small>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">入库数量</label>
                                <input type="number" id="stockQuantity" class="form-control" min="1" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">供应商</label>
                                <select id="stockSupplierId" class="form-select" required></select>
                            </div>
                            <div class="d-grid">
                                <button type="submit" class="btn btn-success btn-lg">确认入库</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    `;
}

function renderSalesPage() {
  return `
        <div class="row">
            <div class="col-md-4">
                <div class="card">
                    <div class="card-header bg-primary text-white"><i class="bi bi-cart-plus"></i> 新销售单</div>
                    <div class="card-body">
                        <form onsubmit="event.preventDefault(); submitSale();">
                            <div class="mb-3">
                                <label class="form-label">选择商品</label>
                                <select id="saleFruitId" class="form-select" required onchange="updateSaleInfo(this.value)"></select>
                                <div id="saleInfo" class="form-text text-info"></div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">客户</label>
                                <select id="saleCustomerId" class="form-select" required></select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">销售员</label>
                                <select id="saleEmployeeId" class="form-select" required></select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">数量</label>
                                <input type="number" id="saleQuantity" class="form-control" min="1" required>
                            </div>
                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary">提交订单</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header"><i class="bi bi-clock-history"></i> 销售记录</div>
                    <div class="card-body">
                        <div class="table-responsive" style="max-height: 600px; overflow-y: auto;">
                            <table class="table table-sm table-hover">
                                <thead><tr><th>ID</th><th>商品</th><th>客户</th><th>员工</th><th>数量</th><th>总价</th><th>时间</th></tr></thead>
                                <tbody id="salesTableBody"></tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;
}

function renderStatsPage() {
  return `
        <div class="card">
            <div class="card-header"><i class="bi bi-bar-chart-fill"></i> 销售统计报表</div>
            <div class="card-body">
                <table class="table table-bordered table-striped">
                    <thead class="table-dark"><tr><th>商品名称</th><th>总销售数量</th><th>总销售额 (元)</th></tr></thead>
                    <tbody id="statsTableBody"></tbody>
                </table>
            </div>
        </div>
    `;
}

// --- Helper: Modal Creator ---
function createModal(id, title, body, action) {
  return `
        <div class="modal fade" id="${id}" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">${title}</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">${body}</div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                        <button type="button" class="btn btn-primary" onclick="${action}">保存</button>
                    </div>
                </div>
            </div>
        </div>
    `;
}

function showModal(id) {
  // Refresh dropdowns if needed
  if (id === "addFruitModal") {
    populateSelect("fruitSupplierId", state.suppliers);
  }
  new bootstrap.Modal(document.getElementById(id)).show();
}

// --- Data Loading & Actions ---

function loadData(type) {
  if (type === "customer") {
    $.get(`${API_BASE}/customers`, (data) => {
      renderTable(
        "customerTableBody",
        data,
        (item) => `
                <td>${item.id}</td><td>${item.name}</td><td>${
          item.phone
        }</td><td>${item.address || "-"}</td>
                <td><button class="btn btn-danger btn-circle btn-sm" onclick="deleteItem('customers', ${
                  item.id
                }, 'customer')"><i class="bi bi-trash"></i></button></td>
            `
      );
    });
  } else if (type === "employee") {
    $.get(`${API_BASE}/employees`, (data) => {
      renderTable(
        "employeeTableBody",
        data,
        (item) => `
                <td>${item.id}</td><td>${item.name}</td><td>${
          item.phone
        }</td><td>${item.position || "-"}</td>
                <td><button class="btn btn-danger btn-circle btn-sm" onclick="deleteItem('employees', ${
                  item.id
                }, 'employee')"><i class="bi bi-trash"></i></button></td>
            `
      );
    });
  } else if (type === "supplier") {
    $.get(`${API_BASE}/suppliers`, (data) => {
      renderTable(
        "supplierTableBody",
        data,
        (item) => `
                <td>${item.id}</td><td>${item.name}</td><td>${
          item.phone
        }</td><td>${item.address || "-"}</td>
                <td><button class="btn btn-danger btn-circle btn-sm" onclick="deleteItem('suppliers', ${
                  item.id
                }, 'supplier')"><i class="bi bi-trash"></i></button></td>
            `
      );
    });
  } else if (type === "fruit") {
    $.get(`${API_BASE}/fruits`, (data) => {
      renderTable(
        "fruitTableBody",
        data,
        (item) => `
                <td>${item.id}</td><td>${
          item.name
        }</td><td class="text-success fw-bold">¥${item.price}</td>
                <td><span class="badge bg-${
                  item.stock < 10 ? "danger" : "success"
                }">${item.stock}</span></td>
                <td>${state.suppliers[item.supplierId]?.name || "未知"}</td>
                <td><button class="btn btn-danger btn-circle btn-sm" onclick="deleteItem('fruits', ${
                  item.id
                }, 'fruit')"><i class="bi bi-trash"></i></button></td>
            `
      );
    });
  } else if (type === "stock") {
    refreshMetadata();
    $.get(`${API_BASE}/fruits`, (data) => populateSelect("stockFruitId", data));
    $.get(`${API_BASE}/suppliers`, (data) =>
      populateSelect("stockSupplierId", data)
    );
  } else if (type === "sales") {
    refreshMetadata();
    $.get(`${API_BASE}/fruits`, (data) => populateSelect("saleFruitId", data));
    $.get(`${API_BASE}/customers`, (data) =>
      populateSelect("saleCustomerId", data)
    );
    $.get(`${API_BASE}/employees`, (data) =>
      populateSelect("saleEmployeeId", data)
    );

    $.get(`${API_BASE}/sales`, (data) => {
      // Sort by date desc
      data.sort((a, b) => new Date(b.saleDate) - new Date(a.saleDate));
      renderTable(
        "salesTableBody",
        data,
        (item) => `
                <td>${item.id}</td>
                <td>${state.fruits[item.fruitId]?.name || item.fruitId}</td>
                <td>${
                  state.customers[item.customerId]?.name || item.customerId
                }</td>
                <td>${
                  state.employees[item.employeeId]?.name || item.employeeId
                }</td>
                <td>${item.quantity}</td>
                <td class="text-primary fw-bold">¥${item.totalPrice}</td>
                <td>${new Date(item.saleDate).toLocaleString()}</td>
            `
      );
    });
  } else if (type === "stats") {
    $.get(`${API_BASE}/stats`, (data) => {
      renderTable(
        "statsTableBody",
        data,
        (item) => `
                <td>${item.fruitName}</td><td>${item.totalQuantity}</td><td class="text-success fw-bold">¥${item.totalRevenue}</td>
            `
      );
    });
  }
}

function renderTable(elementId, data, rowMapper) {
  const tbody = $(`#${elementId}`);
  tbody.empty();
  if (!data || data.length === 0) {
    tbody.html(
      '<tr><td colspan="10" class="text-center text-muted py-4">暂无数据</td></tr>'
    );
    return;
  }
  tbody.html(data.map((item) => `<tr>${rowMapper(item)}</tr>`).join(""));
}

function populateSelect(elementId, data) {
  const select = $(`#${elementId}`);
  select.empty();
  select.append('<option value="">请选择...</option>');

  // Handle both array and object (map)
  const items = Array.isArray(data) ? data : Object.values(data);

  items.forEach((item) => {
    select.append(`<option value="${item.id}">${item.name}</option>`);
  });
}

// --- Actions ---

function saveCustomer() {
  const data = {
    name: $("#custName").val(),
    phone: $("#custPhone").val(),
    address: $("#custAddress").val(),
  };
  if (!data.name || !data.phone)
    return Swal.fire("提示", "请填写必填项", "warning");
  postData("/customers", data, "customer", "addCustomerModal");
}

function saveEmployee() {
  const data = {
    name: $("#empName").val(),
    phone: $("#empPhone").val(),
    position: $("#empPosition").val(),
  };
  if (!data.name || !data.phone)
    return Swal.fire("提示", "请填写必填项", "warning");
  postData("/employees", data, "employee", "addEmployeeModal");
}

function saveSupplier() {
  const data = {
    name: $("#supName").val(),
    phone: $("#supPhone").val(),
    address: $("#supAddress").val(),
  };
  if (!data.name || !data.phone)
    return Swal.fire("提示", "请填写必填项", "warning");
  postData("/suppliers", data, "supplier", "addSupplierModal");
}

function saveFruit() {
  const data = {
    name: $("#fruitName").val(),
    price: $("#fruitPrice").val(),
    stock: $("#fruitStock").val(),
    supplierId: $("#fruitSupplierId").val(),
  };
  if (!data.name || !data.price)
    return Swal.fire("提示", "请填写必填项", "warning");
  postData("/fruits", data, "fruit", "addFruitModal");
}

function submitStockIn() {
  const data = {
    fruitId: $("#stockFruitId").val(),
    quantity: $("#stockQuantity").val(),
    supplierId: $("#stockSupplierId").val(),
  };
  if (!data.fruitId || !data.quantity)
    return Swal.fire("提示", "请填写完整信息", "warning");

  $.ajax({
    url: `${API_BASE}/stock-in`,
    type: "POST",
    contentType: "application/json",
    data: JSON.stringify(data),
    success: function () {
      Swal.fire("成功", "入库成功", "success").then(() => {
        $("#stockQuantity").val("");
        refreshMetadata(); // Update stock in cache
      });
    },
  });
}

function submitSale() {
  const data = {
    fruitId: $("#saleFruitId").val(),
    customerId: $("#saleCustomerId").val(),
    employeeId: $("#saleEmployeeId").val(),
    quantity: $("#saleQuantity").val(),
  };

  if (!data.fruitId || !data.quantity || !data.customerId)
    return Swal.fire("提示", "请填写完整信息", "warning");

  // Client side stock check
  const fruit = state.fruits[data.fruitId];
  if (fruit && parseInt(data.quantity) > fruit.stock) {
    return Swal.fire("库存不足", `当前库存仅剩 ${fruit.stock}`, "error");
  }

  $.ajax({
    url: `${API_BASE}/sales`,
    type: "POST",
    contentType: "application/json",
    data: JSON.stringify(data),
    success: function () {
      Swal.fire("成功", "销售录入成功", "success").then(() => {
        $("#saleQuantity").val("");
        loadData("sales");
        refreshMetadata(); // Update stock
      });
    },
  });
}

function deleteItem(endpoint, id, pageToReload) {
  Swal.fire({
    title: "确定删除?",
    text: "此操作不可恢复!",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#d33",
    cancelButtonColor: "#3085d6",
    confirmButtonText: "删除",
    cancelButtonText: "取消",
  }).then((result) => {
    if (result.isConfirmed) {
      $.ajax({
        url: `${API_BASE}/${endpoint}/${id}`,
        type: "DELETE",
        success: function () {
          Swal.fire("已删除", "数据已删除", "success");
          loadData(pageToReload);
          refreshMetadata();
        },
      });
    }
  });
}

function searchData(type) {
  const keyword = $(
    `#search${type.charAt(0).toUpperCase() + type.slice(1)}`
  ).val();
  const endpoint = type === "customer" ? "customers" : `${type}s`; // simple pluralization
  const param = type === "customer" ? "keyword" : "name";

  $.get(`${API_BASE}/${endpoint}/search?${param}=${keyword}`, function (data) {
    // Re-use render logic by clearing table and manually calling render
    // Ideally refactor renderTable to be more generic, but for now:
    if (type === "customer")
      renderTable(
        "customerTableBody",
        data,
        (item) =>
          `<td>${item.id}</td><td>${item.name}</td><td>${item.phone}</td><td>${item.address}</td><td><button class="btn btn-danger btn-circle btn-sm" onclick="deleteItem('customers', ${item.id}, 'customer')"><i class="bi bi-trash"></i></button></td>`
      );
    else if (type === "employee")
      renderTable(
        "employeeTableBody",
        data,
        (item) =>
          `<td>${item.id}</td><td>${item.name}</td><td>${item.phone}</td><td>${item.position}</td><td><button class="btn btn-danger btn-circle btn-sm" onclick="deleteItem('employees', ${item.id}, 'employee')"><i class="bi bi-trash"></i></button></td>`
      );
    else if (type === "supplier")
      renderTable(
        "supplierTableBody",
        data,
        (item) =>
          `<td>${item.id}</td><td>${item.name}</td><td>${item.phone}</td><td>${item.address}</td><td><button class="btn btn-danger btn-circle btn-sm" onclick="deleteItem('suppliers', ${item.id}, 'supplier')"><i class="bi bi-trash"></i></button></td>`
      );
    else if (type === "fruit")
      renderTable(
        "fruitTableBody",
        data,
        (item) =>
          `<td>${item.id}</td><td>${item.name}</td><td>${item.price}</td><td>${
            item.stock
          }</td><td>${
            state.suppliers[item.supplierId]?.name || "未知"
          }</td><td><button class="btn btn-danger btn-circle btn-sm" onclick="deleteItem('fruits', ${
            item.id
          }, 'fruit')"><i class="bi bi-trash"></i></button></td>`
      );
  });
}

// --- Generic Post Helper ---
function postData(endpoint, data, pageToReload, modalId) {
  $.ajax({
    url: `${API_BASE}${endpoint}`,
    type: "POST",
    contentType: "application/json",
    data: JSON.stringify(data),
    success: function () {
      Swal.fire("成功", "操作成功", "success");
      $(`#${modalId}`).modal("hide");
      // Clear inputs
      $(`#${modalId} input`).val("");
      loadData(pageToReload);
      refreshMetadata();
    },
  });
}

// --- UI Helpers ---
function updateStockInfo(fruitId) {
  const fruit = state.fruits[fruitId];
  if (fruit) {
    $("#currentStockInfo").text(
      `当前库存: ${fruit.stock} | 建议供应商: ${
        state.suppliers[fruit.supplierId]?.name || "无"
      }`
    );
    $("#stockSupplierId").val(fruit.supplierId); // Auto select supplier
  } else {
    $("#currentStockInfo").text("");
  }
}

function updateSaleInfo(fruitId) {
  const fruit = state.fruits[fruitId];
  if (fruit) {
    $("#saleInfo").html(
      `单价: <span class="fw-bold">¥${
        fruit.price
      }</span> | 库存: <span class="fw-bold ${
        fruit.stock < 10 ? "text-danger" : "text-success"
      }">${fruit.stock}</span>`
    );
  } else {
    $("#saleInfo").text("");
  }
}
