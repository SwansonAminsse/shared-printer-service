package com.yn.printer.service.modules.advertisement.repository;

import com.yn.printer.service.modules.advertisement.entity.Placement;
import com.yn.printer.service.modules.advertisement.entity.PlacementPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PlacementPaymentRepository extends JpaRepository<PlacementPayment, Long>, JpaSpecificationExecutor<PlacementPayment> {
    @Query("SELECT SUM(p.currentReceipt) FROM PlacementPayment p " +
            "WHERE p.time BETWEEN :startDate AND :endDate" )
    BigDecimal getADVERTISEMENTPaymentDateBetween(@Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);
    @Query("SELECT SUM(p.currentReceipt) FROM PlacementPayment p " +
            "WHERE p.time < :startDate ")
    BigDecimal getADVERTISEMENTPayment(@Param("startDate") LocalDateTime startDate);


}