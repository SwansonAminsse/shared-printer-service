package com.yn.printer.service.modules.operation.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "OPERATION_DEVICE_META")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class DeviceMeta extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OPERATION_DEVICE_META_SEQ")
	@SequenceGenerator(name = "OPERATION_DEVICE_META_SEQ", sequenceName = "OPERATION_DEVICE_META_SEQ", allocationSize = 1)
	private Long id;

	// 组件装置类型
	private String componentType;

	// 组件名称
	private String name;

}
