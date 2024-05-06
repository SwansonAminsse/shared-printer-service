package com.yn.printer.service.modules.meta.repository;


import com.yn.printer.service.modules.meta.entity.MetaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MetaFileRepository extends JpaRepository<MetaFile, Long>, JpaSpecificationExecutor<MetaFile> {

}
