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
@Table(name = "META_PRINT_TPYE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class PrintTpye extends AuditableModel implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "META_PRINT_TPYE_SEQ")
  @SequenceGenerator(
      name = "META_PRINT_TPYE_SEQ",
      sequenceName = "META_PRINT_TPYE_SEQ",
      allocationSize = 1)
  private Long id;


  private String printingType;


  private String paperType;


}
