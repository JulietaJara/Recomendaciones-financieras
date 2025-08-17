package com.financialrecommendations.repository;

import com.financialrecommendations.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userProfile WHERE u.email = :email")
    Optional<User> findByEmailWithProfile(@Param("email") String email);
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.portfolios WHERE u.id = :id")
    Optional<User> findByIdWithPortfolios(@Param("id") Long id);
}
