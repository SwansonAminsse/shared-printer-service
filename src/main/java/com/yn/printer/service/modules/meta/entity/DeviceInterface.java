package com.yn.printer.service.modules.meta.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "META_DEVICE_INTERFACE", indexes = { @Index(columnList = "name") })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class DeviceInterface extends AuditableModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "META_DEVICE_INTERFACE_SEQ")
    @SequenceGenerator(name = "META_DEVICE_INTERFACE_SEQ", sequenceName = "META_DEVICE_INTERFACE_SEQ", allocationSize = 1)
    private Long id;

    //接口名称
    private String name;

    //备注
    private String remark;

    //接口地址
    private String interfaceAddress;

}