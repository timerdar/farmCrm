package com.timerdar.farmCrm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Consumer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String name;
    @Column(nullable = false)
    private String deliveryAddress;
    @Column(nullable = false)
    private String district;
    private String phoneNumber;

}
