package com.fruit.system.entity;

import lombok.Data;
import javax.persistence.*;

/**
 * 客户实体类 (Customer Entity)
 * 对应数据库中的 customer 表
 * 用于存储购买水果的客户信息
 */
@Data // Lombok 注解：自动生成 Getter, Setter, toString, equals, hashCode 等方法，简化代码
@Entity // JPA 注解：标识这是一个实体类，与数据库表进行映射
@Table(name = "customer") // JPA 注解：指定该实体类对应的数据库表名为 "customer"
public class Customer {

    /**
     * 客户ID (主键)
     * 
     * @Id: 标识该字段为主键
     * @GeneratedValue: 指定主键生成策略，IDENTITY 表示使用数据库的自增列 (Auto Increment)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户姓名
     * 对应数据库表中的 name 字段
     */
    private String name;

    /**
     * 客户联系电话
     * 对应数据库表中的 phone 字段
     */
    private String phone;

    /**
     * 客户地址
     * 对应数据库表中的 address 字段
     */
    private String address;
}
