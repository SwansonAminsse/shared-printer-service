package com.yn.printer.service.modules.settlement.entity;

import com.google.common.base.MoreObjects;
import com.yn.printer.service.common.entity.AuditableModel;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "SETTLEMENT_EQUIPMENT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

public class Equipment extends AuditableModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SETTLEMENT_EQUIPMENT_SEQ")
    @SequenceGenerator(name = "SETTLEMENT_EQUIPMENT_SEQ", sequenceName = "SETTLEMENT_EQUIPMENT_SEQ", allocationSize = 1)
    private Long id;

//结算月份
    private LocalDate settlementMonth;

    //设备名称
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "name")
    private DevicesList name;

    //设备收入
    private BigDecimal revenue = BigDecimal.ZERO;

    //F商结算比例
    private Integer settlementRatio1 = 0;

    //B商结算比例
    private Integer settlementRatio2 = 0;

    //b商结算比例
    private Integer settlementRatio3 = 0;

    //R商结算比例
    private Integer settlementRatio4 = 0;

    //F商支付金额
    private BigDecimal paymentAmount1 = BigDecimal.ZERO;

    //B商支付金额
    private BigDecimal paymentAmount2 = BigDecimal.ZERO;

    //b商支付金额
    private BigDecimal paymentAmount3 = BigDecimal.ZERO;

    //R商支付金额
    private BigDecimal paymentAmount4 = BigDecimal.ZERO;

    //F商应结金额
    private BigDecimal dueAmount1 = BigDecimal.ZERO;

    //B商应结金额
    private BigDecimal dueAmount2 = BigDecimal.ZERO;

    //b商应结金额
    private BigDecimal dueAmount3 = BigDecimal.ZERO;

    //R商应结金额
    private BigDecimal dueAmount4 = BigDecimal.ZERO;

}
