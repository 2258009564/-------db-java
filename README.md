# 水果销售管理系统 (Pro 版)

## 1. 项目简介

这是一个基于 **Java Spring Boot** 和 **Vue 3** 开发的高端水果销售管理系统。相比旧版本，Pro 版采用了前后端分离架构（单页应用 SPA），拥有现代化的 **Element Plus** UI 设计、强大的数据可视化仪表盘，以及模块化的后端架构。旨在提供“低调奢华有内涵”的用户体验。

---

## 2. 项目结构 (File Tree)

```text
水果管理系统 db-java/
├── pom.xml                     # Maven 项目配置文件
├── README.md                   # 项目说明文档
├── Fruit_System_Report.md      # 课程设计报告
├── sql/                        # 数据库脚本
│   └── init.sql                # 数据库初始化 SQL
├── vue-frontend/               # Vue 3 前端项目源码
│   ├── src/
│   │   ├── api/                # Axios 请求封装
│   │   ├── components/         # 公共组件
│   │   ├── layout/             # 布局组件
│   │   ├── router/             # 路由配置
│   │   └── views/              # 页面视图
│   ├── package.json            # 前端依赖配置
│   └── vite.config.js          # Vite 构建配置
├── src/
│   └── main/
│       ├── java/com/fruit/system/
│       │   ├── controller/     # 控制层 (API 接口)
│       │   ├── service/        # 业务层 (逻辑处理)
│       │   ├── repository/     # 数据访问层 (JPA)
│       │   ├── entity/         # 实体类 (数据库映射)
│       │   └── FruitManagementSystemApplication.java # 启动类
│       └── resources/
│           ├── application.properties # 后端配置文件
│           └── static/         # [自动生成] 编译后的前端静态资源
```

---

## 3. 使用指南 (User Guide)

### 3.1 环境准备

在运行本项目之前，请确保您的电脑已安装以下环境：

- **Java JDK**: 推荐 JDK 17 或 JDK 21。
- **Node.js**: 推荐 Node.js 16+ (用于构建前端)。
- **MySQL**: 推荐 MySQL 8.0。
- **Maven**: 用于构建项目。

### 3.2 数据库配置

1.  **创建数据库**: 登录 MySQL，执行 `sql/init.sql` 中的脚本，或者手动创建数据库 `fruit_db` 并导入表结构。
2.  **修改配置**: 打开 `src/main/resources/application.properties` 文件，确认数据库连接信息：
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/fruit_db?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf-8
    spring.datasource.username=root
    spring.datasource.password=你的数据库密码
    ```

### 3.3 如何运行 (How to Run)

**第一步：构建前端 (Build Frontend)**

本项目前端独立开发，需先编译生成静态资源。

```bash
cd vue-frontend
npm install
npm run build
```

_构建完成后，资源会自动输出到 `src/main/resources/static`，后端即可读取。_

**第二步：启动后端 (Run Backend)**

1.  使用 VS Code 打开项目根目录。
2.  找到启动类 `src/main/java/com/fruit/system/FruitManagementSystemApplication.java`。
3.  点击 **Run** 运行。

**第三步：访问系统**

- 打开浏览器，访问: [http://localhost:8080](http://localhost:8080)
- **默认账号**: `admin`
- **默认密码**: `123456`

### 3.4 开发模式

如果需要修改前端代码并实时预览（热更新）：

```bash
cd vue-frontend
npm run serve
```

---

## 4. 功能模块说明

### 4.1 核心亮点 (Pro Features)

- **可视化仪表盘**: 首页展示销售额甜甜圈图、员工业绩柱状图、实时数据卡片。
- **现代化 UI**: 采用磨砂玻璃质感登录页、渐变色统计卡片、流畅的页面切换动画。
- **个人中心**: 支持用户头像下拉菜单（个人中心/退出登录）。

### 4.2 业务模块

- **基础管理**: 包含水果库存、员工、客户、供应商的增删改查（使用统一的 BaseTable 组件）。
- **进销存**: 完整的进货记录和销售记录追踪。

---

## 5. 技术栈

- **后端**: Java, Spring Boot 2.7.5, Spring Data JPA
- **前端**: Vue 3, Vite, Element Plus, Vue Router 4, Chart.js
- **数据库**: MySQL 8.0
- **交互**: RESTful API, Axios

---

## 6. 数据库设计 (ER 简述)

- **Customer (客户)**: id, name, phone, address
- **Employee (员工)**: id, name, phone, position
- **Supplier (供应商)**: id, name, phone, address
- **Fruit (商品)**: id, name, price, stock, supplier_id
- **Sales (销售记录)**: id, fruit_id, customer_id, employee_id, quantity, total_price, sale_date
