package com.fruit.system.service;

import com.fruit.system.entity.Sales;
import com.fruit.system.entity.Fruit;
import com.fruit.system.repository.SalesRepository;
import com.fruit.system.repository.FruitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SalesService {

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private FruitRepository fruitRepository;

    public List<Sales> getAllSales() {
        return salesRepository.findAll();
    }

    @Transactional
    public Sales saveSales(Sales sales) {
        if (sales.getId() != null) {
            return updateSales(sales);
        }

        // Calculate total price
        Fruit fruit = fruitRepository.findById(sales.getFruitId())
                .orElseThrow(() -> new RuntimeException("Fruit not found"));

        if (fruit.getStock() < sales.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // Update stock
        fruit.setStock(fruit.getStock() - sales.getQuantity());
        fruitRepository.save(fruit);

        // Calculate price
        double total = fruit.getPrice() * sales.getQuantity();
        sales.setTotalPrice(total);

        return salesRepository.save(sales);
    }

    @Transactional
    public Sales updateSales(Sales sales) {
        Sales oldSales = salesRepository.findById(sales.getId())
                .orElseThrow(() -> new RuntimeException("Sales record not found"));

        Fruit fruit = fruitRepository.findById(sales.getFruitId())
                .orElseThrow(() -> new RuntimeException("Fruit not found"));

        // Revert old stock deduction
        fruit.setStock(fruit.getStock() + oldSales.getQuantity());

        // Check new stock
        if (fruit.getStock() < sales.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // Apply new stock deduction
        fruit.setStock(fruit.getStock() - sales.getQuantity());
        fruitRepository.save(fruit);

        // Recalculate price
        double total = fruit.getPrice() * sales.getQuantity();
        sales.setTotalPrice(total);

        return salesRepository.save(sales);
    }

    @Transactional
    public void deleteSales(Long id) {
        Sales sales = salesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sales record not found"));

        Fruit fruit = fruitRepository.findById(sales.getFruitId())
                .orElseThrow(() -> new RuntimeException("Fruit not found"));

        // Restore stock
        fruit.setStock(fruit.getStock() + sales.getQuantity());
        fruitRepository.save(fruit);

        salesRepository.deleteById(id);
    }

    public List<Map<String, Object>> getSalesStats() {
        return salesRepository.getSalesStats();
    }

    public List<Map<String, Object>> getSalesStatsByEmployee() {
        return salesRepository.getSalesStatsByEmployee();
    }
}
