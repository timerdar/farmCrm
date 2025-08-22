package com.timerdar.farmCrm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
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
}
