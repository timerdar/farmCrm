package com.timerdar.farmCrm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

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
    private int count;
    private int cost;
    private double weight;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private LocalDate createdAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && productId == order.productId && consumerId == order.consumerId && count == order.count && Double.compare(cost, order.cost) == 0 && Double.compare(weight, order.weight) == 0 && status == order.status && Objects.equals(createdAt, order.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, consumerId, count, cost, weight, status, createdAt);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("id=").append(id);
        sb.append(", productId=").append(productId);
        sb.append(", consumerId=").append(consumerId);
        sb.append(", count=").append(count);
        sb.append(", cost=").append(cost);
        sb.append(", weight=").append(weight);
        sb.append(", status=").append(status);
        sb.append(", createdAt=").append(createdAt);
        sb.append('}');
        return sb.toString();
    }
}
