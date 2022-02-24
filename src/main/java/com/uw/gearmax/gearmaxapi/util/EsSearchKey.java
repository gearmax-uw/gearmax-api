package com.uw.gearmax.gearmaxapi.util;

/**
 * Define name of search key used for elasticsearch query builder
 */
public enum EsSearchKey {

    PRICE("price"),
    BODY_TYPE("body_type"),
    MAKE_NAME("make_name"),
    LISTING_COLOR("listing_color"),
    EXTERIOR_COLOR("exterior_color"),
    INTERIOR_COLOR("interior_color"),
    YEAR("year"),
    MILEAGE("mileage"),
    MAXIMUM_SEATING("maximum_seating"),
    TRANSMISSION("transmission"),
    TRANSMISSION_DISPLAY("transmission_display"),
    FUEL_TYPE("fuel_type"),
    CITY("city"),
    COUNTRY("country"),
    ZIP("zip"),
    MAJOR_OPTIONS("major_options"),
    MODEL_NAME("model_name"),
    WHEEL_SYSTEM("wheel_system"),
    WHEEL_SYSTEM_DISPLAY("wheel_system_display"),
    TRIM_NAME("trim_name"),
    ENGINE_TYPE("engine_type");

    private final String s;

    EsSearchKey(String s) {
        this.s = s;
    }

    public String val() {
        return s;
    }

    public String keywordVal() {
        return s + ".keyword";
    }
}
