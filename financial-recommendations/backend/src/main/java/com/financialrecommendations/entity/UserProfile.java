package com.financialrecommendations.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "risk_tolerance", nullable = false)
    private RiskTolerance riskTolerance;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "investment_experience", nullable = false)
    private InvestmentExperience investmentExperience;
    
    @NotNull
    @Column(name = "investment_horizon", nullable = false)
    private Integer investmentHorizon; // in years
    
    @Column(name = "monthly_income", precision = 15, scale = 2)
    private BigDecimal monthlyIncome;
    
    @Column(name = "monthly_expenses", precision = 15, scale = 2)
    private BigDecimal monthlyExpenses;
    
    @Column(name = "current_savings", precision = 15, scale = 2)
    private BigDecimal currentSavings;
    
    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_investment_goals", joinColumns = @JoinColumn(name = "user_profile_id"))
    @Column(name = "goal")
    private List<InvestmentGoal> investmentGoals;
    
    private Integer age;
    
    @Column(name = "employment_status", length = 50)
    private String employmentStatus;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum RiskTolerance {
        CONSERVATIVE, MODERATE, AGGRESSIVE
    }
    
    public enum InvestmentExperience {
        BEGINNER, INTERMEDIATE, ADVANCED
    }
    
    public enum InvestmentGoal {
        RETIREMENT, WEALTH_BUILDING, EMERGENCY_FUND, HOME_PURCHASE, EDUCATION, EARLY_RETIREMENT
    }
}
