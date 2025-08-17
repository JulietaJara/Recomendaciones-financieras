package com.financialrecommendations.service;

import com.financialrecommendations.entity.*;
import com.financialrecommendations.dto.PortfolioAnalysis;
import com.financialrecommendations.repository.FinancialInstrumentRepository;
import com.financialrecommendations.repository.PortfolioRepository;
import com.financialrecommendations.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationEngineService {
    
    private final FinancialInstrumentRepository instrumentRepository;
    private final PortfolioRepository portfolioRepository;
    private final RecommendationRepository recommendationRepository;
    private final PortfolioAnalysisService portfolioAnalysisService;
    
    public List<Recommendation> generateRecommendations(User user) {
        log.info("Generating recommendations for user: {}", user.getId());
        
        UserProfile profile = user.getUserProfile();
        List<Portfolio> portfolios = portfolioRepository.findByUserIdWithHoldings(user.getId());
        
        List<Recommendation> recommendations = new ArrayList<>();
        
        // Analyze current portfolio allocation
        PortfolioAnalysis analysis = portfolioAnalysisService.analyzePortfolios(portfolios, profile);
        
        // Generate recommendations based on analysis
        recommendations.addAll(generateDiversificationRecommendations(user, analysis));
        recommendations.addAll(generateRiskAdjustmentRecommendations(user, analysis));
        recommendations.addAll(generateRebalancingRecommendations(user, analysis));
        recommendations.addAll(generateNewInvestmentRecommendations(user, analysis));
        
        // Save and return recommendations
        return recommendationRepository.saveAll(recommendations);
    }
    
    private List<Recommendation> generateDiversificationRecommendations(User user, PortfolioAnalysis analysis) {
        List<Recommendation> recommendations = new ArrayList<>();
        
        // Check sector concentration
        if (analysis.getMaxSectorConcentration().compareTo(BigDecimal.valueOf(0.4)) > 0) {
            String dominantSector = analysis.getDominantSector();
            
            Recommendation diversificationRec = new Recommendation();
            diversificationRec.setUser(user);
            diversificationRec.setRecommendationType(Recommendation.RecommendationType.BUY);
            diversificationRec.setTitle("Diversificar concentración en " + dominantSector);
            diversificationRec.setDescription("Tu cartera tiene una alta concentración en el sector " + dominantSector + 
                " (" + analysis.getMaxSectorConcentration().multiply(BigDecimal.valueOf(100)).setScale(1, RoundingMode.HALF_UP) + "%). " +
                "Recomendamos diversificar hacia otros sectores.");
            diversificationRec.setReasoning("La diversificación sectorial reduce el riesgo específico del sector. " +
                "Una concentración superior al 40% en un solo sector aumenta significativamente el riesgo de la cartera. " +
                "Basado en tu perfil de riesgo " + user.getUserProfile().getRiskTolerance() + 
                ", recomendamos mantener la exposición sectorial por debajo del 30%.");
            diversificationRec.setConfidenceScore(BigDecimal.valueOf(0.85));
            diversificationRec.setPriority(1);
            diversificationRec.setExpectedReturn(BigDecimal.valueOf(7.5));
            diversificationRec.setRiskAssessment("Reducción de riesgo mediante diversificación sectorial");
            diversificationRec.setTimeHorizon(180);
            diversificationRec.setExpiresAt(LocalDateTime.now().plusDays(30));
            
            recommendations.add(diversificationRec);
        }
        
        // Check geographic diversification
        if (analysis.getInternationalExposure().compareTo(BigDecimal.valueOf(0.15)) < 0) {
            Recommendation internationalRec = new Recommendation();
            internationalRec.setUser(user);
            internationalRec.setRecommendationType(Recommendation.RecommendationType.BUY);
            internationalRec.setTitle("Agregar exposición internacional");
            internationalRec.setDescription("Tu cartera tiene baja exposición a mercados internacionales (" + 
                analysis.getInternationalExposure().multiply(BigDecimal.valueOf(100)).setScale(1, RoundingMode.HALF_UP) + "%). " +
                "Considera agregar diversificación geográfica.");
            internationalRec.setReasoning("La diversificación geográfica mejora el perfil riesgo-retorno de la cartera. " +
                "Los mercados internacionales pueden ofrecer oportunidades de crecimiento y reducir la correlación con el mercado doméstico. " +
                "Para tu horizonte de inversión de " + user.getUserProfile().getInvestmentHorizon() + " años, " +
                "recomendamos una exposición internacional del 20-30%.");
            internationalRec.setConfidenceScore(BigDecimal.valueOf(0.78));
            internationalRec.setPriority(2);
            internationalRec.setExpectedReturn(BigDecimal.valueOf(8.2));
            internationalRec.setRiskAssessment("Riesgo medio con beneficios de diversificación geográfica");
            internationalRec.setTimeHorizon(365);
            internationalRec.setExpiresAt(LocalDateTime.now().plusDays(45));
            
            recommendations.add(internationalRec);
        }
        
        return recommendations;
    }
    
    private List<Recommendation> generateRiskAdjustmentRecommendations(User user, PortfolioAnalysis analysis) {
        List<Recommendation> recommendations = new ArrayList<>();
        UserProfile profile = user.getUserProfile();
        
        BigDecimal targetRiskLevel = getTargetRiskLevel(profile);
        BigDecimal currentRiskLevel = analysis.getOverallRiskLevel();
        
        BigDecimal riskDifference = currentRiskLevel.subtract(targetRiskLevel);
        
        if (riskDifference.abs().compareTo(BigDecimal.valueOf(0.15)) > 0) {
            Recommendation riskAdjustment = new Recommendation();
            riskAdjustment.setUser(user);
            
            if (riskDifference.compareTo(BigDecimal.ZERO) > 0) {
                // Portfolio is too risky
                riskAdjustment.setRecommendationType(Recommendation.RecommendationType.REBALANCE);
                riskAdjustment.setTitle("Reducir nivel de riesgo de la cartera");
                riskAdjustment.setDescription("Tu cartera actual tiene un nivel de riesgo superior al recomendado para tu perfil. " +
                    "Considera rebalancear hacia activos más conservadores.");
                riskAdjustment.setReasoning("Basado en tu perfil de riesgo " + profile.getRiskTolerance() + 
                    " y experiencia " + profile.getInvestmentExperience() + ", tu cartera debería tener un nivel de riesgo de " +
                    targetRiskLevel.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP) + "%. " +
                    "Actualmente está en " + currentRiskLevel.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP) + "%. " +
                    "Recomendamos aumentar la asignación a bonos y ETFs de bajo riesgo.");
            } else {
                // Portfolio is too conservative
                riskAdjustment.setRecommendationType(Recommendation.RecommendationType.BUY);
                riskAdjustment.setTitle("Aumentar potencial de crecimiento");
                riskAdjustment.setDescription("Tu cartera es muy conservadora para tu perfil de riesgo. " +
                    "Considera aumentar la exposición a activos de crecimiento.");
                riskAdjustment.setReasoning("Con " + profile.getInvestmentHorizon() + " años de horizonte de inversión " +
                    "y perfil " + profile.getRiskTolerance() + ", puedes permitirte más riesgo para obtener mejores retornos. " +
                    "Tu nivel de riesgo actual (" + currentRiskLevel.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP) + "%) " +
                    "está por debajo del objetivo (" + targetRiskLevel.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP) + "%).");
            }
            
            riskAdjustment.setConfidenceScore(BigDecimal.valueOf(0.82));
            riskAdjustment.setPriority(1);
            riskAdjustment.setExpectedReturn(calculateExpectedReturn(profile, targetRiskLevel));
            riskAdjustment.setRiskAssessment("Ajuste de riesgo alineado con perfil del inversor");
            riskAdjustment.setTimeHorizon(120);
            riskAdjustment.setExpiresAt(LocalDateTime.now().plusDays(21));
            
            recommendations.add(riskAdjustment);
        }
        
        return recommendations;
    }
    
    private List<Recommendation> generateRebalancingRecommendations(User user, PortfolioAnalysis analysis) {
        List<Recommendation> recommendations = new ArrayList<>();
        
        // Check if rebalancing is needed based on target allocation
        Map<String, BigDecimal> targetAllocation = getTargetAllocation(user.getUserProfile());
        Map<String, BigDecimal> currentAllocation = analysis.getAssetAllocation();
        
        boolean needsRebalancing = false;
        StringBuilder rebalancingDetails = new StringBuilder();
        
        for (Map.Entry<String, BigDecimal> target : targetAllocation.entrySet()) {
            String assetClass = target.getKey();
            BigDecimal targetPercent = target.getValue();
            BigDecimal currentPercent = currentAllocation.getOrDefault(assetClass, BigDecimal.ZERO);
            BigDecimal difference = currentPercent.subtract(targetPercent);
            
            if (difference.abs().compareTo(BigDecimal.valueOf(0.1)) > 0) {
                needsRebalancing = true;
                rebalancingDetails.append(assetClass).append(": actual ")
                    .append(currentPercent.multiply(BigDecimal.valueOf(100)).setScale(1, RoundingMode.HALF_UP))
                    .append("%, objetivo ")
                    .append(targetPercent.multiply(BigDecimal.valueOf(100)).setScale(1, RoundingMode.HALF_UP))
                    .append("%. ");
            }
        }
        
        if (needsRebalancing) {
            Recommendation rebalancing = new Recommendation();
            rebalancing.setUser(user);
            rebalancing.setRecommendationType(Recommendation.RecommendationType.REBALANCE);
            rebalancing.setTitle("Rebalancear cartera según asignación objetivo");
            rebalancing.setDescription("Tu cartera se ha desviado de la asignación objetivo. " +
                "Es momento de rebalancear para mantener el perfil de riesgo deseado.");
            rebalancing.setReasoning("El rebalanceo periódico es fundamental para mantener el perfil de riesgo objetivo. " +
                "Desviaciones actuales: " + rebalancingDetails.toString() + 
                "Rebalancear ayuda a 'comprar barato y vender caro' de forma sistemática.");
            rebalancing.setConfidenceScore(BigDecimal.valueOf(0.90));
            rebalancing.setPriority(2);
            rebalancing.setExpectedReturn(BigDecimal.valueOf(6.8));
            rebalancing.setRiskAssessment("Mantenimiento del perfil de riesgo objetivo");
            rebalancing.setTimeHorizon(30);
            rebalancing.setExpiresAt(LocalDateTime.now().plusDays(14));
            
            recommendations.add(rebalancing);
        }
        
        return recommendations;
    }
    
    private List<Recommendation> generateNewInvestmentRecommendations(User user, PortfolioAnalysis analysis) {
        List<Recommendation> recommendations = new ArrayList<>();
        UserProfile profile = user.getUserProfile();
        
        // Calculate available cash for investment
        BigDecimal availableCash = calculateAvailableCash(user);
        
        if (availableCash.compareTo(BigDecimal.valueOf(1000)) > 0) {
            // Get suitable instruments based on user profile
            List<FinancialInstrument> suitableInstruments = getSuitableInstruments(profile, analysis);
            
            if (!suitableInstruments.isEmpty()) {
                FinancialInstrument recommendedInstrument = suitableInstruments.get(0);
                
                Recommendation newInvestment = new Recommendation();
                newInvestment.setUser(user);
                newInvestment.setRecommendationType(Recommendation.RecommendationType.BUY);
                newInvestment.setTitle("Invertir efectivo disponible en " + recommendedInstrument.getName());
                newInvestment.setDescription("Tienes $" + availableCash.setScale(0, RoundingMode.HALF_UP) + 
                    " disponibles para invertir. Recomendamos " + recommendedInstrument.getName() + 
                    " (" + recommendedInstrument.getSymbol() + ") basado en tu perfil.");
                newInvestment.setReasoning("Mantener efectivo sin invertir genera pérdida de poder adquisitivo por inflación. " +
                    recommendedInstrument.getName() + " es adecuado para tu perfil de riesgo " + profile.getRiskTolerance() + 
                    " porque: " + getInstrumentJustification(recommendedInstrument, profile) + 
                    " Rendimiento esperado: " + getExpectedReturn(recommendedInstrument) + "% anual.");
                newInvestment.setConfidenceScore(BigDecimal.valueOf(0.75));
                newInvestment.setPriority(3);
                newInvestment.setExpectedReturn(getExpectedReturn(recommendedInstrument));
                newInvestment.setRiskAssessment("Riesgo " + recommendedInstrument.getRiskLevel().toString().toLowerCase() + 
                    " alineado con perfil del inversor");
                newInvestment.setTimeHorizon(profile.getInvestmentHorizon() * 365);
                newInvestment.setExpiresAt(LocalDateTime.now().plusDays(60));
                
                recommendations.add(newInvestment);
            }
        }
        
        return recommendations;
    }
    
    private BigDecimal getTargetRiskLevel(UserProfile profile) {
        return switch (profile.getRiskTolerance()) {
            case CONSERVATIVE -> BigDecimal.valueOf(0.3);
            case MODERATE -> BigDecimal.valueOf(0.6);
            case AGGRESSIVE -> BigDecimal.valueOf(0.8);
        };
    }
    
    private BigDecimal calculateExpectedReturn(UserProfile profile, BigDecimal riskLevel) {
        // Simple risk-return relationship
        BigDecimal baseReturn = BigDecimal.valueOf(4.0); // Risk-free rate
        BigDecimal riskPremium = riskLevel.multiply(BigDecimal.valueOf(8.0)); // Risk premium
        return baseReturn.add(riskPremium);
    }
    
    private Map<String, BigDecimal> getTargetAllocation(UserProfile profile) {
        Map<String, BigDecimal> allocation = new HashMap<>();
        
        return switch (profile.getRiskTolerance()) {
            case CONSERVATIVE -> {
                allocation.put("BONDS", BigDecimal.valueOf(0.6));
                allocation.put("STOCKS", BigDecimal.valueOf(0.3));
                allocation.put("INTERNATIONAL", BigDecimal.valueOf(0.1));
                yield allocation;
            }
            case MODERATE -> {
                allocation.put("STOCKS", BigDecimal.valueOf(0.5));
                allocation.put("BONDS", BigDecimal.valueOf(0.3));
                allocation.put("INTERNATIONAL", BigDecimal.valueOf(0.2));
                yield allocation;
            }
            case AGGRESSIVE -> {
                allocation.put("STOCKS", BigDecimal.valueOf(0.7));
                allocation.put("INTERNATIONAL", BigDecimal.valueOf(0.2));
                allocation.put("BONDS", BigDecimal.valueOf(0.1));
                yield allocation;
            }
        };
    }
    
    private BigDecimal calculateAvailableCash(User user) {
        // Calculate based on monthly income, expenses, and current savings
        UserProfile profile = user.getUserProfile();
        if (profile.getMonthlyIncome() != null && profile.getMonthlyExpenses() != null) {
            BigDecimal monthlySurplus = profile.getMonthlyIncome().subtract(profile.getMonthlyExpenses());
            return monthlySurplus.multiply(BigDecimal.valueOf(3)); // 3 months of surplus
        }
        return BigDecimal.valueOf(5000); // Default assumption
    }
    
    private List<FinancialInstrument> getSuitableInstruments(UserProfile profile, PortfolioAnalysis analysis) {
        FinancialInstrument.RiskLevel targetRiskLevel = switch (profile.getRiskTolerance()) {
            case CONSERVATIVE -> FinancialInstrument.RiskLevel.LOW;
            case MODERATE -> FinancialInstrument.RiskLevel.MEDIUM;
            case AGGRESSIVE -> FinancialInstrument.RiskLevel.HIGH;
        };
        
        List<FinancialInstrument> instruments = instrumentRepository.findByRiskLevel(targetRiskLevel);
        
        // Filter out instruments already heavily represented in portfolio
        return instruments.stream()
            .filter(instrument -> !analysis.isOverweighted(instrument.getSymbol()))
            .limit(5)
            .collect(Collectors.toList());
    }
    
    private String getInstrumentJustification(FinancialInstrument instrument, UserProfile profile) {
        StringBuilder justification = new StringBuilder();
        
        justification.append("Nivel de riesgo ").append(instrument.getRiskLevel().toString().toLowerCase())
            .append(" adecuado para perfil ").append(profile.getRiskTolerance().toString().toLowerCase()).append(". ");
        
        if (instrument.getDividendYield() != null && instrument.getDividendYield().compareTo(BigDecimal.ZERO) > 0) {
            justification.append("Ofrece dividendos del ")
                .append(instrument.getDividendYield().multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP))
                .append("% anual. ");
        }
        
        if (instrument.getExpenseRatio() != null) {
            justification.append("Comisiones bajas del ")
                .append(instrument.getExpenseRatio().multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP))
                .append("% anual. ");
        }
        
        return justification.toString();
    }
    
    private BigDecimal getExpectedReturn(FinancialInstrument instrument) {
        // Simple expected return based on risk level and historical patterns
        return switch (instrument.getRiskLevel()) {
            case LOW -> BigDecimal.valueOf(4.5);
            case MEDIUM -> BigDecimal.valueOf(7.5);
            case HIGH -> BigDecimal.valueOf(10.5);
        };
    }
}
