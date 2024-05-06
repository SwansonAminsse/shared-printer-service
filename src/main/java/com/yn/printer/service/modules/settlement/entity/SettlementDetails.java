package com.yn.printer.service.modules.settlement.entity;

import com.google.common.base.MoreObjects;
import com.yn.printer.service.common.entity.AuditableModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.yn.printer.service.modules.channel.entity.ChannelPartner;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import java.util.Objects;
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "SETTLEMENT_SETTLEMENT_DETAILS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class SettlementDetails extends AuditableModel implements Serializable {
    //渠道商结算明细表
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SETTLEMENT_SETTLEMENT_DETAILS_SEQ")
    @SequenceGenerator(name = "SETTLEMENT_SETTLEMENT_DETAILS_SEQ", sequenceName = "SETTLEMENT_SETTLEMENT_DETAILS_SEQ", allocationSize = 1)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "name")
    private DevicesList name;

    //设备收入
    private BigDecimal deviceIncome = BigDecimal.ZERO;

    //结算比例
    private Integer settlementRatio = 0;

    //应结算金额
    private BigDecimal settlementAmount = BigDecimal.ZERO;

    //已支付金额
    private BigDecimal alreadyPaid = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "channel_partner")
    private ChannelPartner channelPartner;

    //结算月份
    private LocalDate settlementMonth;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "channel_settlement")
    private ChannelSettlement channelSettlement;


    }