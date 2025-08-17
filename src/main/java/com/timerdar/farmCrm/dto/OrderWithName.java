package com.timerdar.farmCrm.dto;

import com.timerdar.farmCrm.model.Order;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderWithName extends Order {

    private String name;

    public OrderWithName(Order order, String name){
        setName(name);
        setId(order.getId());
        setCost(order.getCost());
        setCount(order.getCount());
        setStatus(order.getStatus());
        setWeight(order.getWeight());
        setConsumerId(order.getConsumerId());
        setProductId(order.getProductId());
        setCreatedAt(order.getCreatedAt());
    }

}
