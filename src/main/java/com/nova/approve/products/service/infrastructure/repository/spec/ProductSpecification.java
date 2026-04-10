package com.nova.approve.products.service.infrastructure.repository.spec;
import org.springframework.data.jpa.domain.Specification;

import com.nova.approve.products.service.model.Product;

public class ProductSpecification {

    public static Specification<Product> provider(String provider) {
        return (root, query, cb) ->
                provider == null ? null : cb.equal(root.get("provider"), provider);
    }

    public static Specification<Product> minRating(Double minRating) {
        return (root, query, cb) ->
                minRating == null ? null : cb.greaterThanOrEqualTo(root.get("rating"), minRating);
    }

    public static Specification<Product> maxPrice(Double maxPrice) {
        return (root, query, cb) ->
                maxPrice == null ? null : cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Product> minStock(Integer minStock) {
        return (root, query, cb) ->
                minStock == null ? null : cb.greaterThanOrEqualTo(root.get("stock"), minStock);
    }
}