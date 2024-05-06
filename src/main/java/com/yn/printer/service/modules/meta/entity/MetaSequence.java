package com.yn.printer.service.modules.meta.entity;

import com.google.common.base.MoreObjects;
import com.yn.printer.service.common.entity.AuditableModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * 序列
 *
 * @author huabiao
 * @create 2023/12/8  16:56
 **/
@Entity
@Table(name = "META_SEQUENCE")
public class MetaSequence extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(unique = true)
    private String name;

    private String prefix;

    private String suffix;

    @NotNull
    private Integer padding = 0;

    @NotNull
    @Column(name = "increment_by")
    private Integer increment = 1;

    @NotNull
    @Column(name = "initial_value")
    private Long initial = 0L;

    @NotNull
    @Column(name = "next_value")
    private Long next = 1L;

    public MetaSequence() {
    }

    public MetaSequence(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Integer getPadding() {
        return padding == null ? 0 : padding;
    }

    public void setPadding(Integer padding) {
        this.padding = padding;
    }

    public Integer getIncrement() {
        return increment == null ? 0 : increment;
    }

    public void setIncrement(Integer increment) {
        this.increment = increment;
    }

    public Long getInitial() {
        return initial == null ? 0L : initial;
    }

    public void setInitial(Long initial) {
        this.initial = initial;
    }

    public Long getNext() {
        return next == null ? 0L : next;
    }

    public void setNext(Long next) {
        this.next = next;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MetaSequence)) {
            return false;
        }

        final MetaSequence other = (MetaSequence) obj;
        if (this.getId() != null || other.getId() != null) {
            return Objects.equals(this.getId(), other.getId());
        }

        if (!Objects.equals(getName(), other.getName())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(1110012166, this.getName());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", getId())
                .add("name", getName())
                .add("prefix", getPrefix())
                .add("suffix", getSuffix())
                .add("padding", getPadding())
                .add("increment", getIncrement())
                .add("initial", getInitial())
                .add("next", getNext())
                .omitNullValues()
                .toString();
    }
}
