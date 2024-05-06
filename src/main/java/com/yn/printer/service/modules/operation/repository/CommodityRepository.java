package com.yn.printer.service.modules.operation.repository;

import com.yn.printer.service.modules.operation.entity.Commodity;
import com.yn.printer.service.modules.operation.entity.FixPrice;
import com.yn.printer.service.modules.operation.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CommodityRepository extends JpaRepository<Commodity, Long>, JpaSpecificationExecutor<Commodity> {

    Commodity findByGoodsAndFixPrice(Goods goods, FixPrice fixPrice);

    List<Commodity> findByFixPrice(FixPrice fixPrice);

}
