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
@Table(name = "financial_instruments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialInstrument {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(unique = true, nullable = false, length = 20)
    private String symbol;
    
    @NotBlank
    @Column(nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "instrument_type", nullable = false)
    private InstrumentType instrumentType;
    
    @Column(length = 100)
    private String sector;
    
    @Column(length = 50)
    private String market;
    
    @Column(length = 3)
    private String currency = "USD";
    
    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskLevel riskLevel;
    
    @Column(name = "expense_ratio", precision = 5, scale = 4)
    private BigDecimal expenseRatio;
    
    @Column(name = "dividend_yield", precision = 5, scale = 4)
    private BigDecimal dividendYield;
    
    @Column(name = "market_cap")
    private Long marketCap;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "instrument", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InstrumentPerformance> performanceHistory;
    
    @OneToMany(mappedBy = "instrument", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PortfolioHolding> portfolioHoldings;
    
    public enum InstrumentType {
        STOCK, BOND, ETF, MUTUAL_FUND, CRYPTO, COMMODITY
    }
    
    public enum RiskLevel {
        LOW, MEDIUM, HIGH
    }
}
