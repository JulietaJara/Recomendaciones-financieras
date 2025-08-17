package com.financialrecommendations.controller;

import com.financialrecommendations.entity.Portfolio;
import com.financialrecommendations.service.PortfolioService;
import com.financialrecommendations.security.CurrentUser;
import com.financialrecommendations.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portfolios")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class PortfolioController {
    
    private final PortfolioService portfolioService;
    
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Portfolio>> getUserPortfolios(@CurrentUser UserPrincipal currentUser) {
        List<Portfolio> portfolios = portfolioService.getUserPortfolios(currentUser.getId());
        return ResponseEntity.ok(portfolios);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Portfolio> getPortfolio(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
        try {
            Portfolio portfolio = portfolioService.getPortfolioById(id, currentUser.getId());
            return ResponseEntity.ok(portfolio);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
