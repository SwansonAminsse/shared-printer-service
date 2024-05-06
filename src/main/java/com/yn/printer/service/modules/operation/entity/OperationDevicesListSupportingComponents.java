package com.yn.printer.service.modules.operation.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "operation_devices_list_supporting_components")
public class OperationDevicesListSupportingComponents {

    @Basic
    @Id
    @Column(name = "operation_devices_list")
    private long deviceId;
    @Basic
    @Column(name = "supporting_components")
    private long deviceMeatId;


}