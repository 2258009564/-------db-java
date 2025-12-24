package com.fruit.system.service;

import com.fruit.system.entity.*;
import com.fruit.system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class FruitService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private FruitRepository fruitRepository;
    @Autowired
    private StockInRepository stockInRepository;
    @Autowired
    private SalesRepository salesRepository;

    // Customer
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    public List<Customer> searchCustomers(String keyword) {
        return customerRepository.findByNameContainingOrPhoneContaining(keyword, keyword);
    }

    // Employee
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public List<Employee> searchEmployees(String name) {
        return employeeRepository.findByNameContaining(name);
    }

    // Supplier
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier saveSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }

    public List<Supplier> searchSuppliers(String name) {
        return supplierRepository.findByNameContaining(name);
    }

    // Fruit
    public List<Fruit> getAllFruits() {
        return fruitRepository.findAll();
    }

    public Fruit saveFruit(Fruit fruit) {
        return fruitRepository.save(fruit);
    }

    public void deleteFruit(Long id) {
        fruitRepository.deleteById(id);
    }

    public List<Fruit> searchFruits(String name) {
        return fruitRepository.findByNameContaining(name);
    }

    public Fruit getFruitById(Long id) {
        return fruitRepository.findById(id).orElse(null);
    }

    // Stock In
    @Transactional
    public StockIn stockIn(StockIn stockIn) {
        Fruit fruit = fruitRepository.findById(stockIn.getFruitId())
                .orElseThrow(() -> new RuntimeException("Fruit not found"));
        fruit.setStock(fruit.getStock() + stockIn.getQuantity());
        fruitRepository.save(fruit);
        return stockInRepository.save(stockIn);
    }

    // Sales
    @Transactional
    public Sales sellFruit(Sales sales) {
        Fruit fruit = fruitRepository.findById(sales.getFruitId())
                .orElseThrow(() -> new RuntimeException("Fruit not found"));
        if (fruit.getStock() < sales.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }
        fruit.setStock(fruit.getStock() - sales.getQuantity());
        sales.setTotalPrice(fruit.getPrice() * sales.getQuantity());
        fruitRepository.save(fruit);
        return salesRepository.save(sales);
    }

    public List<Sales> getAllSales() {
        return salesRepository.findAll();
    }

    public List<Map<String, Object>> getSalesStats() {
        return salesRepository.getSalesStats();
    }
}
