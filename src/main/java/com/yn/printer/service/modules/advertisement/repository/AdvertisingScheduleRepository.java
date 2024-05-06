package com.yn.printer.service.modules.advertisement.repository;

import com.yn.printer.service.modules.advertisement.entity.AdvertisingSchedule;
import com.yn.printer.service.modules.advertisement.entity.Placement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisingScheduleRepository extends JpaRepository<AdvertisingSchedule, Long>, JpaSpecificationExecutor<AdvertisingSchedule> {
}