DROP DATABASE IF EXISTS fruit_db;

CREATE DATABASE fruit_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE fruit_db;

SET NAMES utf8mb4;

-- 1. 客户表 (Customer)
CREATE TABLE customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '客户ID',
    name VARCHAR(100) NOT NULL COMMENT '客户姓名',
    phone VARCHAR(20) COMMENT '联系电话',
    address VARCHAR(255) COMMENT '联系地址'
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 2. 员工表 (Employee)
CREATE TABLE employee (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '员工ID',
    name VARCHAR(100) NOT NULL COMMENT '员工姓名',
    phone VARCHAR(20) COMMENT '联系电话',
    position VARCHAR(100) COMMENT '职位'
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 3. 供应商表 (Supplier)
CREATE TABLE supplier (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '供应商ID',
    name VARCHAR(100) NOT NULL COMMENT '供应商名称',
    phone VARCHAR(20) COMMENT '联系电话',
    address VARCHAR(255) COMMENT '供应商地址'
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 4. 水果商品表 (Fruit)
CREATE TABLE fruit (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '商品ID',
    name VARCHAR(100) NOT NULL COMMENT '水果名称',
    price DECIMAL(10, 2) NOT NULL COMMENT '销售价格',
    stock INT DEFAULT 0 COMMENT '当前库存',
    supplier_id BIGINT COMMENT '关联供应商ID',
    FOREIGN KEY (supplier_id) REFERENCES supplier (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 5. 入库记录表 (StockIn)
CREATE TABLE stock_in (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '入库记录ID',
    fruit_id BIGINT COMMENT '入库水果ID',
    supplier_id BIGINT COMMENT '供货商ID',
    quantity INT NOT NULL COMMENT '入库数量',
    stock_in_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
    FOREIGN KEY (fruit_id) REFERENCES fruit (id),
    FOREIGN KEY (supplier_id) REFERENCES supplier (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 6. 销售记录表 (Sales)
CREATE TABLE sales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '销售记录ID',
    fruit_id BIGINT COMMENT '销售水果ID',
    customer_id BIGINT COMMENT '购买客户ID',
    employee_id BIGINT COMMENT '经手员工ID',
    quantity INT NOT NULL COMMENT '销售数量',
    total_price DECIMAL(10, 2) COMMENT '总金额',
    sale_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '销售时间',
    FOREIGN KEY (fruit_id) REFERENCES fruit (id),
    FOREIGN KEY (customer_id) REFERENCES customer (id),
    FOREIGN KEY (employee_id) REFERENCES employee (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 插入测试数据
INSERT INTO customer (name, phone, address) VALUES 
('张三', '13800138000', '北京市朝阳区'),
('李四', '13900139000', '上海市浦东新区');

INSERT INTO employee (name, phone, position) VALUES 
('王五', '13700137000', '店长'),
('赵六', '13600136000', '销售员');

INSERT INTO supplier (name, phone, address) VALUES 
('山东苹果基地', '0535-1234567', '山东省烟台市'),
('海南热带水果', '0898-7654321', '海南省海口市');

INSERT INTO fruit (name, price, stock, supplier_id) VALUES 
('红富士苹果', 5.50, 100, 1),
('香蕉', 3.20, 200, 2),
('巨峰葡萄', 8.00, 50, 1);
