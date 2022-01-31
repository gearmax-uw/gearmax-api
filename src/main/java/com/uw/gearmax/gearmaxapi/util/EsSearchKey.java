package com.uw.gearmax.gearmaxapi.util;

/**
 * Define name of search key used for elasticsearch query builder
 */
public enum EsSearchKey {

    PRICE("price"),
    BODY_TYPE("body_type"),
    MAKE_NAME("make_name"),
    LISTING_COLOR("listing_color"),
    YEAR("year"),
    MILEAGE("mileage"),
    MAXIMUM_SEATING("maximum_seating"),
    TRANSMISSION_DISPLAY("transmission_display");

    private final String s;

    EsSearchKey(String s) {
        this.s = s;
    }

    public String val() {
        return s;
    }
}
