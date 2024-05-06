package com.yn.printer.service.modules.member.entity;

import com.google.common.base.MoreObjects;
import com.yn.printer.service.common.entity.AuditableModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "MEMBER_CHARGE_FILE", indexes = { @Index(columnList = "member") })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ChargeFile extends AuditableModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_CHARGE_FILE_SEQ")
    @SequenceGenerator(name = "MEMBER_CHARGE_FILE_SEQ", sequenceName = "MEMBER_CHARGE_FILE_SEQ", allocationSize = 1)
    private Long id;

    //会员
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "member")
    private Member member;

    //增加
    private Boolean increasing = Boolean.FALSE;

    //充值时间
    private LocalDateTime time;

    //充值额
    private BigDecimal refillAmount = BigDecimal.ZERO;

    //实付金额
    private BigDecimal payAmount = BigDecimal.ZERO;

    //值折扣
    private Integer discount = 0;

    //减少
    private Boolean low = Boolean.FALSE;


}