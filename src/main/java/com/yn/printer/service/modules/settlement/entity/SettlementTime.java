package com.yn.printer.service.modules.settlement.entity;

import com.google.common.base.MoreObjects;
import com.yn.printer.service.common.entity.AuditableModel;
import com.yn.printer.service.modules.settlement.enums.Month;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Objects;
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "SETTLEMENT_SETTLEMENT_TIME")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class SettlementTime extends AuditableModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SETTLEMENT_SETTLEMENT_TIME_SEQ")
    @SequenceGenerator(name = "SETTLEMENT_SETTLEMENT_TIME_SEQ", sequenceName = "SETTLEMENT_SETTLEMENT_TIME_SEQ", allocationSize = 1)
    private Long id;

    //结算日
    @Min(1)
    @Max(28)
    private Integer settlementDay = 0;

    //开始日
    @Min(1)
    @Max(28)
    private Integer startDay = 0;

    //结束日
    @Min(1)
    @Max(28)
    private Integer endDay = 0;

    //开始月
    @Enumerated(value = EnumType.STRING)
    private Month startMonth;

    //结束月
    @Enumerated(value = EnumType.STRING)
    private Month endMonth;


}
