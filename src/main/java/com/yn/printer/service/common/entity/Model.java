package com.yn.printer.service.common.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

/**
 * 基础实体
 *
 * @author huabiao
 * @create 2023/12/08  15:42
 **/
@MappedSuperclass
public abstract class Model {

    @Version
    private Integer version;

    @Transient
    private transient boolean selected;

    private Boolean archived;

    public abstract Long getId();

    public abstract void setId(Long id);

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Check whether the record is selected in the UI widget.
     *
     * @return selection state
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Set the selected state of the record. The UI widget will use this flag to mark/unmark the
     * selection state.
     *
     * @param selected selected state flag
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
