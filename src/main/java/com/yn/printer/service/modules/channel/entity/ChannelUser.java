package com.yn.printer.service.modules.channel.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import com.yn.printer.service.modules.meta.entity.Area;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(
        name = "CHANNEL_CHANNEL_USER",
        indexes = {
                @Index(columnList = "name"),
                @Index(columnList = "region"),
                @Index(columnList = "channel_user"),
                @Index(columnList = "channel_partner")
        })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ChannelUser extends AuditableModel {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHANNEL_CHANNEL_USER_SEQ")
  @SequenceGenerator(
          name = "CHANNEL_CHANNEL_USER_SEQ",
          sequenceName = "CHANNEL_CHANNEL_USER_SEQ",
          allocationSize = 1)
  private Long id;

  // 用户ID
  private String userId;

  // 用户名称
  private String name;

  // 所属区域
  @ManyToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "region")
  private Area region;

  // 推荐人
  @ManyToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "channel_user")
  private ChannelUser channelUser;

  // 联系电话
  private String contactPhone;

  // 渠道商
  @ManyToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "channel_partner")
  private ChannelPartner channelPartner;

  private String attrs;
}
