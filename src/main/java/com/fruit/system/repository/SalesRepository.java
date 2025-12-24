package com.fruit.system.repository;

import com.fruit.system.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Map;

public interface SalesRepository extends JpaRepository<Sales, Long> {

    @Query(value = "SELECT f.name as fruitName, SUM(s.quantity) as totalQuantity, SUM(s.total_price) as totalRevenue " +
            "FROM sales s JOIN fruit f ON s.fruit_id = f.id " +
            "GROUP BY f.name", nativeQuery = true)
    List<Map<String, Object>> getSalesStats();
}
