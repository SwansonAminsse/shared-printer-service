package com.yn.printer.service.modules.channel.repository;

import com.yn.printer.service.modules.channel.entity.ChannelPartner;
import com.yn.printer.service.modules.dataAnalysis.vo.ChannelPartnerInfo;
import com.yn.printer.service.modules.enums.ChannelType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.nio.channels.Channel;
import java.util.List;

public interface ChannelPartnerRepository extends JpaRepository<ChannelPartner, Long> {

  long countByAdminPhone(String adminPhone);

  ChannelPartner findByAdminPhone(String adminPhone);

  @Query("SELECT COUNT(c) FROM ChannelPartner c WHERE c.channelType = :channelType")
  long countByChannelType(ChannelType channelType);

  @Query("SELECT c FROM ChannelPartner c WHERE (:id is null OR c.id = :id)")
  List<ChannelPartner> findChannelById(Long id);

  @Query("SELECT new com.yn.printer.service.modules.dataAnalysis.vo.ChannelPartnerInfo(c.name,c.id) " +
          "FROM ChannelPartner c")
  List<ChannelPartnerInfo> findAllChannel();
}
