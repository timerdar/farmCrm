package com.timerdar.farmCrm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductChangeRequest {
    private long id;
    private double cost;
    private int createdCount;
}
