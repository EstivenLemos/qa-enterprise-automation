package com.smartstore.backend.repositories;

import com.smartstore.backend.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository
        extends JpaRepository<Product, Long> {
}