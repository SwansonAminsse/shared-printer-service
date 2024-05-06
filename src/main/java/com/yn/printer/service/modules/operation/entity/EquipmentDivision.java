package com.yn.printer.service.modules.operation.entity;

import com.google.common.base.MoreObjects;
import com.yn.printer.service.common.entity.AuditableModel;
import com.yn.printer.service.modules.channel.entity.ChannelPartner;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(
    name = "OPERATION_EQUIPMENT_DIVISION",
    indexes = {@Index(columnList = "channel")})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class EquipmentDivision extends AuditableModel {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "OPERATION_EQUIPMENT_DIVISION_SEQ")
  @SequenceGenerator(
      name = "OPERATION_EQUIPMENT_DIVISION_SEQ",
      sequenceName = "OPERATION_EQUIPMENT_DIVISION_SEQ",
      allocationSize = 1)
  private Long id;


  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "channel")
  private ChannelPartner channel;


  private BigDecimal ratio = BigDecimal.ZERO;

  @ManyToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "device")
  private DevicesList device;
}
