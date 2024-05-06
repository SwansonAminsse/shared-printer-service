package com.yn.printer.service.modules.advertisement.entity;

import com.google.common.base.MoreObjects;
import com.yn.printer.service.common.entity.AuditableModel;
import com.yn.printer.service.modules.advertisement.enums.Screen;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "ADVERTISEMENT_PLACEMENT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@ToString(exclude = "advertisingSchedule")
public class Placement extends AuditableModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADVERTISEMENT_PLACEMENT_SEQ")
    @SequenceGenerator(name = "ADVERTISEMENT_PLACEMENT_SEQ", sequenceName = "ADVERTISEMENT_PLACEMENT_SEQ", allocationSize = 1)
    private Long id;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "placement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdvertisingSchedule> advertisingSchedule;


    private String name;

    private BigDecimal pricing = BigDecimal.ZERO;

    @Enumerated(value = EnumType.STRING)
    private Screen screen;

    private Integer intervalTime;

    private Boolean acquiesce;

}