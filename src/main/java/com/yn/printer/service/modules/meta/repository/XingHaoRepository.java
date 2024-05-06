package com.yn.printer.service.modules.meta.repository;


import com.yn.printer.service.modules.meta.entity.XingHao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface XingHaoRepository extends JpaRepository<XingHao, Long>, JpaSpecificationExecutor<XingHao> {

}
