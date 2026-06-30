package com.smartstore.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Order {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private BigDecimal total;

    private String status;

    private LocalDateTime createdAt;

    @ManyToOne

    @JoinColumn(name = "user_id")

    private User user;

}