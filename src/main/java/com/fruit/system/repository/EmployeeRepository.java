package com.fruit.system.repository;

import com.fruit.system.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 员工数据访问接口
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * 根据姓名模糊查询员工
     * 
     * @param name 姓名关键字
     * @return 员工列表
     */
    List<Employee> findByNameContaining(String name);
}
