package com.yn.printer.service.modules.operation.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(
        name = "OPERATION_FIX_PRICE",
        indexes = {@Index(columnList = "name")})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class FixPrice extends AuditableModel {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OPERATION_FIX_PRICE_SEQ")
  @SequenceGenerator(
          name = "OPERATION_FIX_PRICE_SEQ",
          sequenceName = "OPERATION_FIX_PRICE_SEQ",
          allocationSize = 1)
  private Long id;

  // 名称
  private String name;

  // 商品定价
  @OneToMany(
          fetch = FetchType.LAZY,
          mappedBy = "fixPrice",
          cascade = CascadeType.ALL,
          orphanRemoval = true)
  private List<Commodity> commodity;

  private String attrs;

}
