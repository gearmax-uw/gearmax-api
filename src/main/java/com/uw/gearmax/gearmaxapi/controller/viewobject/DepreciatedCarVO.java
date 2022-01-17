package com.uw.gearmax.gearmaxapi.controller.viewobject;

public class DepreciatedCarVO extends CarVO {
    private Boolean isFrameDamaged;
    private Boolean hasAccidents;
    private Boolean isSalvaged;
    private Boolean isCab;
    private Boolean isTheftTitle;

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
