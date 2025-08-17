package com.financialrecommendations.service;

import com.financialrecommendations.entity.*;
import com.financialrecommendations.dto.PortfolioAnalysis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortfolioAnalysisService {
    
    public PortfolioAnalysis analyzePortfolios(List<Portfolio> portfolios, UserProfile profile) {
        PortfolioAnalysis analysis = new PortfolioAnalysis();
        
        if (portfolios.isEmpty()) {
            return getEmptyPortfolioAnalysis();
        }
        
        // Calculate total portfolio value
        BigDecimal totalValue = portfolios.stream()
            .map(Portfolio::getTotalValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        analysis.setTotalValue(totalValue);
        
        // Analyze all holdings across portfolios
        List<PortfolioHolding> allHoldings = portfolios.stream()
            .flatMap(p -> p.getHoldings().stream())
            .collect(Collectors.toList());
        
        // Calculate sector allocation
        Map<String, BigDecimal> sectorAllocation = calculateSectorAllocation(allHoldings, totalValue);
        analysis.setSectorAllocation(sectorAllocation);
        
        // Calculate asset class allocation
        Map<String, BigDecimal> assetAllocation = calculateAssetAllocation(allHoldings, totalValue);
        analysis.setAssetAllocation(assetAllocation);
        
        // Calculate risk metrics
        analysis.setOverallRiskLevel(calculateOverallRiskLevel(allHoldings, totalValue));
        
        // Find dominant sector and concentration
        String dominantSector = sectorAllocation.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("Unknown");
        
        BigDecimal maxConcentration = sectorAllocation.values().stream()
            .max(BigDecimal::compareTo)
            .orElse(BigDecimal.ZERO);
        
        analysis.setDominantSector(dominantSector);
        analysis.setMaxSectorConcentration(maxConcentration);
        
        // Calculate international exposure
        analysis.setInternationalExposure(calculateInternationalExposure(allHoldings, totalValue));
        
        // Calculate diversification score
        analysis.setDiversificationScore(calculateDiversificationScore(sectorAllocation, assetAllocation));
        
        // Identify overweighted positions
        analysis.setOverweightedPositions(identifyOverweightedPositions(allHoldings, totalValue));
        
        return analysis;
    }
    
    private PortfolioAnalysis getEmptyPortfolioAnalysis() {
        PortfolioAnalysis analysis = new PortfolioAnalysis();
        analysis.setTotalValue(BigDecimal.ZERO);
        analysis.setSectorAllocation(new HashMap<>());
        analysis.setAssetAllocation(new HashMap<>());
        analysis.setOverallRiskLevel(BigDecimal.ZERO);
        analysis.setDominantSector("None");
        analysis.setMaxSectorConcentration(BigDecimal.ZERO);
        analysis.setInternationalExposure(BigDecimal.ZERO);
        analysis.setDiversificationScore(BigDecimal.ZERO);
        analysis.setOverweightedPositions(new HashSet<>());
        return analysis;
    }
    
    private Map<String, BigDecimal> calculateSectorAllocation(List<PortfolioHolding> holdings, BigDecimal totalValue) {
        Map<String, BigDecimal> sectorAllocation = new HashMap<>();
        
        for (PortfolioHolding holding : holdings) {
            String sector = holding.getInstrument().getSector();
            if (sector == null) sector = "Other";
            
            BigDecimal allocation = holding.getMarketValue().divide(totalValue, 4, RoundingMode.HALF_UP);
            sectorAllocation.merge(sector, allocation, BigDecimal::add);
        }
        
        return sectorAllocation;
    }
    
    private Map<String, BigDecimal> calculateAssetAllocation(List<PortfolioHolding> holdings, BigDecimal totalValue) {
        Map<String, BigDecimal> assetAllocation = new HashMap<>();
        
        for (PortfolioHolding holding : holdings) {
            String assetClass = mapInstrumentTypeToAssetClass(holding.getInstrument().getInstrumentType());
            BigDecimal allocation = holding.getMarketValue().divide(totalValue, 4, RoundingMode.HALF_UP);
            assetAllocation.merge(assetClass, allocation, BigDecimal::add);
        }
        
        return assetAllocation;
    }
    
    private String mapInstrumentTypeToAssetClass(FinancialInstrument.InstrumentType type) {
        return switch (type) {
            case STOCK -> "STOCKS";
            case BOND -> "BONDS";
            case ETF -> "ETF";
            case MUTUAL_FUND -> "MUTUAL_FUNDS";
            case CRYPTO -> "CRYPTO";
            case COMMODITY -> "COMMODITIES";
        };
    }
    
    private BigDecimal calculateOverallRiskLevel(List<PortfolioHolding> holdings, BigDecimal totalValue) {
        BigDecimal weightedRisk = BigDecimal.ZERO;
        
        for (PortfolioHolding holding : holdings) {
            BigDecimal weight = holding.getMarketValue().divide(totalValue, 4, RoundingMode.HALF_UP);
            BigDecimal riskValue = getRiskValue(holding.getInstrument().getRiskLevel());
            weightedRisk = weightedRisk.add(weight.multiply(riskValue));
        }
        
        return weightedRisk;
    }
    
    private BigDecimal getRiskValue(FinancialInstrument.RiskLevel riskLevel) {
        return switch (riskLevel) {
            case LOW -> BigDecimal.valueOf(0.3);
            case MEDIUM -> BigDecimal.valueOf(0.6);
            case HIGH -> BigDecimal.valueOf(0.9);
        };
    }
    
    private BigDecimal calculateInternationalExposure(List<PortfolioHolding> holdings, BigDecimal totalValue) {
        BigDecimal internationalValue = BigDecimal.ZERO;
        
        for (PortfolioHolding holding : holdings) {
            // Simple heuristic: instruments with "International", "Emerging", or specific international symbols
            String symbol = holding.getInstrument().getSymbol();
            String sector = holding.getInstrument().getSector();
            
            if (isInternationalInstrument(symbol, sector)) {
                internationalValue = internationalValue.add(holding.getMarketValue());
            }
        }
        
        return totalValue.compareTo(BigDecimal.ZERO) > 0 ? 
            internationalValue.divide(totalValue, 4, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }
    
    private boolean isInternationalInstrument(String symbol, String sector) {
        return symbol.contains("VEA") || symbol.contains("VWO") || symbol.contains("EFA") || 
               symbol.contains("EEM") || "International".equals(sector) || "Emerging Markets".equals(sector);
    }
    
    private BigDecimal calculateDiversificationScore(Map<String, BigDecimal> sectorAllocation, 
                                                   Map<String, BigDecimal> assetAllocation) {
        // Calculate Herfindahl-Hirschman Index for diversification
        BigDecimal sectorHHI = sectorAllocation.values().stream()
            .map(allocation -> allocation.multiply(allocation))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal assetHHI = assetAllocation.values().stream()
            .map(allocation -> allocation.multiply(allocation))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Convert to diversification score (1 - HHI), average of sector and asset diversification
        BigDecimal sectorDiversification = BigDecimal.ONE.subtract(sectorHHI);
        BigDecimal assetDiversification = BigDecimal.ONE.subtract(assetHHI);
        
        return sectorDiversification.add(assetDiversification).divide(BigDecimal.valueOf(2), 4, RoundingMode.HALF_UP);
    }
    
    private Set<String> identifyOverweightedPositions(List<PortfolioHolding> holdings, BigDecimal totalValue) {
        Set<String> overweighted = new HashSet<>();
        BigDecimal threshold = BigDecimal.valueOf(0.15); // 15% threshold
        
        for (PortfolioHolding holding : holdings) {
            BigDecimal weight = holding.getMarketValue().divide(totalValue, 4, RoundingMode.HALF_UP);
            if (weight.compareTo(threshold) > 0) {
                overweighted.add(holding.getInstrument().getSymbol());
            }
        }
        
        return overweighted;
    }
}
