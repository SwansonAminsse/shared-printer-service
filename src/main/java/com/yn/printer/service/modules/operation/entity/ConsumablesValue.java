package com.yn.printer.service.modules.operation.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import com.yn.printer.service.modules.operation.enums.ConsumableType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(
        name = "OPERATION_CONSUMABLES_VALUE",
        indexes = {@Index(columnList = "name")})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ConsumablesValue extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OPERATION_CONSUMABLES_VALUE_SEQ")
    @SequenceGenerator(
            name = "OPERATION_CONSUMABLES_VALUE_SEQ",
            sequenceName = "OPERATION_CONSUMABLES_VALUE_SEQ",
            allocationSize = 1)
    private Long id;
//耗材类型
    @Enumerated(value = EnumType.STRING)
    private ConsumableType name;

//耗材值
    private Integer consumablesValue = 0;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "device")
    private DevicesList device;


}