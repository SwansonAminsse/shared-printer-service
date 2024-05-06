package com.yn.printer.service.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yn.printer.service.modules.auth.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author huabiao
 * @create 2023/12/08  15:42
 **/
@MappedSuperclass
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler", "createdBy", "updatedBy"})
public abstract class AuditableModel extends Model {

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "createdBy")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "updatedBy")
    private User updatedBy;

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    @Access(AccessType.FIELD)
    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    @Access(AccessType.FIELD)
    private void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    @Access(AccessType.FIELD)
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    @Access(AccessType.FIELD)
    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }
}
