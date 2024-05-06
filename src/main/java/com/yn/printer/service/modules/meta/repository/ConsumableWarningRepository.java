package com.yn.printer.service.modules.meta.repository;


import com.yn.printer.service.modules.meta.entity.ConsumableWarning;
import com.yn.printer.service.modules.meta.entity.XingHao;
import com.yn.printer.service.modules.operation.enums.ConsumableType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ConsumableWarningRepository extends JpaRepository<ConsumableWarning, Long>, JpaSpecificationExecutor<ConsumableWarning> {

    ConsumableWarning findByXingHaoAndConsumableType(XingHao xingHao, ConsumableType consumableType);

}
