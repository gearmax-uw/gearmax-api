package com.uw.gearmax.gearmaxapi.controller.viewobject;

public class DepreciatedPickupTruckVO extends CarVO {
    // todo: add attributes
    private Boolean isFrameDamaged;
    private Boolean hasAccidents;
    private Boolean isSalvaged;
    private Boolean isCab;
    private Boolean isTheftTitle;
    private String bed;
    private BigDecimal bedLength;
    private String cabin;
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
     public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public BigDecimal getBedLength() {
        return bedLength;
    }

    public void setBedLength(BigDecimal bedLength) {
        this.bedLength = bedLength;
    }

    public String getCabin() {
        return cabin;
    }

    public void setCabin(String cabin) {
        this.cabin = cabin;
    }
}
