package com.yn.printer.service.modules.meta.entity;

import com.google.common.base.MoreObjects;
import com.yn.printer.service.common.entity.AuditableModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "META_ADVERTISER")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Advertiser extends AuditableModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "META_ADVERTISER_SEQ")
    @SequenceGenerator(
            name = "META_ADVERTISER_SEQ",
            sequenceName = "META_ADVERTISER_SEQ",
            allocationSize = 1)
    private Long id;


    private String name;


}