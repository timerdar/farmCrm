package com.timerdar.farmCrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateConsumerRequest {
    private String name;
    private String address;
    private String phone;

    @JsonIgnore
    public void check(){
        if (name.isEmpty() || address.isEmpty() || phone.isEmpty()){
            throw new IllegalArgumentException("Заполнены не все поля");
        }
    }
}
