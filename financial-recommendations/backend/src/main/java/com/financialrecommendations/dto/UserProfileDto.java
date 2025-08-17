package com.financialrecommendations.dto;

import com.financialrecommendations.entity.UserProfile;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UserProfileDto {
    
    private UserProfile.RiskTolerance riskTolerance;
    private UserProfile.InvestmentExperience investmentExperience;
    private Integer investmentHorizon;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpenses;
    private BigDecimal currentSavings;
    private List<UserProfile.InvestmentGoal> investmentGoals;
    private Integer age;
    private String employmentStatus;
}
