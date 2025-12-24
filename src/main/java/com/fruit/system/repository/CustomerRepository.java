package com.fruit.system.repository;

import com.fruit.system.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByNameContainingOrPhoneContaining(String name, String phone);
}
