package com.yn.printer.service.modules.meta.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "META_XING_HAO")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class XingHao extends AuditableModel implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "META_XING_HAO_SEQ")
  @SequenceGenerator(
          name = "META_XING_HAO_SEQ",
          sequenceName = "META_XING_HAO_SEQ",
          allocationSize = 1)
  private Long id;

  // 型号名称
  private String name;

  // 品牌
  @ManyToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "device_brand")
  private DeviceBrand deviceBrand;

  // A4纸打印机名称
  private String a4PrinterName = "EPSON WF-C5890 Series2";

  // 相纸打印机名称
  private String photoPrinterName = "EPSON WF-C5890 Series3";

}
