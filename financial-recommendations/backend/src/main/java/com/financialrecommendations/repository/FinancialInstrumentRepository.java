package com.financialrecommendations.repository;

import com.financialrecommendations.entity.FinancialInstrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialInstrumentRepository extends JpaRepository<FinancialInstrument, Long> {
    
    Optional<FinancialInstrument> findBySymbol(String symbol);
    
    List<FinancialInstrument> findByInstrumentType(FinancialInstrument.InstrumentType instrumentType);
    
    List<FinancialInstrument> findByRiskLevel(FinancialInstrument.RiskLevel riskLevel);
    
    List<FinancialInstrument> findBySector(String sector);
    
    @Query("SELECT fi FROM FinancialInstrument fi WHERE fi.isActive = true")
    List<FinancialInstrument> findAllActive();
    
    @Query("SELECT fi FROM FinancialInstrument fi WHERE fi.instrumentType = :type AND fi.riskLevel = :riskLevel AND fi.isActive = true")
    List<FinancialInstrument> findByTypeAndRiskLevel(@Param("type") FinancialInstrument.InstrumentType type, 
                                                    @Param("riskLevel") FinancialInstrument.RiskLevel riskLevel);
}
