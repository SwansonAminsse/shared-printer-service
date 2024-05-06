package com.yn.printer.service.modules.member.repository;

import com.yn.printer.service.modules.member.entity.ChargeFile;
import com.yn.printer.service.modules.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ChargeFileRepository extends JpaRepository<ChargeFile, Long>, JpaSpecificationExecutor<ChargeFile> {

    @Query("SELECT COALESCE(SUM(pf.refillAmount), 0) FROM ChargeFile pf WHERE pf.increasing = true AND pf.member = :member")
    BigDecimal sumOfActiveIncreasingPoints(@Param("member") Member member);
    @Query("SELECT COALESCE(SUM(pf.refillAmount), 0) FROM ChargeFile pf WHERE pf.low = true AND pf.member = :member")
    BigDecimal sumOfLowPoints(@Param("member") Member member);

}
