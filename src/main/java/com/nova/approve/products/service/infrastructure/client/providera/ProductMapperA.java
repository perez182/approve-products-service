package com.nova.approve.products.service.infrastructure.client.providera;

import org.springframework.stereotype.Component;

import com.nova.approve.products.service.model.Product;

@Component
public class ProductMapperA {

    public Product fromProviderA(ProductADTO dto) {

        Product product = new Product();
        product.setId(null); 

        product.setName(dto.title());
        product.setPrice(dto.price());

        product.setRating(dto.rating() != null ? dto.rating().rate() : null);

        product.setStock(0);

        product.setProvider("providerA");
        product.setCategory(dto.category());
        product.setBrand(null);

        return product;
    }
    
}
