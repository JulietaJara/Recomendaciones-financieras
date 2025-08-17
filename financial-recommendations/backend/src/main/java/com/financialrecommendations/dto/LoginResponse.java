package com.financialrecommendations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String tokenType = "Bearer";
    
    public LoginResponse(String accessToken, Long userId, String email, String firstName, String lastName) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
