package com.fruit.system.service;

import com.fruit.system.entity.StockIn;
import com.fruit.system.entity.Fruit;
import com.fruit.system.repository.StockInRepository;
import com.fruit.system.repository.FruitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockInService {

    @Autowired
    private StockInRepository stockInRepository;

    @Autowired
    private FruitRepository fruitRepository;

    public List<StockIn> getAllStockIns() {
        return stockInRepository.findAll();
    }

    @Transactional
    public StockIn saveStockIn(StockIn stockIn) {
        if (stockIn.getId() != null) {
            return updateStockIn(stockIn);
        }

        // Increase stock
        Fruit fruit = fruitRepository.findById(stockIn.getFruitId())
                .orElseThrow(() -> new RuntimeException("Fruit not found"));

        fruit.setStock(fruit.getStock() + stockIn.getQuantity());
        fruitRepository.save(fruit);

        return stockInRepository.save(stockIn);
    }

    @Transactional
    public StockIn updateStockIn(StockIn stockIn) {
        StockIn oldStockIn = stockInRepository.findById(stockIn.getId())
                .orElseThrow(() -> new RuntimeException("StockIn record not found"));

        Fruit fruit = fruitRepository.findById(stockIn.getFruitId())
                .orElseThrow(() -> new RuntimeException("Fruit not found"));

        // Revert old quantity
        fruit.setStock(fruit.getStock() - oldStockIn.getQuantity());

        // Apply new quantity
        fruit.setStock(fruit.getStock() + stockIn.getQuantity());
        fruitRepository.save(fruit);

        return stockInRepository.save(stockIn);
    }

    @Transactional
    public void deleteStockIn(Long id) {
        StockIn stockIn = stockInRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("StockIn record not found"));

        Fruit fruit = fruitRepository.findById(stockIn.getFruitId())
                .orElseThrow(() -> new RuntimeException("Fruit not found"));

        // Revert stock
        fruit.setStock(fruit.getStock() - stockIn.getQuantity());
        fruitRepository.save(fruit);

        stockInRepository.deleteById(id);
    }
}
