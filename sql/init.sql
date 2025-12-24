DROP DATABASE IF EXISTS fruit_db;

CREATE DATABASE fruit_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE fruit_db;

SET NAMES utf8mb4;

-- 客户表
CREATE TABLE customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 员工表
CREATE TABLE employee (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    position VARCHAR(100)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 供应商表
CREATE TABLE supplier (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 水果商品表
CREATE TABLE fruit (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock INT DEFAULT 0,
    supplier_id BIGINT,
    FOREIGN KEY (supplier_id) REFERENCES supplier (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 入库记录表
CREATE TABLE stock_in (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fruit_id BIGINT,
    quantity INT NOT NULL,
    stock_in_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    supplier_id BIGINT,
    FOREIGN KEY (fruit_id) REFERENCES fruit (id),
    FOREIGN KEY (supplier_id) REFERENCES supplier (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 销售记录表
CREATE TABLE sales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fruit_id BIGINT,
    customer_id BIGINT,
    employee_id BIGINT,
    quantity INT NOT NULL,
    total_price DECIMAL(10, 2),
    sale_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fruit_id) REFERENCES fruit (id),
    FOREIGN KEY (customer_id) REFERENCES customer (id),
    FOREIGN KEY (employee_id) REFERENCES employee (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 插入测试数据
INSERT INTO
    customer (name, phone, address)
VALUES ('张三', '13800138000', '北京市朝阳区');

INSERT INTO
    customer (name, phone, address)
VALUES (
        '李四',
        '13900139000',
        '上海市浦东新区'
    );

INSERT INTO
    employee (name, phone, position)
VALUES ('王五', '13700137000', '店长');

INSERT INTO
    employee (name, phone, position)
VALUES ('赵六', '13600136000', '销售员');

INSERT INTO
    supplier (name, phone, address)
VALUES (
        '山东苹果基地',
        '0535-1234567',
        '山东省烟台市'
    );

INSERT INTO
    supplier (name, phone, address)
VALUES (
        '海南热带水果',
        '0898-7654321',
        '海南省海口市'
    );

INSERT INTO
    fruit (
        name,
        price,
        stock,
        supplier_id
    )
VALUES ('红富士苹果', 5.50, 100, 1);

INSERT INTO
    fruit (
        name,
        price,
        stock,
        supplier_id
    )
VALUES ('香蕉', 3.20, 200, 2);

INSERT INTO
    fruit (
        name,
        price,
        stock,
        supplier_id
    )
VALUES ('巨峰葡萄', 8.00, 50, 1);