package com.nova.approve.products.service.controller;


import org.springframework.web.bind.annotation.*;

import com.nova.approve.products.service.client.providera.ProductADTO;
import com.nova.approve.products.service.client.providerb.ProductBDTO;
import com.nova.approve.products.service.service.InventoryService;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductsController {

    private final InventoryService inventoryService;


    @GetMapping("/a")
    public List<ProductADTO> getProviderA() {
        return inventoryService.getProductsFromProviderA();
    }

    @GetMapping("/b")
    public List<ProductBDTO> getProviderB() {
        return inventoryService.getProductsFromProviderB();
    }


    @GetMapping("/all")
    public Map<String, Object> getAll() {
        return Map.of(
            "providerA", inventoryService.getProductsFromProviderA(),
            "providerB", inventoryService.getProductsFromProviderB()
        );
    }
}