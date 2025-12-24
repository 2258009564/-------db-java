package com.fruit.system.repository;

import com.fruit.system.entity.StockIn;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 入库记录数据访问接口
 */
public interface StockInRepository extends JpaRepository<StockIn, Long> {
}
