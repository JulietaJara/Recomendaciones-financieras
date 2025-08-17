package com.financialrecommendations.repository;

import com.financialrecommendations.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    
    List<Portfolio> findByUserId(Long userId);
    
    List<Portfolio> findByUserIdAndIsActive(Long userId, Boolean isActive);
    
    @Query("SELECT p FROM Portfolio p LEFT JOIN FETCH p.holdings h LEFT JOIN FETCH h.instrument WHERE p.user.id = :userId")
    List<Portfolio> findByUserIdWithHoldings(@Param("userId") Long userId);
    
    @Query("SELECT p FROM Portfolio p LEFT JOIN FETCH p.holdings WHERE p.id = :id")
    Optional<Portfolio> findByIdWithHoldings(@Param("id") Long id);
    
    @Query("SELECT SUM(p.totalValue) FROM Portfolio p WHERE p.user.id = :userId AND p.isActive = true")
    BigDecimal getTotalPortfolioValueByUserId(@Param("userId") Long userId);
}
