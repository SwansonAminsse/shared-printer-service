package com.yn.printer.service.modules.operation.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import com.yn.printer.service.modules.meta.enums.PrintColor;
import com.yn.printer.service.modules.meta.enums.PrintFaces;
import com.yn.printer.service.modules.meta.enums.PrintType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "OPERATION_GOODS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Goods extends AuditableModel {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OPERATION_GOODS_SEQ")
  @SequenceGenerator(
          name = "OPERATION_GOODS_SEQ",
          sequenceName = "OPERATION_GOODS_SEQ",
          allocationSize = 1)
  private Long id;

  // 商品名
  private String name;

  // 打印类型
  @Enumerated(EnumType.STRING)
  private PrintType printType;

  // 打印颜色
  @Enumerated(EnumType.STRING)
  private PrintColor printColor;

  // 打印面数
  @Enumerated(EnumType.STRING)
  private PrintFaces printFaces;

  // 商品定价
  @OneToMany(
          fetch = FetchType.LAZY,
          mappedBy = "goods",
          cascade = CascadeType.ALL,
          orphanRemoval = true)
  private List<Commodity> commodity;

  private String attrs;
}
