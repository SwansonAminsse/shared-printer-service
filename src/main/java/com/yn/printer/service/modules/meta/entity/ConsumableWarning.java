package com.yn.printer.service.modules.meta.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import com.yn.printer.service.modules.operation.enums.ConsumableType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(
    name = "META_CONSUMABLE_WARNING",
    indexes = {@Index(columnList = "xing_hao")})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ConsumableWarning extends AuditableModel implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "META_CONSUMABLE_WARNING_SEQ")
  @SequenceGenerator(
      name = "META_CONSUMABLE_WARNING_SEQ",
      sequenceName = "META_CONSUMABLE_WARNING_SEQ",
      allocationSize = 1)
  private Long id;

  // 耗材类型
  @Enumerated(value = EnumType.STRING)
  private ConsumableType consumableType;

  // 耗材预警值
  private Integer warningValue = 0;

  // 型号
  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "xing_hao")
  private XingHao xingHao;

  //最大耗材余量
  private Integer maxConsumableResidue = 0;

}
