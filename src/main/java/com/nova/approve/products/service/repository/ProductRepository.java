package com.nova.approve.products.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nova.approve.products.service.model.Product;

public interface ProductRepository extends JpaRepository<Product,Long>,JpaSpecificationExecutor<Product> {
}