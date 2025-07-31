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
@Table(name = "products")
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private String name;
    private int price;
    private boolean isWeighed;
    private int createdCount;

    @JsonIgnore
    public boolean isValid(){
        return price > 0 && name != null && !name.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product1 = (Product) o;
        return id == product1.id && price == product1.price && isWeighed == product1.isWeighed && Objects.equals(name, product1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, isWeighed);
    }
}
