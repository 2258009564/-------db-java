package com.fruit.system.controller;

import com.fruit.system.entity.*;
import com.fruit.system.service.FruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 控制层 (Controller Layer)
 * 负责接收前端的 HTTP 请求，调用 Service 层处理业务，并返回 JSON 数据
 */
@RestController // Spring 注解：标识这是一个 RESTful 风格的控制器，所有方法的返回值都会自动转换为 JSON
@RequestMapping("/api") // Spring 注解：定义该控制器所有接口的公共路径前缀为 /api
@CrossOrigin(origins = "*") // Spring 注解：允许跨域请求 (CORS)，方便前端调试
public class FruitController {

    @Autowired
    private FruitService fruitService;

    // --- 客户管理接口 ---

    /**
     * 获取所有客户列表
     * GET /api/customers
     */
    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return fruitService.getAllCustomers();
    }

    /**
     * 创建或更新客户
     * POST /api/customers
     * 
     * @RequestBody: 将请求体中的 JSON 数据映射为 Customer 对象
     */
    @PostMapping("/customers")
    public Customer createCustomer(@RequestBody Customer customer) {
        return fruitService.saveCustomer(customer);
    }

    /**
     * 删除客户
     * DELETE /api/customers/{id}
     * 
     * @PathVariable: 获取 URL 路径中的 id 参数
     */
    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        fruitService.deleteCustomer(id);
    }

    /**
     * 搜索客户
     * GET /api/customers/search?keyword=xxx
     * 
     * @RequestParam: 获取 URL 查询参数 keyword
     */
    @GetMapping("/customers/search")
    public List<Customer> searchCustomers(@RequestParam String keyword) {
        return fruitService.searchCustomers(keyword);
    }

    // --- 员工管理接口 ---

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

    // --- 供应商管理接口 ---

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

    // --- 商品管理接口 ---

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

    // --- 入库管理接口 ---

    /**
     * 提交入库单
     * POST /api/stock-in
     */
    @PostMapping("/stock-in")
    public StockIn stockIn(@RequestBody StockIn stockIn) {
        return fruitService.stockIn(stockIn);
    }

    // --- 销售管理接口 ---

    /**
     * 提交销售单
     * POST /api/sales
     */
    @PostMapping("/sales")
    public Sales sellFruit(@RequestBody Sales sales) {
        return fruitService.sellFruit(sales);
    }

    /**
     * 获取所有销售记录
     * GET /api/sales
     */
    @GetMapping("/sales")
    public List<Sales> getAllSales() {
        return fruitService.getAllSales();
    }

    // --- 统计接口 ---

    /**
     * 获取销售统计数据
     * GET /api/stats
     */
    @GetMapping("/stats")
    public List<Map<String, Object>> getStats() {
        return fruitService.getSalesStats();
    }
}
