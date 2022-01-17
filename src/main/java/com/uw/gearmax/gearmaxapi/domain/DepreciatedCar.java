package com.uw.gearmax.gearmaxapi.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "depreciation_info")
public class DepreciatedCar {

    @Id
    private Long id;

    @Column(name = "frame_damaged", nullable = false)
    private Boolean isFrameDamaged;

    @Column(name = "has_accidents", nullable = false)
    private Boolean hasAccidents;

    @Column(name = "salvage", nullable = false)
    private Boolean isSalvaged;

    @Column(name = "isCab", nullable = false)
    private Boolean isCab;

    @Column(name = "isTheftTitle", nullable = false)
    private Boolean isTheftTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getFrameDamaged() {
        return isFrameDamaged;
    }

    public void setFrameDamaged(Boolean frameDamaged) {
        isFrameDamaged = frameDamaged;
    }

    public Boolean getHasAccidents() {
        return hasAccidents;
    }

    public void setHasAccidents(Boolean hasAccidents) {
        this.hasAccidents = hasAccidents;
    }

    public Boolean getSalvaged() {
        return isSalvaged;
    }

    public void setSalvaged(Boolean salvaged) {
        isSalvaged = salvaged;
    }

    public Boolean getCab() {
        return isCab;
    }

    public void setCab(Boolean cab) {
        isCab = cab;
    }

    public Boolean getTheftTitle() {
        return isTheftTitle;
    }

    public void setTheftTitle(Boolean theftTitle) {
        isTheftTitle = theftTitle;
    }
}
