package com.fruit.system.repository;

import com.fruit.system.entity.Fruit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 水果商品数据访问接口
 */
public interface FruitRepository extends JpaRepository<Fruit, Long> {

    /**
     * 根据名称模糊查询水果
     * 
     * @param name 名称关键字
     * @return 水果列表
     */
    List<Fruit> findByNameContaining(String name);
}
