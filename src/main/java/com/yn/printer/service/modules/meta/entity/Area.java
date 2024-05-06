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
@Table(
    name = "META_AREA",
    indexes = {
      @Index(columnList = "code"),
      @Index(columnList = "name"),
      @Index(columnList = "area")
    })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Area extends AuditableModel implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "META_AREA_SEQ")
  @SequenceGenerator(name = "META_AREA_SEQ", sequenceName = "META_AREA_SEQ", allocationSize = 1)
  private Long id;

  private String code;

  private String name;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "area")
  private Area area;

}
