package com.yn.printer.service.modules.operation.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "OPERATION_COMMODITY")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Commodity extends AuditableModel {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OPERATION_COMMODITY_SEQ")
  @SequenceGenerator(
          name = "OPERATION_COMMODITY_SEQ",
          sequenceName = "OPERATION_COMMODITY_SEQ",
          allocationSize = 1)
  private Long id;

  // 价格
  private BigDecimal price = BigDecimal.ZERO;

  // 商品名称
  @ManyToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "goods")
  private Goods goods;

  // 定价名称
  @ManyToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "fix_price")
  private FixPrice fixPrice;

  private String attrs;

}
