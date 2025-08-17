package com.financialrecommendations.repository;

import com.financialrecommendations.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    
    List<Recommendation> findByUserIdOrderByPriorityAscCreatedAtDesc(Long userId);
    
    List<Recommendation> findByUserIdAndStatus(Long userId, Recommendation.RecommendationStatus status);
    
    @Query("SELECT r FROM Recommendation r WHERE r.user.id = :userId AND r.status = 'PENDING' ORDER BY r.priority ASC, r.createdAt DESC")
    List<Recommendation> findPendingRecommendationsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT r FROM Recommendation r WHERE r.expiresAt < :now AND r.status = 'PENDING'")
    List<Recommendation> findExpiredRecommendations(@Param("now") LocalDateTime now);
    
    @Query("SELECT r FROM Recommendation r LEFT JOIN FETCH r.recommendedInstruments ri LEFT JOIN FETCH ri.instrument WHERE r.id = :id")
    Recommendation findByIdWithInstruments(@Param("id") Long id);
}
