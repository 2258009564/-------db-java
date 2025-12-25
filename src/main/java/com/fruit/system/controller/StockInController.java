package com.fruit.system.controller;

import com.fruit.system.entity.StockIn;
import com.fruit.system.service.StockInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stockin")
@CrossOrigin(origins = "*")
public class StockInController {

    @Autowired
    private StockInService stockInService;

    @GetMapping
    public List<StockIn> getAllStockIns() {
        return stockInService.getAllStockIns();
    }

    @PostMapping
    public StockIn createStockIn(@RequestBody StockIn stockIn) {
        return stockInService.saveStockIn(stockIn);
    }

    @PutMapping
    public StockIn updateStockIn(@RequestBody StockIn stockIn) {
        return stockInService.saveStockIn(stockIn);
    }

    @DeleteMapping("/{id}")
    public void deleteStockIn(@PathVariable Long id) {
        stockInService.deleteStockIn(id);
    }
}
