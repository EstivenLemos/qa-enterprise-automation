package com.smartstore.backend.repositories;

import com.smartstore.backend.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {
}