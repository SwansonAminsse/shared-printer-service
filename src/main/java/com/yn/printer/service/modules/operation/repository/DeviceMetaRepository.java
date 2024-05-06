package com.yn.printer.service.modules.operation.repository;

import com.yn.printer.service.modules.operation.entity.DeviceMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DeviceMetaRepository extends JpaRepository<DeviceMeta, Long>, JpaSpecificationExecutor<DeviceMeta> {
}
