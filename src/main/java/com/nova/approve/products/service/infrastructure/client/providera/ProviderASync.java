package com.nova.approve.products.service.infrastructure.client.providera;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nova.approve.products.service.service.InventoryService;
import com.nova.approve.products.service.infrastructure.client.ProductProviderSync;
import com.nova.approve.products.service.model.Product;

@Component
public class ProviderASync implements ProductProviderSync {

    private final InventoryService inventoryService;
    private final ProductMapperA mapperA;

    public ProviderASync(InventoryService inventoryService,
                         ProductMapperA mapperA) {
        this.inventoryService = inventoryService;
        this.mapperA = mapperA;
    }

    @Override
    public List<Product> fetchAndMap() {
        return inventoryService.getProductsFromProviderA()
                .stream()
                .map(mapperA::fromProviderA)
                .toList();
    }
}