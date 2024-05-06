package com.yn.printer.service.modules.advertisement.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "advertisement_placement_registration")
public class AdvertisementPlacementRegistration {
    @Basic
    @Id
    @Column(name = "advertisement_placement")
    private long placementId;
    @Basic
    @Column(name = "registration")
    private long registrationId;
}