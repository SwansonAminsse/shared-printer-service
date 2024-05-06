package com.yn.printer.service.modules.advertisement.service.impl;

import com.alibaba.fastjson.JSON;
import com.yn.printer.service.common.exception.YnError;
import com.yn.printer.service.common.exception.YnErrorException;
import com.yn.printer.service.modules.advertisement.entity.*;
import com.yn.printer.service.modules.advertisement.enums.Screen;
import com.yn.printer.service.modules.advertisement.repository.AdvertisementPlacementLocationRepository;
import com.yn.printer.service.modules.advertisement.repository.AdvertisementPlacementRegistrationRepository;
import com.yn.printer.service.modules.advertisement.repository.PlacementRepository;
import com.yn.printer.service.modules.advertisement.repository.RegistrationRepository;
import com.yn.printer.service.modules.advertisement.service.IPlacementService;
import com.yn.printer.service.modules.advertisement.vo.PlacementVO;
import com.yn.printer.service.modules.common.mqtt.MqttConfig;
import com.yn.printer.service.modules.common.mqtt.MqttSender;
import com.yn.printer.service.modules.common.mqtt.dto.AdvertisementPush;
import com.yn.printer.service.modules.common.service.IFileService;
import com.yn.printer.service.modules.common.vo.MetaFileVo;
import com.yn.printer.service.modules.meta.repository.MetaFileRepository;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import com.yn.printer.service.modules.operation.repository.DevicesListRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlacementServiceImpl implements IPlacementService {
    @Autowired
    private MetaFileRepository metaFileRepository;
    @Autowired
    private PlacementRepository placementRepository;
    @Autowired
    private AdvertisementPlacementLocationRepository advertisementPlacementLocationRepository;
    @Autowired
    private IFileService fileService;
    @Autowired
    private AdvertisementPlacementRegistrationRepository advertisementPlacementRegistrationRepository;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    DevicesListRepository repository;
    @Autowired
    MqttSender mqttSender;
    @Autowired
    MqttConfig mqttConfig;

@Override
    public List<PlacementVO>  getAddress(Long deviceId, Screen screen)  {
    LocalDateTime localDateTime = LocalDateTime.now();
    List<AdvertisementPlacementLocation> advertisementPlacementLocations = advertisementPlacementLocationRepository.findByDeviceId(deviceId);
    List<Long> placementIds = advertisementPlacementLocations.stream()
            .map(AdvertisementPlacementLocation::getPlacementId)
            .collect(Collectors.toList());
    List<Placement> placements = new ArrayList<>();
    for (Long placementId : placementIds) {
        Optional<Placement> placementOptional = placementRepository.findById(placementId);
        placementOptional.ifPresent(placements::add);
    }
    List<Placement> filteredPlacements = placements.stream()
            .filter(placement -> isPlacementValidFor(localDateTime, screen, placement))
            .collect(Collectors.toList());
    if (!filteredPlacements.isEmpty()) {
        List<MetaFileVo> metaFileVos = getRegistration(filteredPlacements.get(0))
                        .stream()
                        .map(Registration::getContent)
                        .map(fileService::metaFile2Vo)
                        .collect(Collectors.toList());
        List<PlacementVO> placementVOs = new ArrayList<>();
        metaFileVos.forEach(metaFileVo -> {
            PlacementVO placementVO = new PlacementVO();
            BeanUtils.copyProperties(metaFileVo, placementVO);
            placementVOs.add(placementVO);
        });

        for(PlacementVO placementVO : placementVOs)
           placementVO.setIntervalTime(filteredPlacements.get(0).getIntervalTime());
        return placementVOs;
    } else {
        List<MetaFileVo> metaFileVos = getRegistration(placementRepository.findByAcquiesce(true)).stream()
                .map(Registration::getContent)
                .map(fileService::metaFile2Vo)
                .collect(Collectors.toList());
        val placementVOs = new ArrayList<PlacementVO>();
        for (MetaFileVo metaFileVo : metaFileVos) {
            PlacementVO placementVO = new PlacementVO();
            BeanUtils.copyProperties(metaFileVo, placementVO);
            placementVOs.add(placementVO);
        }
        for(PlacementVO placementVO : placementVOs)
            placementVO.setIntervalTime(placementRepository.findByAcquiesce(true).getIntervalTime());
        return placementVOs;
    }
    }
    @Override
    public  void changeAdvertisement(String code) {
        DevicesList device = repository.findByCode(code);
        if (device == null) throw new YnErrorException(YnError.YN_400001);
        String topic = mqttConfig.topicSendDevice + device.getCode();
        String message = JSON.toJSONString(new AdvertisementPush());
        mqttSender.send(topic, message);
        log.info("推送MQTT消息 主题: {}, 内容: {}", topic, message);
    }
    private boolean isPlacementValidFor(LocalDateTime localDateTime, Screen screen, Placement placement) {
        for (AdvertisingSchedule advertisingSchedule : placement.getAdvertisingSchedule()) {
            if (isTimeOverlap(localDateTime, advertisingSchedule.getStartTime(), advertisingSchedule.getEndTime())
                    && screen.equals(placement.getScreen())) {
                return true;
            }
        }
        return false;
    }
    private boolean isTimeOverlap(LocalDateTime targetTime, LocalDateTime startTime, LocalDateTime endTime) {
        return !targetTime.isBefore(startTime) && !targetTime.isAfter(endTime);
    }
    private List<Registration> getRegistration(Placement placement) {
        List<Long> RegistrationIds = advertisementPlacementRegistrationRepository.findRegistrationIdsByPlacementId(placement.getId());
        log.info("RegistrationIds:{}", JSON.toJSONString(RegistrationIds));
        List<Registration> registrations = new ArrayList<>();
        for (Long RegistrationId : RegistrationIds) {
            Optional<Registration> registrationOptional = registrationRepository.findById(RegistrationId);
            registrationOptional.ifPresent(registrations::add);
        }
        return registrations;
    }
}
