# 水果销售管理系统 (Pro 版)

## 1. 项目简介

这是一个基于 **Java Spring Boot** 和 **Vue 3** 开发的高端水果销售管理系统。相比旧版本，Pro 版采用了前后端分离架构（单页应用 SPA），拥有现代化的 **Element Plus** UI 设计、强大的数据可视化仪表盘，以及模块化的后端架构。旨在提供“低调奢华有内涵”的用户体验。

---

## 2. 快速开始 (Quick Start)

### 2.1 环境准备

在运行本项目之前，请确保您的电脑已安装以下环境：

- **Java JDK**: 推荐 JDK 17 或 JDK 21。
- **Node.js**: 推荐 Node.js 16+ (用于构建前端)。
- **MySQL**: 推荐 MySQL 8.0。
- **Maven**: (可选) 如果使用 VS Code 或 IntelliJ IDEA，IDE 会自动处理依赖，无需手动安装 Maven。
- **Git**: 用于下载代码。

### 2.2 下载项目

打开终端（Terminal）或 Git Bash，执行以下命令：

```bash
git clone https://github.com/your-username/fruit-management-system.git
cd fruit-management-system
```

_(注：如果是下载的 ZIP 包，请直接解压并进入文件夹)_

### 2.3 初始化数据库

1.  **创建数据库**: 登录 MySQL，创建一个名为 `fruit_db` 的数据库。
    ```sql
    CREATE DATABASE fruit_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    ```
2.  **导入数据**: 运行项目根目录下的 `sql/init.sql` 脚本。
    - 可以使用 Navicat、DBeaver 等工具导入。
    - 或者使用命令行：`mysql -u root -p fruit_db < sql/init.sql`
3.  **配置连接**: 打开 `src/main/resources/application.properties` 文件，修改数据库密码：
    ```properties
    spring.datasource.username=root
    spring.datasource.password=你的数据库密码
    ```

### 2.4 第一次构建 (First Build)

在首次运行前，需要下载依赖包。

**后端依赖 (Maven):**

```bash
mvn clean install
```

_(如果使用 VS Code，打开项目后等待 Java 插件自动加载依赖即可)_

**前端依赖 (Node.js):**

```bash
cd vue-frontend
npm install
```

### 2.5 运行项目 (Run)

本项目采用前后端分离架构，需要**同时启动**两个服务（后端 + 前端）。

**1. 启动后端 (Backend)**

保持当前终端窗口，执行：

```bash
# 在项目根目录
mvn spring-boot:run
```

_(或者在 VS Code 中找到 `FruitManagementSystemApplication.java` 点击 Run)_

> **注意**: 看到 `Started FruitManagementSystemApplication in ... seconds` 即表示启动成功。**请不要关闭此窗口**。

**2. 启动前端 (Frontend)**

**新建**一个终端窗口 (Terminal)，执行：

```bash
# 进入前端目录
cd vue-frontend
# 启动开发服务器
npm run serve
```

**3. 访问系统**

打开浏览器访问：[http://localhost:5173](http://localhost:5173)

- **默认账号**: `admin`
- **默认密码**: `123`

---

## 3. 项目结构 (File Tree)

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

---

## 7. 常见问题 (Troubleshooting)

**Q1: 启动后端时报错 "Port 8080 was already in use"**

- **原因**: 端口 8080 被占用。
- **解决**: 关闭占用该端口的程序，或者修改 `src/main/resources/application.properties` 中的 `server.port=8081`，然后重启后端。

**Q2: 前端页面显示 "Network Error" 或无法加载数据**

- **原因**: 后端服务未启动，或前端代理配置错误。
- **解决**: 确保后端控制台没有报错且正在运行。检查 `vue-frontend/vite.config.js` 中的 `proxy` 目标地址是否为 `http://localhost:8080`。

**Q3: 登录失败，提示 "Bad Credentials"**

- **原因**: 数据库密码错误或数据未导入。
- **解决**: 检查 `application.properties` 中的数据库密码是否正确；检查数据库 `fruit_db` 中是否有 `employee` 表且包含 admin 用户。
