package com.yn.printer.service.modules.settlement.entity;

import com.google.common.base.MoreObjects;
import com.yn.printer.service.common.entity.AuditableModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import com.yn.printer.service.modules.channel.entity.ChannelPartner;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "SETTLEMENT_CHANNEL_SETTLEMENT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ChannelSettlement extends AuditableModel implements Serializable {
//渠道结算表
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SETTLEMENT_CHANNEL_SETTLEMENT_SEQ")
    @SequenceGenerator(name = "SETTLEMENT_CHANNEL_SETTLEMENT_SEQ", sequenceName = "SETTLEMENT_CHANNEL_SETTLEMENT_SEQ", allocationSize = 1)
    private Long id;

    //结算月份
    private LocalDate settlementMonth;

    //渠道商名称
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "name")
    private ChannelPartner name;

    //应结算金额
    private BigDecimal settlementAmount = BigDecimal.ZERO;

    //已支付金额
    private BigDecimal alreadyPaid = BigDecimal.ZERO;

    //结算明细
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "channelSettlement",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<SettlementDetails> settlementDetails;


}
