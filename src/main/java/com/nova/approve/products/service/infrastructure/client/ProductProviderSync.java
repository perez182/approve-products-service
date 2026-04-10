package com.nova.approve.products.service.infrastructure.client;

import java.util.List;

import com.nova.approve.products.service.model.Product;

public interface ProductProviderSync {

    List<Product> fetchAndMap();

}