package com.yn.printer.service.modules.member.entity;

import com.yn.printer.service.common.entity.AuditableModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Cacheable(false)
@Table(name = "MEMBER_POINTS_FILE", indexes = { @Index(columnList = "member") })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class PointsFile extends AuditableModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_POINTS_FILE_SEQ")
    @SequenceGenerator(name = "MEMBER_POINTS_FILE_SEQ", sequenceName = "MEMBER_POINTS_FILE_SEQ", allocationSize = 1)
    private Long id;
    //会员
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "member")
    private Member member;
    //增加
    private Boolean increasing = Boolean.FALSE;

    //积分
    private Integer points = 0;

    //过期时间
    private LocalDateTime expires;

    //减少
    private Boolean low = Boolean.FALSE;

}