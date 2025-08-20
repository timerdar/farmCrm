package com.timerdar.farmCrm.dto;

import com.timerdar.farmCrm.model.Order;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderWithNameAndWeightable extends Order {

    private String name;
    private boolean weighed;

    public OrderWithNameAndWeightable(Order order, String name, boolean weighed){
        setName(name);
        setWeighed(weighed);
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
