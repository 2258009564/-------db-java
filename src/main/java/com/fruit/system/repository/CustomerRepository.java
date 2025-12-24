package com.fruit.system.repository;

import com.fruit.system.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 客户数据访问接口 (Customer Repository)
 * 继承 JpaRepository，自动获得基本的 CRUD (增删改查) 功能
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * 根据姓名或电话模糊查询客户
     * Spring Data JPA 会根据方法名自动生成 SQL 语句
     * 相当于: SELECT * FROM customer WHERE name LIKE %name% OR phone LIKE %phone%
     * 
     * @param name  姓名关键字
     * @param phone 电话关键字
     * @return 符合条件的客户列表
     */
    List<Customer> findByNameContainingOrPhoneContaining(String name, String phone);
}
