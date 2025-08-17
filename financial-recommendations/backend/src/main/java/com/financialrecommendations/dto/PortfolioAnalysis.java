package com.financialrecommendations.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioAnalysis {
    
    private BigDecimal totalValue;
    private Map<String, BigDecimal> sectorAllocation;
    private Map<String, BigDecimal> assetAllocation;
    private BigDecimal overallRiskLevel;
    private String dominantSector;
    private BigDecimal maxSectorConcentration;
    private BigDecimal internationalExposure;
    private BigDecimal diversificationScore;
    private Set<String> overweightedPositions;
    
    public boolean isOverweighted(String symbol) {
        return overweightedPositions != null && overweightedPositions.contains(symbol);
    }
}
