package com.nova.approve.products.service.infrastructure.client.providerb;

import org.springframework.stereotype.Component;

import com.nova.approve.products.service.model.Product;

@Component
public class ProductMapperB {
    public Product fromProviderB(ProductBDTO dto) {

        Product product = new Product();

        product.setId(null); 

        product.setName(dto.title());
        product.setPrice(dto.price());

        product.setRating(dto.rating());

        product.setStock(dto.stock());

        product.setProvider("providerB");

        product.setCategory(dto.category());

        product.setBrand(null);

        return product;
    }
}
