package com.yn.printer.service.modules.advertisement.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Data
@Entity
@Table(name = "advertisement_placement_location")
public class AdvertisementPlacementLocation {
    @Basic
    @Id
    @Column(name = "advertisement_placement")
    private long placementId;
    @Basic
    @Column(name = "location")
    private long deviceId;
}