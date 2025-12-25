package com.fruit.system.service;

import com.fruit.system.entity.StockIn;
import com.fruit.system.repository.StockInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockInService {

    @Autowired
    private StockInRepository stockInRepository;

    public List<StockIn> getAllStockIns() {
        return stockInRepository.findAll();
    }

    @Transactional
    public StockIn saveStockIn(StockIn stockIn) {
        // Logic to increase fruit stock could go here
        return stockInRepository.save(stockIn);
    }

    public void deleteStockIn(Long id) {
        stockInRepository.deleteById(id);
    }
}
