package com.financialrecommendations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FinancialRecommendationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancialRecommendationsApplication.class, args);
    }
}
