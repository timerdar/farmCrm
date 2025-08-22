package com.timerdar.farmCrm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.timerdar.farmCrm.dto.ShortProductInfo;
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
    private int cost;
    private boolean isWeighed;
    private int createdCount;

    @JsonIgnore
    public boolean isValid(){
        return cost > 0 && name != null && !name.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product1 = (Product) o;
        return id == product1.id && cost == product1.cost && isWeighed == product1.isWeighed && Objects.equals(name, product1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cost, isWeighed);
    }

    @JsonIgnore
    public ShortProductInfo toShort(){
        return new ShortProductInfo(this.getId(), this.getName());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Product{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", cost=").append(cost);
        sb.append(", isWeighed=").append(isWeighed);
        sb.append(", createdCount=").append(createdCount);
        sb.append('}');
        return sb.toString();
    }
}
