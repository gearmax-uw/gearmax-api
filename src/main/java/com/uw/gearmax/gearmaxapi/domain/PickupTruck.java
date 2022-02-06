package com.uw.gearmax.gearmaxapi.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Entity(name = "pickup_truck")
public class PickupTruck {
    @Id
    private Long id;

    @Column(name = "bed", nullable = false)
    private String bed = "";

    @Column(name = "bed_length", nullable = false)
    @Digits(integer = 2, fraction = 1)
    private BigDecimal bedLength = BigDecimal.valueOf(0.0);

    @Column(name = "cabin", nullable = false)
    private String cabin = "";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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