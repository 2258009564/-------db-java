package com.fruit.system.controller;

import com.fruit.system.entity.Sales;
import com.fruit.system.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "*")
public class SalesController {

    @Autowired
    private SalesService salesService;

    @GetMapping
    public List<Sales> getAllSales() {
        return salesService.getAllSales();
    }

    @PostMapping
    public Sales createSales(@RequestBody Sales sales) {
        return salesService.saveSales(sales);
    }

    @PutMapping
    public Sales updateSales(@RequestBody Sales sales) {
        return salesService.saveSales(sales);
    }

    @DeleteMapping("/{id}")
    public void deleteSales(@PathVariable Long id) {
        salesService.deleteSales(id);
    }

    @GetMapping("/stats/fruit")
    public List<Map<String, Object>> getSalesStatsByFruit() {
        return salesService.getSalesStats();
    }

    @GetMapping("/stats/employee")
    public List<Map<String, Object>> getSalesStatsByEmployee() {
        return salesService.getSalesStatsByEmployee();
    }
}
