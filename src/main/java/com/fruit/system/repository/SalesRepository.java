package com.fruit.system.repository;

import com.fruit.system.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Map;

/**
 * 销售记录数据访问接口
 */
public interface SalesRepository extends JpaRepository<Sales, Long> {

    /**
     * 获取销售统计数据
     * 使用 @Query 注解自定义 SQL 查询
     * 统计每种水果的总销量和总销售额
     * 
     * @return 包含统计结果的 Map 列表 (fruitName, totalQuantity, totalRevenue)
     */
    @Query(value = "SELECT f.name as fruitName, SUM(s.quantity) as totalQuantity, SUM(s.total_price) as totalRevenue " +
            "FROM sales s JOIN fruit f ON s.fruit_id = f.id " +
            "GROUP BY f.name", nativeQuery = true)
    List<Map<String, Object>> getSalesStats();
}
