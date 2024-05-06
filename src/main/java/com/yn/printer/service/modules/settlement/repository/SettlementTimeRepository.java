package com.yn.printer.service.modules.settlement.repository;

import com.yn.printer.service.modules.settlement.entity.SettlementTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SettlementTimeRepository extends JpaRepository<SettlementTime, Long>, JpaSpecificationExecutor<SettlementTime> {


}
