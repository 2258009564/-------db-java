package com.fruit.system.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 销售记录实体类 (Sales Entity)
 * 对应数据库中的 sales 表
 * 用于记录每一笔销售交易的详细信息
 */
@Data
@Entity
@Table(name = "sales")
public class Sales {

    /**
     * 销售记录ID (主键)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 销售的水果ID
     * 关联 Fruit 表
     */
    @Column(name = "fruit_id")
    private Long fruitId;

    /**
     * 购买客户ID
     * 关联 Customer 表
     */
    @Column(name = "customer_id")
    private Long customerId;

    /**
     * 经手员工ID
     * 关联 Employee 表
     */
    @Column(name = "employee_id")
    private Long employeeId;

    /**
     * 销售数量
     */
    private Integer quantity;

    /**
     * 本单总价
     * 通常由 数量 * 单价 计算得出
     */
    @Column(name = "total_price")
    private Double totalPrice;

    /**
     * 销售时间
     */
    @Column(name = "sale_date")
    private LocalDateTime saleDate;

    /**
     * 在持久化之前执行的方法
     * 用于自动设置销售时间为当前时间
     */
    @PrePersist
    protected void onCreate() {
        saleDate = LocalDateTime.now();
    }
}
