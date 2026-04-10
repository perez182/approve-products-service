package com.nova.approve.products.service.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nova.approve.products.service.model.Product;



public interface ProductRepository extends JpaRepository<Product,Long>,JpaSpecificationExecutor<Product> {
    @Modifying
    @Query("UPDATE Product p SET p.stock = :stock WHERE p.stock = 0")
    int restockZeroProducts(@Param("stock") Integer stock);

    @Modifying
    @Query("UPDATE Product p SET p.stock = 0")
    int resetAllStockToZero();

}