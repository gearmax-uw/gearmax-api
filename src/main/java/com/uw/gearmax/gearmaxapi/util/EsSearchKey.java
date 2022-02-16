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
    TRANSMISSION("transmission"),
    TRANSMISSION_DISPLAY("transmission_display"),
    FUEL_TYPE("fuel_type"),
    CITY("city"),
    MAJOR_OPTIONS("major_options"),
    MODEL_NAME("model_name"),
    WHEEL_SYSTEM("wheel_system"),
    TRIM_NAME("trim_name");

    private final String s;

    EsSearchKey(String s) {
        this.s = s;
    }

    public String val() {
        return s;
    }
}
