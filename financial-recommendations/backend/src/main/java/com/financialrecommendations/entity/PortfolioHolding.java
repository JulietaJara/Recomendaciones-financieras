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

@Entity
@Table(name = "portfolio_holdings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioHolding {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrument_id", nullable = false)
    private FinancialInstrument instrument;
    
    @NotNull
    @Column(precision = 15, scale = 6, nullable = false)
    private BigDecimal quantity;
    
    @NotNull
    @Column(name = "average_cost", precision = 15, scale = 4, nullable = false)
    private BigDecimal averageCost;
    
    @Column(name = "current_price", precision = 15, scale = 4)
    private BigDecimal currentPrice;
    
    @Column(name = "market_value", precision = 15, scale = 2)
    private BigDecimal marketValue;
    
    @Column(name = "unrealized_gain_loss", precision = 15, scale = 2)
    private BigDecimal unrealizedGainLoss;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
