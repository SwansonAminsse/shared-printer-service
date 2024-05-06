package com.yn.printer.service.modules.operation.repository;

import com.yn.printer.service.modules.meta.enums.PaperType;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import com.yn.printer.service.modules.operation.entity.PaperTable;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaperTableRepository extends JpaRepository<PaperTable, Long>, JpaSpecificationExecutor<PaperTable> {
    @Query("SELECT c FROM PaperTable c WHERE c.id = :Id")
    PaperTable find(@Param("Id") Long Id);

    List<PaperTable> findByDevice(DevicesList device);

    PaperTable findByDeviceAndName(DevicesList device, PaperType name);
}
