package com.nova.approve.products.service.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nova.approve.products.service.infrastructure.repository.ProductRepository;
import com.nova.approve.products.service.infrastructure.repository.spec.ProductSpecification;
import com.nova.approve.products.service.model.Product;

import lombok.AllArgsConstructor;

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
}
