package com.yn.printer.service.modules.operation.repository;

import com.yn.printer.service.modules.operation.entity.FixPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FixPriceRepository extends JpaRepository<FixPrice, Long>, JpaSpecificationExecutor<FixPrice> {

}
