package com.timerdar.farmCrm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConsumerChangeRequest {
    private long id;
    private String phone;
    private String address;
}
