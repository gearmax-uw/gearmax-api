package com.uw.gearmax.gearmaxapi.util;

/**
 * Define name of request parameters used in request url
 */
public enum UrlParameter {
    PAGE_INDEX("pageIndex"),
    PAGE_SIZE("pageSize"),
    SORT_FIELD("sort"),
    SORT_ORDER("sortOrder"),
    PRICE("price"),
    YEAR("year"),
    BODY_TYPE("bodyType"),
    MAKE_NAME("makeName"),
    LISTING_COLOR("listingColor"),
    MILEAGE("mileage"),
    MAXIMUM_SEATING("maximumSeating"),
    TRANSMISSION("transmission"),
    TRANSMISSION_DISPLAY("transmissionDisplay"),
    FUEL_TYPE("fuelType"),
    CITY("city"),
    FEATURES("features"),
    SEARCH("search");

    private final String s;

    UrlParameter(String s) {
        this.s = s;
    }

    public String val() {
        return s;
    }
}
