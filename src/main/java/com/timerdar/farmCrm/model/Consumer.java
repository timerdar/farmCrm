package com.timerdar.farmCrm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    public boolean isValid(){
        return isPresent(name) && isPresent(deliveryAddress) && isPresent(district) && isPresent(phoneNumber);
    }

    @JsonIgnore
    private boolean isPresent(String value){
        return value != null && !value.isEmpty();
    }
}
