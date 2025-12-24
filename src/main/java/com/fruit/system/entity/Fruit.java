package com.fruit.system.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "fruit")
public class Fruit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private Integer stock;

    @Column(name = "supplier_id")
    private Long supplierId;
}
