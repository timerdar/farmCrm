package com.timerdar.farmCrm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long productId;
    private long consumerId;
    private int amount;
    private double cost;
    private double weight;
    private OrderStatus status;
    private LocalDate createdAt;
}
