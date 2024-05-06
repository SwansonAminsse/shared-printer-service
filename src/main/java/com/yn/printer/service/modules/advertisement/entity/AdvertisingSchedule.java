package com.yn.printer.service.modules.advertisement.entity;

import com.google.common.base.MoreObjects;
import com.yn.printer.service.common.entity.AuditableModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "ADVERTISEMENT_ADVERTISING_SCHEDULE", indexes = { @Index(columnList = "placement") })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@ToString
public class AdvertisingSchedule extends AuditableModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADVERTISEMENT_ADVERTISING_SCHEDULE_SEQ")
    @SequenceGenerator(name = "ADVERTISEMENT_ADVERTISING_SCHEDULE_SEQ", sequenceName = "ADVERTISEMENT_ADVERTISING_SCHEDULE_SEQ", allocationSize = 1)
    private Long id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @JoinColumn(name = "placement")
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Placement placement;


}