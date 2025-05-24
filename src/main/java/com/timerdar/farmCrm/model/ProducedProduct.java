package com.timerdar.farmCrm.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ProducedProduct {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;
    private int amount;
    private LocalDate producedAt;

}
