package com.jsserverspac.todoapplication.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponse {
    private String token;
    private String error;
    private String message;
    public AuthResponse(String token, String error, String message) {
        if(!error.isEmpty()) {
            this.token = "";
            this.error = error;
        } else {
            this.token = token;
            this.message = message;
        }
    }
}
