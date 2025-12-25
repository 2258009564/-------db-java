package com.fruit.system.controller;

import com.fruit.system.entity.Fruit;
import com.fruit.system.service.FruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fruits")
@CrossOrigin(origins = "*")
public class FruitController {

    @Autowired
    private FruitService fruitService;

    @GetMapping
    public List<Fruit> getAllFruits() {
        return fruitService.getAllFruits();
    }

    @PostMapping
    public Fruit createFruit(@RequestBody Fruit fruit) {
        return fruitService.saveFruit(fruit);
    }

    @DeleteMapping("/{id}")
    public void deleteFruit(@PathVariable Long id) {
        fruitService.deleteFruit(id);
    }
}
