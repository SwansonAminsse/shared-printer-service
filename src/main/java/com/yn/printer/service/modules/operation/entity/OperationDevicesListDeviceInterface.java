package com.yn.printer.service.modules.operation.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "operation_devices_list_device_interface")
public class OperationDevicesListDeviceInterface {
    @Basic
    @Id
    @Column(name = "operation_devices_list")
    private long deviceId;
    @Basic
    @Column(name = "device_interface")
    private long deviceInterfaceId;
}