package com.nova.approve.products.service.api.dto;

public record RestockResponseDTO(
        int updatedProducts,
        Integer newStock
) {}