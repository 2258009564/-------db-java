package com.fruit.system.repository;

import com.fruit.system.entity.StockIn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockInRepository extends JpaRepository<StockIn, Long> {
}
