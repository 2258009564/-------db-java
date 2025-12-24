package com.fruit.system.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sales")
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fruit_id")
    private Long fruitId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "employee_id")
    private Long employeeId;

    private Integer quantity;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "sale_date")
    private LocalDateTime saleDate;

    @PrePersist
    protected void onCreate() {
        saleDate = LocalDateTime.now();
    }
}
