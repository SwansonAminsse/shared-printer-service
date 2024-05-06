package com.yn.printer.service.modules.meta.entity;

import com.google.common.base.MoreObjects;

import com.yn.printer.service.common.entity.AuditableModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "META_STREET", indexes = { @Index(columnList = "district"), @Index(columnList = "name"), @Index(columnList = "code") })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Street extends AuditableModel implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "META_STREET_SEQ")
	@SequenceGenerator(name = "META_STREET_SEQ", sequenceName = "META_STREET_SEQ", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "district")
	private Area district;

	private String name;


	private Integer code = 0;


	private String point;


	private BigDecimal upperLongitude = BigDecimal.ZERO;


	private BigDecimal lowerlongitude = BigDecimal.ZERO;


	private BigDecimal lowerDimension = BigDecimal.ZERO;


	private BigDecimal capDimension = BigDecimal.ZERO;

}
