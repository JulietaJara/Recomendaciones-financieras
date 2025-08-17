package com.financialrecommendations.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "recommendation_instruments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationInstrument {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_id", nullable = false)
    private Recommendation recommendation;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrument_id", nullable = false)
    private FinancialInstrument instrument;
    
    @NotNull
    @Column(name = "allocation_percentage", precision = 5, scale = 2, nullable = false)
    private BigDecimal allocationPercentage;
    
    @Column(name = "target_amount", precision = 15, scale = 2)
    private BigDecimal targetAmount;
    
    @Column(name = "current_amount", precision = 15, scale = 2)
    private BigDecimal currentAmount = BigDecimal.ZERO;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
