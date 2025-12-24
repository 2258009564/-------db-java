package com.fruit.system.repository;

import com.fruit.system.entity.Fruit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FruitRepository extends JpaRepository<Fruit, Long> {
    List<Fruit> findByNameContaining(String name);
}
