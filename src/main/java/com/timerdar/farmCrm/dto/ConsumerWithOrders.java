package com.timerdar.farmCrm.dto;

import com.timerdar.farmCrm.model.Consumer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ConsumerWithOrders extends Consumer {
    private List<OrderWithName> orders;

    public ConsumerWithOrders(Consumer consumer, List<OrderWithName> orders){
        super(consumer.getId(), consumer.getName(), consumer.getAddress(), consumer.getPhone(), consumer.getTotalSum());
        this.orders = orders;
    }
}
