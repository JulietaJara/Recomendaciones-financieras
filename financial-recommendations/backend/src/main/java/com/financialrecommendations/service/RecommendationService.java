package com.financialrecommendations.service;

import com.financialrecommendations.entity.Recommendation;
import com.financialrecommendations.entity.User;
import com.financialrecommendations.repository.RecommendationRepository;
import com.financialrecommendations.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RecommendationService {
    
    private final RecommendationRepository recommendationRepository;
    private final UserRepository userRepository;
    private final RecommendationEngineService recommendationEngineService;
    
    @Transactional(readOnly = true)
    public List<Recommendation> getUserRecommendations(Long userId) {
        return recommendationRepository.findByUserIdOrderByPriorityAscCreatedAtDesc(userId);
    }
    
    @Transactional(readOnly = true)
    public List<Recommendation> getPendingRecommendations(Long userId) {
        return recommendationRepository.findPendingRecommendationsByUserId(userId);
    }
    
    public List<Recommendation> generateRecommendationsForUser(Long userId) {
        User user = userRepository.findByEmailWithProfile(
            userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getEmail()
        ).orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getUserProfile() == null) {
            throw new RuntimeException("User profile not found. Please complete your profile first.");
        }
        
        return recommendationEngineService.generateRecommendations(user);
    }
    
    public Recommendation acceptRecommendation(Long recommendationId) {
        Recommendation recommendation = recommendationRepository.findById(recommendationId)
            .orElseThrow(() -> new RuntimeException("Recommendation not found"));
        
        if (recommendation.getStatus() != Recommendation.RecommendationStatus.PENDING) {
            throw new RuntimeException("Recommendation is not pending");
        }
        
        recommendation.setStatus(Recommendation.RecommendationStatus.ACCEPTED);
        return recommendationRepository.save(recommendation);
    }
    
    public Recommendation rejectRecommendation(Long recommendationId) {
        Recommendation recommendation = recommendationRepository.findById(recommendationId)
            .orElseThrow(() -> new RuntimeException("Recommendation not found"));
        
        if (recommendation.getStatus() != Recommendation.RecommendationStatus.PENDING) {
            throw new RuntimeException("Recommendation is not pending");
        }
        
        recommendation.setStatus(Recommendation.RecommendationStatus.REJECTED);
        return recommendationRepository.save(recommendation);
    }
    
    public void expireOldRecommendations() {
        List<Recommendation> expiredRecommendations = 
            recommendationRepository.findExpiredRecommendations(LocalDateTime.now());
        
        expiredRecommendations.forEach(rec -> {
            rec.setStatus(Recommendation.RecommendationStatus.EXPIRED);
            log.info("Expired recommendation {} for user {}", rec.getId(), rec.getUser().getId());
        });
        
        recommendationRepository.saveAll(expiredRecommendations);
    }
}
