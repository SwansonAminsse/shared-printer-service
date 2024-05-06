package com.yn.printer.service.modules.advertisement.repository;
import com.yn.printer.service.modules.advertisement.entity.AdvertisementPlacementLocation;
import com.yn.printer.service.modules.advertisement.entity.Placement;
import com.yn.printer.service.modules.advertisement.enums.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PlacementRepository extends JpaRepository<Placement, Long>, JpaSpecificationExecutor<Placement> {
    Placement findByAcquiesce(Boolean acquiesce);
}