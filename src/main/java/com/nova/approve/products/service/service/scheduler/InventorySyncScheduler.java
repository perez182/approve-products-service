package com.nova.approve.products.service.service.scheduler;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    @Scheduled(fixedDelay = 600000)
    public void syncInventory() {
        log.info("Execute Cron save products");
        
        try {
            List<CompletableFuture<List<Product>>> futures = providers.stream()
                    .map(provider -> CompletableFuture.supplyAsync(() -> {
                        try {
                            return provider.fetchAndMap();
                        } catch (Exception e) {
                            log.error("Provider failed: " + e.getMessage());
                            return List.<Product>of();
                        }
                    }))
                    .toList();

            List<Product> products = futures.stream()
                    .map(CompletableFuture::join)
                    .flatMap(List::stream)
                    .toList();

            auditZeroStockProducts(products);      
            productRepository.saveAll(products);

            log.info("Sync completed: " + products.size());
        } catch (Exception e) {
            log.error("Global sync error: " + e.getMessage());
        }
    }

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