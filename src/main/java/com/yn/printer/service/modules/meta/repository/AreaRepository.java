package com.yn.printer.service.modules.meta.repository;

import com.yn.printer.service.modules.meta.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AreaRepository extends JpaRepository<Area, Long>, JpaSpecificationExecutor<Area> {
    Area findByName(String name);

    Area findByNameAndArea(String name,Area area);


}