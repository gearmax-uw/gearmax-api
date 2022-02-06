package com.uw.gearmax.gearmaxapi.util;

public enum FieldVal {

    PICKUP_TRUCK("Pickup Truck");

    private final String s;

    FieldVal(String s) {
        this.s = s;
    }

    public String val() {
        return s;
    }
}
