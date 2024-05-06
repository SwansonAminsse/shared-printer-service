package com.yn.printer.service.modules.operation.repository;

import com.yn.printer.service.modules.operation.entity.OperationDevicesListSupportingComponents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OperationDevicesListSupportingComponentsRepository extends JpaRepository<OperationDevicesListSupportingComponents, Long>, JpaSpecificationExecutor<OperationDevicesListSupportingComponents> {
    @Query("SELECT o.deviceMeatId FROM OperationDevicesListSupportingComponents o WHERE o.deviceId = :deviceId")
    List<Long> finddeviceMeatIdsBydeviceId(@Param("deviceId") Long deviceId);



}
