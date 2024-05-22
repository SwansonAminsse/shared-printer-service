package com.yn.printer.service.modules.channel.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import com.yn.printer.service.modules.channel.enums.ChannelType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(
        name = "CHANNEL_CHANNEL_PARTNER",
        indexes = {@Index(columnList = "channel_partner")})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ChannelPartner extends AuditableModel {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHANNEL_CHANNEL_PARTNER_SEQ")
  @SequenceGenerator(
          name = "CHANNEL_CHANNEL_PARTNER_SEQ",
          sequenceName = "CHANNEL_CHANNEL_PARTNER_SEQ",
          allocationSize = 1)
  @Column(name = "ID")
  private Long id;

  // 渠道商ID
  private String channelId;

  // 渠道商名称
  private String name;

  // 渠道商类型
  @Enumerated(value = EnumType.STRING)
  private ChannelType channelType;




  // 管理员电话
  private String adminPhone;

  // 上级渠道商
  @ManyToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "channel_partner")
  private ChannelPartner channelPartner;

  // 内部人员
  @OneToMany(
          fetch = FetchType.LAZY,
          mappedBy = "channelPartner",
          cascade = CascadeType.ALL,
          orphanRemoval = true)
  private List<ChannelUser> channelUser;

  private String attrs;

}
