package com.financialrecommendations.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "recommendations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation_type", nullable = false)
    private RecommendationType recommendationType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecommendationStatus status = RecommendationStatus.PENDING;
    
    @Column(name = "confidence_score", precision = 3, scale = 2)
    private BigDecimal confidenceScore;
    
    @NotBlank
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String reasoning;
    
    @Column(name = "expected_return", precision = 5, scale = 2)
    private BigDecimal expectedReturn;
    
    @Column(name = "risk_assessment", columnDefinition = "TEXT")
    private String riskAssessment;
    
    @Column(name = "time_horizon")
    private Integer timeHorizon; // in days
    
    private Integer priority = 1;
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "recommendation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RecommendationInstrument> recommendedInstruments;
    
    @OneToMany(mappedBy = "recommendation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> relatedTransactions;
    
    public enum RecommendationType {
        BUY, SELL, HOLD, REBALANCE
    }
    
    public enum RecommendationStatus {
        PENDING, ACCEPTED, REJECTED, EXPIRED
    }
}
