package com.timerdar.farmCrm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
public class DeliveryOrderNumForConsumer {
    private int consumerId;
    private int deliveryOrderNumber;

    @Override
    public String toString() {
        return "{consumerId: " + consumerId + ", num: " + deliveryOrderNumber + "}";
    }
}
