package com.timerdar.farmCrm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderChangeRequest {

    private long id;
    private double cost;
    private double amount;
    private double weight;
    private String status;

}
