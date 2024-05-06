package com.yn.printer.service.modules.operation.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import com.yn.printer.service.modules.meta.enums.PaperType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "OPERATION_PAPER_TABLE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class PaperTable extends AuditableModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OPERATION_PAPER_TABLE_SEQ")
    @SequenceGenerator(
            name = "OPERATION_PAPER_TABLE_SEQ",
            sequenceName = "OPERATION_PAPER_TABLE_SEQ",
            allocationSize = 1)
    private Long id;

    // 设备
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "device")
    private DevicesList device;

    // 设备的纸张类型
    @Enumerated(value = EnumType.STRING)
    private PaperType name;

    // 剩余纸张
    private Integer residue = 0;


}
