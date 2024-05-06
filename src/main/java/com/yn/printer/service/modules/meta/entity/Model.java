package com.yn.printer.service.modules.meta.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "META_MODEL", indexes = {@Index(columnList = "name")})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Model extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "META_MODEL_SEQ")
	@SequenceGenerator(name = "META_MODEL_SEQ", sequenceName = "META_MODEL_SEQ", allocationSize = 1)
	private Long id;

	// 名称
	private String name;

	private String attrs;
}
