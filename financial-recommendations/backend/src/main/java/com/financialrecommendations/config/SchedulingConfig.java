package com.financialrecommendations.config;

import com.financialrecommendations.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SchedulingConfig {
    
    private final RecommendationService recommendationService;
    
    @Scheduled(fixedRate = 3600000) // Run every hour
    public void expireOldRecommendations() {
        log.info("Running scheduled task to expire old recommendations");
        recommendationService.expireOldRecommendations();
    }
}
