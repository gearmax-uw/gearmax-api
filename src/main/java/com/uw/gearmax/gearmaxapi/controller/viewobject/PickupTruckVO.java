package com.uw.gearmax.gearmaxapi.controller.viewobject;

import java.math.BigDecimal;

public class PickupTruckVO {
    private String bed;
    private BigDecimal bedHeight;
    private BigDecimal bedLength;
    private String cabin;

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public BigDecimal getBedHeight() {
        return bedHeight;
    }

    public void setBedHeight(BigDecimal bedHeight) {
        this.bedHeight = bedHeight;
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
