package com.uw.gearmax.gearmaxapi.util;

/**
 * Define name of search key used for sql specification builder
 */
public enum SpecSearchKey {

    PRICE("price"),
    YEAR("year"),
    BODY_TYPE("bodyType"),
    MAKE_NAME("makeName"),
    LISTING_COLOR("listingColor"),
    MILEAGE("mileage"),
    MAXIMUM_SEATING("maximumSeating"),
    TRANSMISSION_DISPLAY("transmissionDisplay");

    private final String s;

    SpecSearchKey(String s) {
        this.s = s;
    }

    public String val() {
        return s;
    }
}
