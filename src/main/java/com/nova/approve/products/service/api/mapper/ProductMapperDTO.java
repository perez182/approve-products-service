package com.nova.approve.products.service.api.mapper;

import org.springframework.stereotype.Component;

import com.nova.approve.products.service.api.dto.ProductResponseDTO;
import com.nova.approve.products.service.model.Product;

@Component
public class ProductMapperDTO {
     public ProductResponseDTO toResponse(Product p) {
            return new ProductResponseDTO(
                    p.getId(),
                    p.getExternalId(),
                    p.getName(),
                    p.getPrice(),
                    p.getRating(),
                    p.getStock(),
                    p.getProvider(),
                    p.getCategory(),
                    p.getBrand(),
                    p.getImage()
            );
        }
}
