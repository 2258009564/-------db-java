package com.fruit.system.controller;

import com.fruit.system.entity.*;
import com.fruit.system.service.FruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FruitController {

    @Autowired
    private FruitService fruitService;

    // Customer
    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return fruitService.getAllCustomers();
    }

    @PostMapping("/customers")
    public Customer createCustomer(@RequestBody Customer customer) {
        return fruitService.saveCustomer(customer);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        fruitService.deleteCustomer(id);
    }

    @GetMapping("/customers/search")
    public List<Customer> searchCustomers(@RequestParam String keyword) {
        return fruitService.searchCustomers(keyword);
    }

    // Employee
    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return fruitService.getAllEmployees();
    }

    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee) {
        return fruitService.saveEmployee(employee);
    }

    @DeleteMapping("/employees/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        fruitService.deleteEmployee(id);
    }

    @GetMapping("/employees/search")
    public List<Employee> searchEmployees(@RequestParam String name) {
        return fruitService.searchEmployees(name);
    }

    // Supplier
    @GetMapping("/suppliers")
    public List<Supplier> getAllSuppliers() {
        return fruitService.getAllSuppliers();
    }

    @PostMapping("/suppliers")
    public Supplier createSupplier(@RequestBody Supplier supplier) {
        return fruitService.saveSupplier(supplier);
    }

    @DeleteMapping("/suppliers/{id}")
    public void deleteSupplier(@PathVariable Long id) {
        fruitService.deleteSupplier(id);
    }

    @GetMapping("/suppliers/search")
    public List<Supplier> searchSuppliers(@RequestParam String name) {
        return fruitService.searchSuppliers(name);
    }

    // Fruit
    @GetMapping("/fruits")
    public List<Fruit> getAllFruits() {
        return fruitService.getAllFruits();
    }

    @PostMapping("/fruits")
    public Fruit createFruit(@RequestBody Fruit fruit) {
        return fruitService.saveFruit(fruit);
    }

    @DeleteMapping("/fruits/{id}")
    public void deleteFruit(@PathVariable Long id) {
        fruitService.deleteFruit(id);
    }

    @GetMapping("/fruits/search")
    public List<Fruit> searchFruits(@RequestParam String name) {
        return fruitService.searchFruits(name);
    }

    // Stock In
    @PostMapping("/stock-in")
    public StockIn stockIn(@RequestBody StockIn stockIn) {
        return fruitService.stockIn(stockIn);
    }

    // Sales
    @PostMapping("/sales")
    public Sales sellFruit(@RequestBody Sales sales) {
        return fruitService.sellFruit(sales);
    }

    @GetMapping("/sales")
    public List<Sales> getAllSales() {
        return fruitService.getAllSales();
    }

    // Stats
    @GetMapping("/stats")
    public List<Map<String, Object>> getStats() {
        return fruitService.getSalesStats();
    }
}
