package com.fruit.system.service;

import com.fruit.system.entity.Sales;
import com.fruit.system.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SalesService {

    @Autowired
    private SalesRepository salesRepository;

    public List<Sales> getAllSales() {
        return salesRepository.findAll();
    }

    @Transactional
    public Sales saveSales(Sales sales) {
        // Here you might want to add logic to decrease fruit stock
        return salesRepository.save(sales);
    }

    public void deleteSales(Long id) {
        salesRepository.deleteById(id);
    }

    public List<Map<String, Object>> getSalesStats() {
        return salesRepository.getSalesStats();
    }

    public List<Map<String, Object>> getSalesStatsByEmployee() {
        return salesRepository.getSalesStatsByEmployee();
    }
}
