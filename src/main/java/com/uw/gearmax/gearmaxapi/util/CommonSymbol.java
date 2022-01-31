package com.uw.gearmax.gearmaxapi.util;

public enum CommonSymbol {

    DASH("-");

    private final String s;

    CommonSymbol(String s) {
        this.s = s;
    }

    public String val() {
        return s;
    }
}
