package com.uw.gearmax.gearmaxapi.controller.viewobject;

import java.math.BigDecimal;

public class PickupTruckVO extends CarVO {
    private String bed;
    private BigDecimal bedLength;
    private String cabin;

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
