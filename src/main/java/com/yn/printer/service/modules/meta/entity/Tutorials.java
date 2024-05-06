package com.yn.printer.service.modules.meta.entity;

import com.google.common.base.MoreObjects;
import com.yn.printer.service.common.entity.AuditableModel;
import com.yn.printer.service.modules.operation.enums.TutorialTypes;
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
@Table(name = "META_TUTORIALS", indexes = { @Index(columnList = "content") })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Tutorials extends AuditableModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "META_TUTORIALS_SEQ")
    @SequenceGenerator(name = "META_TUTORIALS_SEQ", sequenceName = "META_TUTORIALS_SEQ", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "content")
    private MetaFile content;
    @Enumerated(value = EnumType.STRING)
    private TutorialTypes tutorialType;
}
