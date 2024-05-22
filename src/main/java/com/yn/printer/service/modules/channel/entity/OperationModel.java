package com.yn.printer.service.modules.channel.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import com.yn.printer.service.modules.channel.enums.ChannelType;
import com.yn.printer.service.modules.channel.enums.OperatingModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(
        name = "CHANNEL_OPERATION_MODEL",
        indexes = {@Index(columnList = "user_id")})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class OperationModel extends AuditableModel {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHANNEL_OPERATION_MODEL_SEQ")
  @SequenceGenerator(
          name = "CHANNEL_OPERATION_MODEL_SEQ",
          sequenceName = "CHANNEL_OPERATION_MODEL_SEQ",
          allocationSize = 1)
  private Long id;

  // 运营角色
  private ChannelType operationalRoles;

  // 用户ID
  @ManyToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "user_id")
  private ChannelPartner userId;

  // 运营模式
  private OperatingModel operationMode;

  private String attrs;
}
