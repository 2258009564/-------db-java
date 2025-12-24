package com.fruit.system.entity;

import lombok.Data;
import javax.persistence.*;

/**
 * 水果商品实体类 (Fruit Entity)
 * 对应数据库中的 fruit 表
 * 用于存储水果的名称、价格、库存等核心信息
 */
@Data // Lombok 注解：自动生成样板代码
@Entity // JPA 注解：实体类标识
@Table(name = "fruit") // JPA 注解：对应表名
public class Fruit {

    /**
     * 商品ID (主键)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 水果名称
     * 如：红富士苹果、香蕉等
     */
    private String name;

    /**
     * 销售价格
     * 单位：元
     */
    private Double price;

    /**
     * 当前库存数量
     * 每次进货会增加，销售会减少
     */
    private Integer stock;

    /**
     * 供应商ID
     * 关联 Supplier 表的主键，表示该水果的主要供货商
     * 
     * @Column(name = "supplier_id"): 指定映射到数据库表的 supplier_id 列
     */
    @Column(name = "supplier_id")
    private Long supplierId;
}
