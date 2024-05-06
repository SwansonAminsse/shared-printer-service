package com.yn.printer.service.modules.operation.repository;

import com.yn.printer.service.modules.operation.entity.ConsumablesValue;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import com.yn.printer.service.modules.operation.enums.ConsumableType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConsumablesValueRepository extends JpaRepository<ConsumablesValue, Long>, JpaSpecificationExecutor<ConsumablesValue> {
    @Query("SELECT c FROM ConsumablesValue c WHERE c.id = :Id")
    ConsumablesValue find(@Param("Id") Long Id);

    List<ConsumablesValue> findByDevice(DevicesList device);

    ConsumablesValue findByDeviceAndName(DevicesList device, ConsumableType consumableType);
}
