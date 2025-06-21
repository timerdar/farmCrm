package com.timerdar.farmCrm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class CreateOrderRequest {
    private long productId;
    private long consumerId;
    private int amount;
    private double weight;

    public boolean isFullyEntered(){
        return productId != 0 && consumerId != 0 && (amount != 0 || weight != 0);
    }
}
