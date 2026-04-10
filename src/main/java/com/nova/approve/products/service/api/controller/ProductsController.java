package com.nova.approve.products.service.api.controller;


import org.springframework.web.bind.annotation.*;

import com.nova.approve.products.service.api.dto.ProductResponseDTO;
import com.nova.approve.products.service.api.mapper.ProductMapperDTO;
import com.nova.approve.products.service.infrastructure.client.providera.ProductADTO;
import com.nova.approve.products.service.infrastructure.client.providerb.ProductBDTO;
import com.nova.approve.products.service.model.Product;
import com.nova.approve.products.service.service.InventoryService;
import com.nova.approve.products.service.service.InventoryServiceAPI;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductsController {

    private final InventoryService inventoryService;
    private final InventoryServiceAPI serviceapi;
    private final ProductMapperDTO mapper;

    @GetMapping
    public List<ProductResponseDTO> getInventory(
            @RequestParam(required = false) String provider,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minStock
    ) 
    {
        return serviceapi.search(provider, minRating, maxPrice, minStock)
        .stream()
        .map(mapper::toResponse)
        .toList();
    }


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