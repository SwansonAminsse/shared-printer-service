package com.yn.printer.service.modules.member.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "MEMBER_MEMBER_LEVEL", indexes = {@Index(columnList = "name")})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class MemberLevel extends AuditableModel implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_MEMBER_LEVEL_SEQ")
	@SequenceGenerator(name = "MEMBER_MEMBER_LEVEL_SEQ", sequenceName = "MEMBER_MEMBER_LEVEL_SEQ", allocationSize = 1)
	private Long id;

	// 等级名称
	private String name;

	// 最小积分
	private Integer minPoints = 0;

	// 最大积分
	private Integer maxPoints = 0;

	// 优惠折扣率
	private Integer discountRate = 0;

}
