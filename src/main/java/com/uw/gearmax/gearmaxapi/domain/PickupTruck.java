package com.uw.gearmax.gearmaxapi.domain;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class PickupTruck {
    @Id
    private Long id;

    @Column(name = "bed", nullable = false)
    private String bed = "";

    @Column(name = "bed_height", nullable = false)
    @Digits(integer = 2, fraction = 1)
    private BigDecimal bedHeight = BigDecimal.valueOf(0.0);

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