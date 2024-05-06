package com.yn.printer.service.modules.meta.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import com.yn.printer.service.modules.meta.enums.PaperType;
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
        name = "META_PAPER_WARNING",
        indexes = {@Index(columnList = "xing_hao")})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class PaperWarning extends AuditableModel implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "META_PAPER_WARNING_SEQ")
  @SequenceGenerator(
          name = "META_PAPER_WARNING_SEQ",
          sequenceName = "META_PAPER_WARNING_SEQ",
          allocationSize = 1)
  private Long id;

  // 缺纸预警张数
  private Integer paperWarningCount = 0;

  // 型号
  @ManyToOne(
          fetch = FetchType.LAZY,
          cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "xing_hao")
  private XingHao xingHao;

  // 纸张类型
  @Enumerated(value = EnumType.STRING)
  private PaperType paperType;

  //最大纸张余量
  private Integer maxPaperResidue = 0;

}
