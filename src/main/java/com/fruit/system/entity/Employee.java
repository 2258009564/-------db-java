package com.fruit.system.entity;

import lombok.Data;
import javax.persistence.*;

/**
 * 员工实体类 (Employee Entity)
 * 对应数据库中的 employee 表
 * 用于存储店铺员工的基本信息
 */
@Data // Lombok 注解：自动生成 Getter, Setter, toString 等方法
@Entity // JPA 注解：标识这是一个实体类
@Table(name = "employee") // JPA 注解：指定对应的数据库表名为 "employee"
public class Employee {

    /**
     * 员工ID (主键)
     * 使用数据库自增策略
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 员工联系电话
     */
    private String phone;

    /**
     * 员工职位
     * 例如：店长、销售员、理货员等
     */
    private String position;
}
