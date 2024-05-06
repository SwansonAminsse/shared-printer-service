package com.yn.printer.service.modules.settlement.repository;

import com.yn.printer.service.modules.settlement.entity.ChannelSettlement;
import com.yn.printer.service.modules.settlement.entity.SettlementDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChannelSettlementRepository extends JpaRepository<ChannelSettlement, Long>, JpaSpecificationExecutor<ChannelSettlement> {
}