package com.uw.gearmax.gearmaxapi.response;

public class MetaData {
    private long totalElements;

    public MetaData(long totalElements) {
        this.totalElements = totalElements;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
