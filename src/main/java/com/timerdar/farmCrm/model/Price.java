package com.timerdar.farmCrm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Price {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private String productName;
    private int price;
    private boolean isWeighed;

    @JsonIgnore
    public boolean isValid(){
        return price > 0 && productName != null && !productName.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Price price1 = (Price) o;
        return id == price1.id && price == price1.price && isWeighed == price1.isWeighed && Objects.equals(productName, price1.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, price, isWeighed);
    }
}
