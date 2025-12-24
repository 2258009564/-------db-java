package com.fruit.system.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "stock_in")
public class StockIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fruit_id")
    private Long fruitId;

    private Integer quantity;

    @Column(name = "stock_in_date")
    private LocalDateTime stockInDate;

    @Column(name = "supplier_id")
    private Long supplierId;

    @PrePersist
    protected void onCreate() {
        stockInDate = LocalDateTime.now();
    }
}
