# 水果销售管理系统

## 1. 项目简介

这是一个基于 Java Spring Boot 和 MySQL 开发的水果销售管理系统。旨在帮助水果店进行高效的客户、员工、供应商、商品管理，以及处理日常的入库和销售业务。

---

## 2. 项目结构 (File Tree)

```text
水果管理系统 db-java/
├── pom.xml                     # Maven 项目配置文件 (依赖管理)
├── README.md                   # 项目说明文档
├── sql/
│   └── init.sql                # 数据库初始化脚本 (建表语句)
├── src/
│   └── main/
│       ├── java/
│       │   └── com/fruit/system/
│       │       ├── controller/ # 控制层 (API 接口)
│       │       ├── entity/     # 实体类 (数据库表映射)
│       │       ├── repository/ # 数据访问层 (DAO)
│       │       ├── service/    # 业务逻辑层
│       │       └── FruitManagementSystemApplication.java # 启动类
│       └── resources/
│           ├── application.properties # 配置文件 (数据库连接等)
│           └── static/         # 前端静态资源
│               ├── css/
│               │   └── style.css   # 样式表
│               ├── js/
│               │   └── app.js      # 前端逻辑脚本
│               └── index.html      # 系统入口页面
```

---

## 3. 使用指南 (User Guide)

### 3.1 环境准备

在运行本项目之前，请确保您的电脑已安装以下环境：

- **Java JDK**: 推荐 JDK 17 或 JDK 21。
- **MySQL**: 推荐 MySQL 8.0。
- **Maven**: 用于构建项目（通常集成在 IDE 中）。
- **VS Code**: 推荐使用的代码编辑器，需安装 "Extension Pack for Java"。

### 3.2 数据库配置

1.  **创建数据库**: 登录 MySQL，执行 `sql/init.sql` 中的脚本，或者手动创建数据库 `fruit_db` 并导入表结构。
2.  **修改配置**: 打开 `src/main/resources/application.properties` 文件，确认数据库连接信息：
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/fruit_db?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf-8
    spring.datasource.username=root
    spring.datasource.password=你的数据库密码
    ```

### 3.3 如何运行 (How to Run)

1.  **打开项目**: 使用 VS Code 打开 `水果管理系统 db-java` 文件夹。
2.  **找到启动类**: 在资源管理器中找到 `src/main/java/com/fruit/system/FruitManagementSystemApplication.java`。
3.  **启动服务**:
    - 打开该文件。
    - 点击代码上方的 **"Run"** 按钮，或者按 `F5` 调试运行。
    - 观察下方 **Terminal (终端)** 面板，等待出现 `Started FruitManagementSystemApplication in ... seconds` 字样。
4.  **访问系统**:
    - 打开浏览器，访问: [http://localhost:8080](http://localhost:8080)

### 3.4 如何结束运行 (How to Stop)

- 在 VS Code 的 **Terminal (终端)** 面板中，点击右上角的 **垃圾桶图标 (Kill Terminal)**。
- 或者在终端内点击，然后按快捷键 `Ctrl + C`。

---

## 4. 功能模块说明

### 4.1 基础管理

- **客户管理**: 录入客户信息（姓名、电话、地址），支持查询和删除。
- **员工管理**: 管理店铺员工信息及职位。
- **供应商管理**: 维护水果供应商的联系方式。
- **商品管理**: 录入水果信息，设置价格，查看当前库存。

### 4.2 业务处理

- **入库管理**: 选择供应商和商品进行入库操作，系统会自动增加对应商品的库存。
- **销售管理**: 选择客户、员工和商品进行销售，系统会自动扣减库存并计算总价。如果库存不足会提示错误。

### 4.3 数据统计

- **销售统计**: 查看每种水果的总销售数量和总销售额报表。

---

## 5. 技术栈

- **后端**: Java, Spring Boot 2.7.x, Spring Data JPA
- **数据库**: MySQL 8.0
- **前端**: HTML5, Bootstrap 5, jQuery, SweetAlert2
- **交互**: RESTful API, AJAX

---

## 6. 数据库设计 (ER 简述)

- **Customer (客户)**: id, name, phone, address
- **Employee (员工)**: id, name, phone, position
- **Supplier (供应商)**: id, name, phone, address
- **Fruit (商品)**: id, name, price, stock, supplier_id
- **Sales (销售记录)**: id, fruit_id, customer_id, employee_id, quantity, total_price, sale_date
