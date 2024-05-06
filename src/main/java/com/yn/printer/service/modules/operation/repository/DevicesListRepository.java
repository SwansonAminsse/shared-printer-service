package com.yn.printer.service.modules.operation.repository;

import com.yn.printer.service.modules.channel.entity.ChannelPartner;
import com.yn.printer.service.modules.enums.DeviceStatus;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import com.yn.printer.service.modules.operation.vo.DeviceStatusVO;
import com.yn.printer.service.modules.operation.vo.DevicesListVO;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.yn.printer.service.modules.operation.util.DistanceCalculator.calculateDistance;

@Repository
public interface DevicesListRepository extends JpaRepository<DevicesList, Long>, JpaSpecificationExecutor<DevicesList> {

    @Query("SELECT new com.yn.printer.service.modules.operation.vo.DevicesListVO(d.address, d.jd, d.wd, d.code, d.terminalMerchants.name,d.name,d.province,d.city,d.county,d.street) " +
            "FROM DevicesList d " +
            "WHERE d.dev = :member ")
    Page<DevicesListVO> findDevicesAndChannelNameByMember(
            @Param("member") Member member,
            Pageable pageable);
    long countByCreatedOnAfter(LocalDateTime time);
    @Query("SELECT new com.yn.printer.service.modules.operation.vo.DeviceStatusVO(d.status, d.abnormalReason) " +
            "FROM DevicesList d " +
            "WHERE d.code = :code ")
    DeviceStatusVO findDeviceStatusVOByCode(
            @Param("code") String code);

    default Page<DevicesListVO> findDevicesByPhoneNumberWithDistance(
            Member member, double latitude, double longitude, Pageable pageable) {
        Page<DevicesListVO> devicesListPage = findDevicesAndChannelNameByMember(member, pageable);
        System.out.println(devicesListPage.getContent());
        List<DevicesListVO> devicesList = devicesListPage.getContent().stream()
                .map(device -> DevicesListVO.createWithDistance(device, latitude, longitude))
                .sorted(Comparator.comparingDouble(DevicesListVO::getDistance))
                .collect(Collectors.toList());
        return new PageImpl<>(devicesList, pageable, devicesListPage.getTotalElements());
    }

    default Page<DevicesListVO> findDevicesWithDistance(
            double latitude, double longitude, Pageable pageable) {
        List<DevicesList> devicesLists = findAll();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<DevicesListVO> devicesList;
        if (startItem < devicesLists.size()) {
            int endItem = Math.min(startItem + pageSize, devicesLists.size());
            List<DevicesList> sublist = devicesLists.subList(startItem, endItem);
            devicesList = sublist.stream()
                    .filter(Objects::nonNull) // 过滤掉为null的元素
                    .map(device -> DevicesListVO.create(device, latitude, longitude))
                    .sorted(Comparator.comparingDouble(DevicesListVO::getDistance))
                    .collect(Collectors.toList());
        } else {
            devicesList = Collections.emptyList();
        }
        return new PageImpl<>(devicesList, pageable, devicesLists.size());
    }

    long countByDev(Member dev);

    long countByDeviceStatus(Boolean deviceStatus);


    DevicesList findByBindUser(Member member);

    DevicesList findByCode(String code);

    @Query("SELECT d FROM DevicesList d " +
            "WHERE d.platform = :channelPartner " +
            "OR d.cityPartner = :channelPartner " +
            "OR d.partners = :channelPartner " +
            "OR d.terminalMerchants = :channelPartner")
    Page<DevicesList> findDevicesByChannelPartner(@Param("channelPartner") ChannelPartner channelPartner,
                                                  Pageable pageable);

    @Query("SELECT d FROM DevicesList d " +
            "WHERE d.platform = :channelPartner " +
            "OR d.cityPartner = :channelPartner " +
            "OR d.partners = :channelPartner " +
            "OR d.terminalMerchants = :channelPartner")
    List<DevicesList> findDevices(@Param("channelPartner") ChannelPartner channelPartner);

    default Page<DevicesListVO> findDevicesWithDistance(
            ChannelPartner channelPartner, double latitude, double longitude, Pageable pageable) {
        List<DevicesList> devicesList = findDevices(channelPartner);
        List<DevicesListVO> devicesListVO = devicesList.stream()
                .map(device -> {
                    val deviceVO = new DevicesListVO(
                            device.getAddress(),
                            device.getJd(),
                            device.getWd(),
                            device.getCode(),
                            device.getTerminalMerchants().getName(),
                            device.getName(),
                            device.getProvince(),
                            device.getCity(),
                            device.getCounty(),
                            device.getStreet()
                    );
                    double distance = calculateDistance(latitude, longitude, device.getWd(), device.getJd());
                    deviceVO.setDistance(distance);
                    return deviceVO;
                })
                .sorted(Comparator.comparingDouble(DevicesListVO::getDistance))
                .collect(Collectors.toList());
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min((start + pageable.getPageSize()), devicesListVO.size());
        List<DevicesListVO> sublist = devicesListVO.subList(start, end);
        return new PageImpl<>(sublist, pageable, devicesListVO.size());
    }


    List<DevicesList> findAllByBindUserNotNull();

    List<DevicesList> findByDeviceStatus(boolean b);

    List<DevicesList> findByStatus(DeviceStatus status);



    @Query("SELECT d.code FROM DevicesList d " +
            "WHERE d.status = :status " )
    List<String> findCodeByStatus(@Param("status") DeviceStatus status);

}

