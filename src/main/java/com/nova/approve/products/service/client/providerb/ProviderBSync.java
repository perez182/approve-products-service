package com.nova.approve.products.service.client.providerb;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nova.approve.products.service.client.ProductProviderSync;
import com.nova.approve.products.service.service.InventoryService;
import com.nova.approve.products.service.model.Product;

@Component
public class ProviderBSync implements ProductProviderSync {

    private final InventoryService inventoryService;
    private final ProductMapperB mapperB;

    public ProviderBSync(InventoryService inventoryService,
                         ProductMapperB mapperB) {
        this.inventoryService = inventoryService;
        this.mapperB = mapperB;
    }

    @Override
    public List<Product> fetchAndMap() {
        return inventoryService.getProductsFromProviderB()
                .stream()
                .map(mapperB::fromProviderB)
                .toList();
    }
}