package com.financialrecommendations.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRegistrationDto {
    
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
    
    @NotBlank
    @Size(max = 100)
    private String firstName;
    
    @NotBlank
    @Size(max = 100)
    private String lastName;
    
    private LocalDate dateOfBirth;
    private String phone;
}
