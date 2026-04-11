package com.nova.approve.products.service.service.scheduler;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nova.approve.products.service.infrastructure.client.ProductProviderSync;
import com.nova.approve.products.service.infrastructure.repository.ProductRepository;
import com.nova.approve.products.service.model.Product;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class InventorySyncScheduler {

    private final List<ProductProviderSync> providers;
    private final ProductRepository productRepository;

    @Scheduled(fixedDelay = 200000)
    public void syncInventory() {
        log.info("Execute Cron save products");

        try {
            List<Product> products = fetchProductsParallel();

            auditZeroStockProducts(products);

            upsertProducts(products);

            log.info("Sync completed: {}", products.size());

        } catch (Exception e) {
            log.error("Global sync error: {}", e.getMessage());
        }
    }

    /**
    * Executes all provider calls in parallel and aggregates the results.
    * If a provider fails, it does not stop the entire proces (graceful degradation).
    */
    private List<Product> fetchProductsParallel() {
        List<CompletableFuture<List<Product>>> futures = providers.stream()
                .map(provider -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return provider.fetchAndMap();
                    } catch (Exception e) {
                        log.error("Provider failed: {}", e.getMessage());
                        return List.<Product>of();
                    }
                }))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .toList();
    }


    /**
    * synchronizes incomin products with the database using externalId as the unique key.
    * existing records are updated, while new ones are inserted (idempotent upsert).
     */
    private void upsertProducts(List<Product> products) {

            List<String> externalIds = products.stream()
                    .map(Product::getExternalId)
                    .toList();

            List<Product> existingProducts =
                    productRepository.findAllByExternalIdIn(externalIds);

            Map<String, Product> existingMap = existingProducts.stream()
                    .collect(Collectors.toMap(Product::getExternalId, p -> p));

            List<Product> toSave = products.stream()
                    .map(p -> mergeProduct(p, existingMap))
                    .toList();

            productRepository.saveAll(toSave);

            log.info("Upsert completed: {}", toSave.size());
        }

    private Product mergeProduct(Product incoming, Map<String, Product> existingMap) {

            Product existing = existingMap.get(incoming.getExternalId());

            if (existing == null) {
                return incoming; 
            }

            existing.setName(incoming.getName());
            existing.setPrice(incoming.getPrice());
            existing.setRating(incoming.getRating());
            existing.setStock(incoming.getStock());
            existing.setCategory(incoming.getCategory());
            existing.setBrand(incoming.getBrand());
            existing.setImage(incoming.getImage());

            return existing;
        }


    // =========================
    // AUDIT LOG
    // =========================
    private void auditZeroStockProducts(List<Product> products) {
        long zeroStockCount = products.stream()
                .filter(p -> Integer.valueOf(0).equals(p.getStock()))
                .peek(p -> log.warn("product_stock_zero name={} provider={} stock={}",
                        p.getName(),
                        p.getProvider(),
                        p.getStock()))
                .count();

        log.info("Zero stock products count={}", zeroStockCount);
    }
}