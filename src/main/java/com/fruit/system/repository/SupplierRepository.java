package com.fruit.system.repository;

import com.fruit.system.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 供应商数据访问接口
 */
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    /**
     * 根据名称模糊查询供应商
     * 
     * @param name 名称关键字
     * @return 供应商列表
     */
    List<Supplier> findByNameContaining(String name);
}
