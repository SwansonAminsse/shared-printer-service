package com.yn.printer.service.modules.advertisement.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(
        name = "ADVERTISEMENT_PLACEMENT_PAYMENT",
        indexes = {@Index(columnList = "name"), @Index(columnList = "placement")})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class PlacementPayment  extends AuditableModel implements Serializable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ADVERTISEMENT_PLACEMENT_PAYMENT_SEQ")
    @SequenceGenerator(
            name = "ADVERTISEMENT_PLACEMENT_PAYMENT_SEQ",
            sequenceName = "ADVERTISEMENT_PLACEMENT_PAYMENT_SEQ",
            allocationSize = 1)
    private Long id;

    //广告名称
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "name")
    private Registration name;

    //活动名称
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "placement")
    private Placement placement;

    //收款时间
    private LocalDateTime time;

    //应收款
    private BigDecimal accountsReceivable = BigDecimal.ZERO;

    //本次收款
    private BigDecimal currentReceipt = BigDecimal.ZERO;
}