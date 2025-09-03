package com.timerdar.farmCrm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryOrderNumForConsumer {
    private int consumerId;
    private int deliveryOrderNumber;

    @Override
    public String toString() {
        return "{consumerId: " + consumerId + ", num: " + deliveryOrderNumber + "}";
    }
}
