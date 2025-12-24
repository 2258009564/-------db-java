package com.fruit.system.service;

import com.fruit.system.entity.*;
import com.fruit.system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 业务逻辑层 (Service Layer)
 * 处理核心业务逻辑，如库存管理、销售计算等
 * 调用 Repository 层进行数据持久化
 */
@Service // Spring 注解：标识这是一个 Service 类，会被 Spring 容器管理
public class FruitService {

    // 注入各个 Repository
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

    // --- 客户管理 (Customer Management) ---

    /**
     * 获取所有客户
     */
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * 保存或更新客户信息
     * 如果 ID 存在则更新，不存在则新增
     */
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    /**
     * 根据ID删除客户
     */
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    /**
     * 搜索客户
     */
    public List<Customer> searchCustomers(String keyword) {
        return customerRepository.findByNameContainingOrPhoneContaining(keyword, keyword);
    }

    // --- 员工管理 (Employee Management) ---

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

    // --- 供应商管理 (Supplier Management) ---

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

    // --- 商品管理 (Fruit Management) ---

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

    // --- 入库业务 (Stock In Business) ---

    /**
     * 处理入库业务
     * 1. 查询商品是否存在
     * 2. 增加商品库存
     * 3. 保存入库记录
     * 
     * @Transactional: 保证事务一致性，要么全部成功，要么全部回滚
     */
    @Transactional
    public StockIn stockIn(StockIn stockIn) {
        // 1. 查找商品
        Fruit fruit = fruitRepository.findById(stockIn.getFruitId())
                .orElseThrow(() -> new RuntimeException("Fruit not found"));

        // 2. 更新库存 (原库存 + 入库数量)
        fruit.setStock(fruit.getStock() + stockIn.getQuantity());
        fruitRepository.save(fruit);

        // 3. 保存入库记录
        return stockInRepository.save(stockIn);
    }

    // --- 销售业务 (Sales Business) ---

    /**
     * 处理销售业务
     * 1. 检查库存是否充足
     * 2. 扣减库存
     * 3. 计算总价
     * 4. 保存销售记录
     */
    @Transactional
    public Sales sellFruit(Sales sales) {
        // 1. 查找商品
        Fruit fruit = fruitRepository.findById(sales.getFruitId())
                .orElseThrow(() -> new RuntimeException("Fruit not found"));

        // 2. 检查库存
        if (fruit.getStock() < sales.getQuantity()) {
            throw new RuntimeException("Insufficient stock"); // 库存不足抛出异常
        }

        // 3. 扣减库存
        fruit.setStock(fruit.getStock() - sales.getQuantity());
        fruitRepository.save(fruit);

        // 4. 计算总价 (单价 * 数量)
        sales.setTotalPrice(fruit.getPrice() * sales.getQuantity());

        // 5. 保存销售记录
        return salesRepository.save(sales);
    }

    public List<Sales> getAllSales() {
        return salesRepository.findAll();
    }

    /**
     * 获取销售统计报表
     */
    public List<Map<String, Object>> getSalesStats() {
        return salesRepository.getSalesStats();
    }
}
