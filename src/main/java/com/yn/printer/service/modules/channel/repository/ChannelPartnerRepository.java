package com.yn.printer.service.modules.channel.repository;

import com.yn.printer.service.modules.channel.entity.ChannelPartner;
import com.yn.printer.service.modules.enums.ChannelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChannelPartnerRepository extends JpaRepository<ChannelPartner, Long> {

  long countByAdminPhone(String adminPhone);

  ChannelPartner findByAdminPhone(String adminPhone);

  @Query("SELECT COUNT(c) FROM ChannelPartner c WHERE c.channelType = :channelType")
  long countByChannelType(ChannelType channelType);


  List<ChannelPartner> findAll();
}
