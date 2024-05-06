package com.yn.printer.service.modules.meta.repository;


import com.yn.printer.service.modules.meta.entity.PaperWarning;
import com.yn.printer.service.modules.meta.entity.XingHao;
import com.yn.printer.service.modules.meta.enums.PaperType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PaperWarningRepository extends JpaRepository<PaperWarning, Long>, JpaSpecificationExecutor<PaperWarning> {

    PaperWarning findByXingHaoAndPaperType(XingHao xingHao, PaperType paperType);

    List<PaperWarning> findByXingHao(XingHao xh);
}
