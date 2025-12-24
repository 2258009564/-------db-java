package com.fruit.system.entity;

import lombok.Data;
import javax.persistence.*;

/**
 * 供应商实体类 (Supplier Entity)
 * 对应数据库中的 supplier 表
 * 用于存储水果供货商的信息
 */
@Data
@Entity
@Table(name = "supplier")
public class Supplier {

    /**
     * 供应商ID (主键)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 供应商名称
     */
    private String name;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 供应商地址
     */
    private String address;
}
