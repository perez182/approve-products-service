package com.nova.approve.products.service.infrastructure.client.providerb;

import java.util.List;

public record ProductsBDTO(
    List<ProductBDTO> products
) {}