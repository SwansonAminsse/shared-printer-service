package com.yn.printer.service.modules.advertisement.repository;
import com.yn.printer.service.modules.advertisement.entity.AdvertisementPlacementRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdvertisementPlacementRegistrationRepository extends JpaRepository<AdvertisementPlacementRegistration, Long>, JpaSpecificationExecutor<AdvertisementPlacementRegistration> {
    @Query("SELECT r.registrationId FROM AdvertisementPlacementRegistration r WHERE r.placementId = :placementId")
    List<Long> findRegistrationIdsByPlacementId(@Param("placementId") Long placementId);
}
