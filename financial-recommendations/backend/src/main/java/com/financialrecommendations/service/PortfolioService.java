package com.financialrecommendations.service;

import com.financialrecommendations.entity.Portfolio;
import com.financialrecommendations.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PortfolioService {
    
    private final PortfolioRepository portfolioRepository;
    
    @Transactional(readOnly = true)
    public List<Portfolio> getUserPortfolios(Long userId) {
        return portfolioRepository.findByUserIdWithHoldings(userId);
    }
    
    @Transactional(readOnly = true)
    public Portfolio getPortfolioById(Long portfolioId, Long userId) {
        Portfolio portfolio = portfolioRepository.findByIdWithHoldings(portfolioId)
            .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        
        if (!portfolio.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }
        
        return portfolio;
    }
}
