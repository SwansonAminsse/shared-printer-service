package com.yn.printer.service.modules.settlement.repository;

import com.yn.printer.service.modules.settlement.entity.Equipment;
import com.yn.printer.service.modules.settlement.entity.SettlementTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EquipmentRepository extends JpaRepository<Equipment, Long>, JpaSpecificationExecutor<Equipment> {
}
