package com.yn.printer.service.modules.operation.repository;

import com.yn.printer.service.modules.meta.enums.PrintColor;
import com.yn.printer.service.modules.meta.enums.PrintFaces;
import com.yn.printer.service.modules.meta.enums.PrintType;
import com.yn.printer.service.modules.operation.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GoodsRepository extends JpaRepository<Goods, Long>, JpaSpecificationExecutor<Goods> {

    Goods findByPrintType(PrintType printType);

    Goods findByPrintTypeAndPrintColorAndPrintFaces(PrintType printType, PrintColor printColor, PrintFaces printFaces);

}
