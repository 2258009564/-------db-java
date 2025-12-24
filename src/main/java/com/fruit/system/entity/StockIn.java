package com.fruit.system.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 入库记录实体类 (StockIn Entity)
 * 对应数据库中的 stock_in 表
 * 用于记录水果的进货信息
 */
@Data
@Entity
@Table(name = "stock_in")
public class StockIn {

    /**
     * 入库记录ID (主键)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 入库的水果ID
     * 关联 Fruit 表
     */
    @Column(name = "fruit_id")
    private Long fruitId;

    /**
     * 入库数量
     */
    private Integer quantity;

    /**
     * 入库时间
     */
    @Column(name = "stock_in_date")
    private LocalDateTime stockInDate;

    /**
     * 供应商ID
     * 关联 Supplier 表
     */
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 自动设置入库时间
     */
    @PrePersist
    protected void onCreate() {
        stockInDate = LocalDateTime.now();
    }
}
