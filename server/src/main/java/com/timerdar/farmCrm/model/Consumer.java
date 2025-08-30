package com.timerdar.farmCrm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Table(name = "consumers")
public class Consumer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String phone;
    private int totalSum;
    @Column(nullable = true)
    private int deliveryOrderNumber;

    @JsonIgnore
    public boolean isValid(){
        return isPresent(name) && isPresent(address)  && isPresent(phone);
    }

    @JsonIgnore
    private boolean isPresent(String value){
        return value != null && !value.isEmpty();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Consumer{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", totalSum=").append(totalSum);
        sb.append('}');
        return sb.toString();
    }

    public Consumer(long id, String name, String address, String phone, int totalSum) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.totalSum = totalSum;
    }
}
