package com.yn.printer.service.modules.member.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.*;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "MEMBER_MEMBER", indexes = {@Index(columnList = "name"), @Index(columnList = "member_level")})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Member extends AuditableModel implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_MEMBER_SEQ")
	@SequenceGenerator(name = "MEMBER_MEMBER_SEQ", sequenceName = "MEMBER_MEMBER_SEQ", allocationSize = 1)
	private Long id;

	// 会员名称
	private String name;

	// 微信号

	private String wxAccount;

	// 电话号

	private String phoneNumber;

	// 入会日期
	private LocalDate joiningDate;

	// 会员等级
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "member_level")
	private MemberLevel memberLevel;

	// 累计积分
	private Integer accumulatedPoints = 0;

	// 已消耗积分
	private Integer consumedPoints = 0;

	// 累计消费金额
	private BigDecimal accumulatedAmount = BigDecimal.ZERO;

	// 账户余额
	private BigDecimal accountBalance = BigDecimal.ZERO;

	// 状态
	private Boolean status = Boolean.FALSE;

	// openId
	private String openId;

	//头像路径
	private String imageUrl;


	private String password;


	private String email;


}
