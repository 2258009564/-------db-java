哈尔滨理工大学
计算机科学与技术学院

《DB-Java》
课程设计报告

题 目 ：水果销售管理系统设计与实现
班 级 ：软件 24- 班
专 业 ：软件工程
姓名
（学 号） ：姓名（学号）、姓名（学号）、
：姓名（学号）、姓名（学号）、
指导教师 ：张三
日期 ：2026 年 1 月 9 日

目录
一、需求分析（三号，宋体，粗体） 2
二、系统设计（三号，宋体，粗体） 2
（一）系统中的数据定义（四号，黑体，粗体） 2
（二）系统的概要设计 2
（三）系统的详细设计 2
（四）数据库设计 2
（五）系统的核心算法 2
三、系统编码及运行（三号，宋体，粗体） 3
（一）系统开发涉及的软件 3
（二）系统运行界面及结果 3
四、系统测试（三号，宋体，粗体） 3
五、总结 3
附录（源代码） 4
（注：这部分可以点击右键更新生成，重新设置字体为四号）

一、需求分析（三号，宋体，粗体）
正文用小四字体，行间距为 1.5 倍。
随着信息技术的飞速发展，传统的手工记账方式已无法满足现代水果店高效、精准的管理需求。手工记录不仅效率低下，而且容易出错，难以实时掌握库存动态和销售情况。
本系统旨在开发一套基于 Web 的水果销售管理系统，通过数字化手段实现对水果进销存的全流程管理。
主要功能包括：

1.  **基础信息管理**：对水果商品、供应商、客户、员工等基础数据进行增删改查。
2.  **进货管理**：记录水果入库信息，自动增加库存。
3.  **销售管理**：记录销售交易，自动扣减库存并计算总价。
4.  **统计分析**：提供销售报表，按水果种类、员工绩效等维度进行数据可视化展示。

二、系统设计（三号，宋体，粗体）
（一）系统中的数据定义（四号，黑体，粗体）
本系统共包含 6 个实体集，详细定义如下：

**1. 实体集：fruit (水果商品)**
用于存储水果的基本信息，是系统的核心数据。

- `id` (Long): 商品唯一标识，主键，自增。
- `name` (String): 水果名称，如“红富士苹果”。
- `price` (Double): 销售单价，单位：元。
- `stock` (Integer): 当前库存数量，进货增加，销售减少。
- `supplierId` (Long): 关联供应商 ID，外键。

**2. 实体集：customer (客户)**
用于存储购买水果的客户信息，便于进行客户关系管理。

- `id` (Long): 客户唯一标识，主键，自增。
- `name` (String): 客户姓名。
- `phone` (String): 联系电话。
- `address` (String): 联系地址。

**3. 实体集：employee (员工)**
用于存储店铺员工信息，用于销售记录的经手人关联。

- `id` (Long): 员工唯一标识，主键，自增。
- `name` (String): 员工姓名。
- `phone` (String): 联系电话。
- `position` (String): 职位，如“店长”、“销售员”。

**4. 实体集：supplier (供应商)**
用于存储水果供货商信息，用于入库记录的来源关联。

- `id` (Long): 供应商唯一标识，主键，自增。
- `name` (String): 供应商名称。
- `phone` (String): 联系电话。
- `address` (String): 供应商地址。

**5. 联系集：sales (销售记录)**
记录每一次销售交易的详细信息，关联水果、客户和员工。

- `id` (Long): 销售记录 ID，主键，自增。
- `fruitId` (Long): 销售的水果 ID。
- `customerId` (Long): 购买客户 ID。
- `employeeId` (Long): 经手员工 ID。
- `quantity` (Integer): 销售数量。
- `totalPrice` (Double): 本次交易总金额。
- `saleDate` (LocalDateTime): 销售时间，自动生成。

**6. 联系集：stock_in (入库记录)**
记录每一次进货交易的详细信息，关联水果和供应商。

- `id` (Long): 入库记录 ID，主键，自增。
- `fruitId` (Long): 入库的水果 ID。
- `supplierId` (Long): 供货商 ID。
- `quantity` (Integer): 入库数量。
- `stockInDate` (LocalDateTime): 入库时间，自动生成。

（二）系统的概要设计
系统采用 B/S (Browser/Server) 架构，前后端分离设计。

1.  **前端层**：使用 Vue.js 3 + Element Plus 构建单页应用 (SPA)，负责页面展示和用户交互。通过 Axios 发送异步请求与后端通信。
2.  **后端层**：使用 Spring Boot 框架，提供 RESTful API 接口。
    - Controller 层：接收前端请求，参数校验。
    - Service 层：处理业务逻辑（如库存检查、价格计算）。
    - Repository 层：使用 Spring Data JPA 与数据库交互。
3.  **数据层**：使用 MySQL 8.0 存储业务数据。

（三）系统的详细设计
本系统主要包含以下功能模块，各模块均实现了完整的 CRUD (增删改查) 操作：

     **1. 基础信息管理模块 (CRUD)**
     - **功能**: 对水果、客户、员工、供应商进行管理。
     - **流程**:
       - **Create (增)**: 前端填写表单 -> POST 请求 -> Service 保存 -> 返回新对象。
       - **Read (查)**: GET 请求 -> Repository `findAll()` -> 返回列表。
       - **Update (改)**: PUT 请求 -> Service 更新字段 -> Repository `save()`。
       - **Delete (删)**: DELETE 请求 -> Repository `deleteById()`。

     **2. 销售业务模块**
     - **功能**: 处理销售交易，自动扣减库存。
     - **流程**:
       1. 用户在前端选择水果、客户、员工，输入数量。
       2. 前端发送 POST 请求至 `/api/sales`。
       3. 后端 `SalesService` 接收请求。
       4. **库存检查**: 查询 `Fruit` 表，判断 `stock >= quantity`。
       5. **扣减库存**: 若充足，`fruit.setStock(fruit.getStock() - quantity)`。
       6. **计算总价**: `totalPrice = fruit.getPrice() * quantity`。
       7. **保存记录**: 保存 `Sales` 对象，更新 `Fruit` 对象。

     **3. 进货业务模块**
     - **功能**: 处理入库交易，自动增加库存。
     - **流程**:
       1. 用户选择水果、供应商，输入数量。
       2. 后端接收请求，更新 `Fruit` 表：`fruit.setStock(fruit.getStock() + quantity)`。
       3. 保存 `StockIn` 记录。

（四）数据库设计
必须有 ER 图****\*****
(此处应插入 ER 图，描述 Fruit, Customer, Employee, Supplier, Sales, StockIn 之间的关系)

（五）系统的核心算法
本系统主要涉及业务逻辑计算和统计查询算法。
**1. 销售统计 (SQL 聚合)**:
`sql
     SELECT f.name, SUM(s.quantity) as total_qty, SUM(s.total_price) as total_revenue
     FROM sales s
     JOIN fruit f ON s.fruit_id = f.id
     GROUP BY f.id, f.name
     ORDER BY total_revenue DESC
     `
**2. 库存扣减 (Java 逻辑)**:
`java
     @Transactional
     public void processSale(Long fruitId, int quantity) {
         Fruit fruit = fruitRepository.findById(fruitId).orElseThrow();
         if (fruit.getStock() < quantity) {
             throw new RuntimeException("库存不足");
         }
         fruit.setStock(fruit.getStock() - quantity);
         fruitRepository.save(fruit);
     }
     `

三、系统编码及运行（三号，宋体，粗体）
（一）系统开发涉及的软件 1. **开发工具**: Visual Studio Code (VS Code) 2. **后端环境**: JDK 17, Maven 3.8+ 3. **前端环境**: Node.js 16+, Vue CLI / Vite 4. **数据库**: MySQL 8.0 Community Server 5. **辅助工具**: Postman (接口测试), Navicat (数据库管理)

（二）系统运行界面及结果
(此处应插入系统运行截图)
图 1 系统登录界面：采用现代化磨砂玻璃风格，支持管理员登录。
图 2 仪表盘界面：展示销售额统计图表和快捷入口。
图 3 销售管理界面：支持商品选择、数量输入和实时库存显示。

四、系统测试（三号，宋体，粗体）
**测试用例 1: 正常销售流程** - 输入: 选择“红富士苹果”, 数量 5, 客户“张三”, 员工“李四” - 预期: 提示销售成功，库存减少 5，生成销售记录。 - 结果: 成功，数据库数据一致。

     **测试用例 2: 库存不足测试**
     - 输入: 选择“香蕉”, 数量 10000 (超过库存)
     - 预期: 系统拦截请求，提示“库存不足”。
     - 结果: 界面弹出错误提示，数据库未发生变化。

五、总结
本次课程设计实现了一个功能完善的水果销售管理系统。
**主要收获**:

1.  熟练掌握了 Spring Boot + Vue 3 的前后端分离开发模式。
2.  深入理解了 JPA 的关联查询和事务管理机制。
3.  提升了数据库设计能力，特别是实体关系和外键约束的应用。
    **未来展望**:
    未来计划引入权限管理模块 (Spring Security) 和 支付接口对接，使系统更具实用性。

附录（源代码）
(此处可附上关键代码片段，如 SalesController.java, SalesService.java 等)

## 二、系统设计

### 2.1 系统中的数据定义 (数据库设计)

本系统使用 MySQL 数据库 `fruit_db`，包含以下核心数据表：

#### 1. 客户表 (customer)

存储购买水果的客户信息。
| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 客户 ID |
| name | VARCHAR(100) | NOT NULL | 客户姓名 |
| phone | VARCHAR(20) | | 联系电话 |
| address | VARCHAR(255) | | 联系地址 |

#### 2. 员工表 (employee)

存储店铺员工信息。
| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 员工 ID |
| name | VARCHAR(100) | NOT NULL | 员工姓名 |
| phone | VARCHAR(20) | | 联系电话 |
| position | VARCHAR(100) | | 职位 |

#### 3. 供应商表 (supplier)

存储水果供货商信息。
| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 供应商 ID |
| name | VARCHAR(100) | NOT NULL | 供应商名称 |
| phone | VARCHAR(20) | | 联系电话 |
| address | VARCHAR(255) | | 地址 |

#### 4. 水果商品表 (fruit)

存储水果的基本信息及当前库存。
| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 商品 ID |
| name | VARCHAR(100) | NOT NULL | 水果名称 |
| price | DECIMAL(10, 2) | NOT NULL | 销售单价 |
| stock | INT | DEFAULT 0 | 当前库存量 |
| supplier_id | BIGINT | FOREIGN KEY | 关联供应商 ID |

#### 5. 入库记录表 (stock_in)

记录每次进货的详细信息。
| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 记录 ID |
| fruit_id | BIGINT | FOREIGN KEY | 关联商品 ID |
| quantity | INT | NOT NULL | 入库数量 |
| stock_in_date | DATETIME | DEFAULT CURRENT_TIMESTAMP | 入库时间 |
| supplier_id | BIGINT | FOREIGN KEY | 关联供应商 ID |

#### 6. 销售记录表 (sales)

记录每一笔销售交易。
| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 销售 ID |
| fruit_id | BIGINT | FOREIGN KEY | 关联商品 ID |
| customer_id | BIGINT | FOREIGN KEY | 关联客户 ID |
| employee_id | BIGINT | FOREIGN KEY | 关联员工 ID |
| quantity | INT | NOT NULL | 销售数量 |
| total_price | DECIMAL(10, 2) | | 本单总价 |
| sale_date | DATETIME | DEFAULT CURRENT_TIMESTAMP | 销售时间 |

### 2.2 系统的概要设计

系统采用经典的分层架构：

1.  **表现层 (Controller)**: `FruitController` 负责接收前端 AJAX 请求，处理参数，并返回 JSON 格式的数据。
2.  **业务逻辑层 (Service)**: `FruitService` 封装具体的业务逻辑，如销售时的库存检查、价格计算、事务管理。
3.  **数据访问层 (Repository)**: 使用 Spring Data JPA 接口 (`FruitRepository`, `SalesRepository` 等) 直接与 MySQL 数据库交互，无需编写复杂的 SQL 语句。
4.  **实体层 (Entity)**: 定义与数据库表一一对应的 Java 类 (`Fruit`, `Sales` 等)。

### 2.3 系统的核心算法与逻辑

1.  **销售逻辑 (Sale Process)**:

    - 接收销售请求（商品 ID, 数量, 客户 ID, 员工 ID）。
    - 查询商品当前库存。
    - **判断**: 若 `请求数量 > 当前库存`，抛出异常，提示“库存不足”。
    - **计算**: `总价 = 商品单价 * 销售数量`。
    - **更新**: 扣减商品表中的库存 (`stock = stock - quantity`)。
    - **记录**: 在销售表中插入一条新记录。
    - 以上步骤在同一个事务 (`@Transactional`) 中执行，确保数据一致性。

2.  **入库逻辑 (Stock In Process)**:
    - 接收入库请求。
    - 在入库记录表中添加记录。
    - 更新商品表库存 (`stock = stock + quantity`)。

---

## 三、系统编码及运行

### 3.1 系统开发涉及的软件与技术栈

- **操作系统**: Windows
- **开发工具 (IDE)**: Visual Studio Code (VS Code)
- **构建工具**: Maven
- **后端技术**:
  - **语言**: Java 17 / 21
  - **框架**: Spring Boot 2.7.5
  - **ORM**: Spring Data JPA
- **前端技术**:
  - **结构**: HTML5
  - **样式**: Bootstrap 5 (CSS Framework)
  - **脚本**: JavaScript (ES6), jQuery
  - **交互**: SweetAlert2 (弹窗组件)
- **数据库**: MySQL 8.0
- **接口测试**: Postman / 浏览器开发者工具

### 3.2 系统运行界面

系统采用单页应用 (SPA) 风格的设计，左侧为导航菜单，右侧为内容区域。

1.  **首页**: 显示欢迎信息和系统概览。
2.  **商品管理页**: 表格展示所有水果，支持“添加商品”模态框，可进行删除操作。
3.  **销售管理页**:
    - 左侧：销售开单区域，下拉选择商品（显示实时库存和价格）、客户、员工，输入数量。
    - 右侧：实时销售记录列表。
4.  **统计页**: 图表或表格形式展示各商品的销售业绩。

---

## 四、系统测试

### 4.1 测试环境

- **硬件**: PC 机 (Intel Core i5, 16GB RAM)
- **软件**: Windows 11, Chrome 浏览器, MySQL 8.0

### 4.2 测试用例与结果

1.  **库存不足测试**:
    - **操作**: 选择库存为 10 的“红富士苹果”，输入销售数量 20，点击提交。
    - **预期**: 系统弹出错误提示“库存不足”，数据库不发生变化。
    - **结果**: 界面显示 SweetAlert 错误弹窗，测试通过。
2.  **正常销售测试**:
    - **操作**: 选择库存为 10 的“香蕉”，输入销售数量 5。
    - **预期**: 提示“销售成功”，库存变为 5，销售记录表中增加一条记录。
    - **结果**: 操作成功，数据更新正确，测试通过。
3.  **数据关联测试**:
    - **操作**: 删除一个已有销售记录的商品。
    - **预期**: 由于外键约束，数据库应阻止删除，或级联处理（视配置而定，本系统配置为保护数据，提示无法删除）。
    - **结果**: 后端捕获外键异常，前端提示“该商品存在关联数据，无法删除”，测试通过。

### 4.3 遇到的问题及解决方案

- **问题**: 数据库中文乱码。
  - **解决**: 在 `application.properties` 连接字符串中添加 `characterEncoding=utf-8`，并将数据库字符集设置为 `utf8mb4`。
- **问题**: 前端页面切换时数据未刷新。
  - **解决**: 在 `loadPage` 函数中添加回调，每次切换页面时重新调用 `loadData()` 获取最新数据。
- **问题**: 跨域请求被拦截 (CORS)。
  - **解决**: 在 Spring Boot 控制器类上添加 `@CrossOrigin(origins = "*")` 注解。

---

## 五、总结

本项目成功开发了一套基于 Spring Boot 和 MySQL 的水果销售管理系统。

- **技术层面**: 摒弃了传统的 Java Swing 桌面开发模式，采用了更符合现代潮流的 B/S 架构。后端利用 Spring Boot 的自动配置极大简化了开发流程，前端使用 Bootstrap 实现了美观的响应式界面。
- **功能层面**: 实现了从进货到销售的完整闭环，数据结构设计合理，能够满足小型水果店的日常经营需求。
- **收获**: 深入理解了 RESTful API 的设计原则，掌握了前后端分离开发的基本模式，以及 JPA 在处理复杂数据库关系时的优势。

未来可以进一步扩展功能，如增加会员积分系统、引入 ECharts 图表进行可视化数据分析、以及部署到云服务器实现远程访问。
