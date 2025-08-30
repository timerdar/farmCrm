package com.timerdar.farmCrm.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {
    private String name;
    private int cost;
    private boolean weightable;

    @JsonIgnore
    public void check(){
        if(name.isEmpty() || cost <= 0)
            throw new IllegalArgumentException("Заполнены не все поля");
    }
}
