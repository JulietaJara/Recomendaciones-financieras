package com.financialrecommendations.controller;

import com.financialrecommendations.entity.Recommendation;
import com.financialrecommendations.service.RecommendationService;
import com.financialrecommendations.security.CurrentUser;
import com.financialrecommendations.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class RecommendationController {
    
    private final RecommendationService recommendationService;
    
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Recommendation>> getUserRecommendations(@CurrentUser UserPrincipal currentUser) {
        List<Recommendation> recommendations = recommendationService.getUserRecommendations(currentUser.getId());
        return ResponseEntity.ok(recommendations);
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Recommendation>> getPendingRecommendations(@CurrentUser UserPrincipal currentUser) {
        List<Recommendation> recommendations = recommendationService.getPendingRecommendations(currentUser.getId());
        return ResponseEntity.ok(recommendations);
    }
    
    @PostMapping("/generate")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Recommendation>> generateRecommendations(@CurrentUser UserPrincipal currentUser) {
        try {
            List<Recommendation> recommendations = recommendationService.generateRecommendationsForUser(currentUser.getId());
            return ResponseEntity.ok(recommendations);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @PostMapping("/{id}/accept")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Recommendation> acceptRecommendation(@PathVariable Long id) {
        try {
            Recommendation recommendation = recommendationService.acceptRecommendation(id);
            return ResponseEntity.ok(recommendation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Recommendation> rejectRecommendation(@PathVariable Long id) {
        try {
            Recommendation recommendation = recommendationService.rejectRecommendation(id);
            return ResponseEntity.ok(recommendation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
