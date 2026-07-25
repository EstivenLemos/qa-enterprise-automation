package com.smartstore.backend.repositories;

import com.smartstore.backend.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository
        extends JpaRepository<Order, Long> {
}