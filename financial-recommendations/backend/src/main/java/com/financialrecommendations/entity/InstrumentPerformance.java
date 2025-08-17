package com.financialrecommendations.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "instrument_performance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentPerformance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrument_id", nullable = false)
    private FinancialInstrument instrument;
    
    @NotNull
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(name = "open_price", precision = 15, scale = 4)
    private BigDecimal openPrice;
    
    @Column(name = "close_price", precision = 15, scale = 4)
    private BigDecimal closePrice;
    
    @Column(name = "high_price", precision = 15, scale = 4)
    private BigDecimal highPrice;
    
    @Column(name = "low_price", precision = 15, scale = 4)
    private BigDecimal lowPrice;
    
    private Long volume;
    
    @Column(name = "adjusted_close", precision = 15, scale = 4)
    private BigDecimal adjustedClose;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
