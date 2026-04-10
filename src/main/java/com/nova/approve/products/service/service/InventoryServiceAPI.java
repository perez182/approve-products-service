package com.nova.approve.products.service.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nova.approve.products.service.infrastructure.repository.ProductRepository;
import com.nova.approve.products.service.infrastructure.repository.spec.ProductSpecification;
import com.nova.approve.products.service.model.Product;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class InventoryServiceAPI {
    private final ProductRepository productRepository;

    public List<Product> search(String provider,Double minRating,Double maxPrice,Integer minStock) 
    {
       Specification<Product> spec = Specification.allOf(
                ProductSpecification.provider(provider),
                ProductSpecification.minRating(minRating),
                ProductSpecification.maxPrice(maxPrice),
                ProductSpecification.minStock(minStock)
                );

        return productRepository.findAll(spec);
    }

    
    @Transactional
    public int restockZeros(Integer newStock) {
       int updated = productRepository.restockZeroProducts(newStock);
       log.info("event=restock_zeros updated_products={} new_stock={}", updated, newStock);
       return updated;
    }

    @Transactional
    public int resetStockZero() {
       int updated = productRepository.resetAllStockToZero();
       log.info("event=reset_stock_zero updated_products={} ", updated);
       return updated;
    }
}
