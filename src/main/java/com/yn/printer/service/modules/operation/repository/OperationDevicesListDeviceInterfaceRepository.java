package com.yn.printer.service.modules.operation.repository;

import com.yn.printer.service.modules.operation.entity.OperationDevicesListDeviceInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OperationDevicesListDeviceInterfaceRepository extends JpaRepository<OperationDevicesListDeviceInterface, Long>, JpaSpecificationExecutor<OperationDevicesListDeviceInterface> {
    @Query("SELECT o.deviceInterfaceId FROM OperationDevicesListDeviceInterface o WHERE o.deviceId = :deviceId")
    List<Long> finddeviceInterfaceIdsBydeviceId(@Param("deviceId") Long deviceId);







}