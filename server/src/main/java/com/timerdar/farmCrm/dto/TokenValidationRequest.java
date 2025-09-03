package com.timerdar.farmCrm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TokenValidationRequest {
    private String token;
    private String login;
}
