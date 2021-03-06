package com.uw.gearmax.gearmaxapi.controller.viewobject;

import java.time.LocalDate;
import java.util.List;

public class CarVO {

    private Long id;
    private String vin;
    private Integer price;
    private Integer year;
    private String bodyType;
    private String makeName;
    private String modelName;
    private String exteriorColor;
    private String interiorColor;
    private String listingColor;
    private Integer mileage;
    private Integer maximumSeating;
    private String transmissionDisplay;
    private String zip;
    private String city;
    private String country;
    private String mainPictureUrl;
    private LocalDate listedDate;
    private List<String> majorOptions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getMakeName() {
        return makeName;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }

    public String getExteriorColor() {
        return exteriorColor;
    }

    public void setExteriorColor(String exteriorColor) {
        this.exteriorColor = exteriorColor;
    }

    public String getInteriorColor() {
        return interiorColor;
    }

    public void setInteriorColor(String interiorColor) {
        this.interiorColor = interiorColor;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public Integer getMaximumSeating() {
        return maximumSeating;
    }

    public void setMaximumSeating(Integer maximumSeating) {
        this.maximumSeating = maximumSeating;
    }

    public String getTransmissionDisplay() {
        return transmissionDisplay;
    }

    public void setTransmissionDisplay(String transmissionDisplay) {
        this.transmissionDisplay = transmissionDisplay;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getListedDate() {
        return listedDate;
    }

    public void setListedDate(LocalDate listedDate) {
        this.listedDate = listedDate;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getListingColor() {
        return listingColor;
    }

    public void setListingColor(String listingColor) {
        this.listingColor = listingColor;
    }

    public List<String> getMajorOptions() {
        return majorOptions;
    }

    public void setMajorOptions(List<String> majorOptions) {
        this.majorOptions = majorOptions;
    }

    public String getMainPictureUrl() {
        return mainPictureUrl;
    }

    public void setMainPictureUrl(String mainPictureUrl) {
        this.mainPictureUrl = mainPictureUrl;
    }
}
