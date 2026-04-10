package com.nova.approve.products.service.api.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nova.approve.products.service.api.dto.ProductResponseDTO;
import com.nova.approve.products.service.api.dto.RestockResponseDTO;
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

    private final InventoryServiceAPI serviceapi;
    private final ProductMapperDTO mapper;

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getInventory(
            @RequestParam(required = false) String provider,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minStock
    ) 
    {
        var response = serviceapi.search(provider, minRating, maxPrice, minStock)
        .stream()
        .map(mapper::toResponse)
        .toList();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/restock-zeros")
    public ResponseEntity<RestockResponseDTO> restockZeros(@RequestBody Integer stock) {
        int updated = serviceapi.restockZeros(stock);
        RestockResponseDTO response = new RestockResponseDTO(updated, stock);
        return ResponseEntity.ok(response);
    }


    @PatchMapping("/reset-stock")
    public ResponseEntity<RestockResponseDTO> resetSock() {
        int updated = serviceapi.resetStockZero();
        RestockResponseDTO response = new RestockResponseDTO(updated, 0);
        return ResponseEntity.ok(response);
    }
}