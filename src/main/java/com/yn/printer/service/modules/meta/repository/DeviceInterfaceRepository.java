package com.yn.printer.service.modules.meta.repository;

import com.yn.printer.service.modules.meta.entity.DeviceInterface;
import com.yn.printer.service.modules.operation.entity.DeviceMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DeviceInterfaceRepository  extends JpaRepository<DeviceInterface, Long>, JpaSpecificationExecutor<DeviceInterface> {
}
