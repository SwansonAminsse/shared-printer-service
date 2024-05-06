package com.yn.printer.service.modules.advertisement.entity;

import com.google.common.base.MoreObjects;
import com.yn.printer.service.common.entity.AuditableModel;
import com.yn.printer.service.modules.meta.entity.Advertiser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import com.yn.printer.service.modules.meta.entity.MetaFile;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(
        name = "ADVERTISEMENT_REGISTRATION")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Registration extends AuditableModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADVERTISEMENT_REGISTRATION_SEQ")
    @SequenceGenerator(
            name = "ADVERTISEMENT_REGISTRATION_SEQ",
            sequenceName = "ADVERTISEMENT_REGISTRATION_SEQ",
            allocationSize = 1)
    private Long id;

    //广告内容
    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "content")
    private MetaFile content;

    //广告名称
    private String name;

    //投放厂家
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "advertiser")
    private Advertiser advertiser;

}