package com.yn.printer.service.modules.advertisement.repository;

import com.yn.printer.service.modules.advertisement.entity.AdvertisementPlacementLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AdvertisementPlacementLocationRepository extends JpaRepository<AdvertisementPlacementLocation, Long> , JpaSpecificationExecutor<AdvertisementPlacementLocation> {

    List<AdvertisementPlacementLocation> findByDeviceId(Long deviceId);
}