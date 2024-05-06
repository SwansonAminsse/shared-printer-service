package com.yn.printer.service.modules.operation.repository;

import com.yn.printer.service.modules.channel.entity.ChannelPartner;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import com.yn.printer.service.modules.operation.entity.EquipmentDivision;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EquipmentDivisionRepository extends JpaRepository<EquipmentDivision, Long>, JpaSpecificationExecutor<EquipmentDivision> {

}