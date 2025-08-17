package com.timerdar.farmCrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class CreateOrderRequest {
    private long productId;
    private long consumerId;
    private int amount;

    @JsonIgnore
    public boolean isFullyEntered(){
        return (amount != 0);
    }
}
