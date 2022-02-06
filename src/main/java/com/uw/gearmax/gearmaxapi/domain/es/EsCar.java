package com.uw.gearmax.gearmaxapi.domain.es;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(indexName = "used-car")
public class EsCar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Field(name = "id", type = FieldType.Long)
    private Long id;
    @Field(name = "back_legroom", type = FieldType.Double)
    private BigDecimal backLegroom;
    @Field(name = "body_type", type = FieldType.Keyword, index = false)
    private String bodyType = "";
    @Field(name = "city", type = FieldType.Keyword, index = false)
    private String city = "";
    @Field(name = "city_fuel_economy", type = FieldType.Integer)
    private Integer cityFuelEconomy;
    @Field(name = "country", type = FieldType.Keyword, index = false)
    private String country = "";
    @Field(name = "engine_displacement", type = FieldType.Integer)
    private Integer engineDisplacement;
    @Field(name = "engine_type", type = FieldType.Keyword, index = false)
    private String engineType = "";
    @Field(name = "exterior_color", type = FieldType.Keyword, index = false)
    private String exteriorColor = "";
    @Field(name = "front_legroom", type = FieldType.Double)
    private BigDecimal frontLegroom;
    @Field(name = "fuel_tank_volume", type = FieldType.Double)
    private BigDecimal fuelTankVolume;
    @Field(name = "fuel_type", type = FieldType.Keyword, index = false)
    private String fuelType = "";
    @Field(name = "height", type = FieldType.Double)
    private BigDecimal height;
    @Field(name = "highway_fuel_economy", type = FieldType.Integer)
    private Integer highwayFuelEconomy;
    @Field(name = "interior_color", type = FieldType.Keyword, index = false)
    private String interiorColor = "";
    @Field(name = "is_new", type = FieldType.Boolean)
    private Boolean isNew = false;
    @Field(name = "length", type = FieldType.Double)
    private BigDecimal length;
    @Field(name = "listed_date", type = FieldType.Date)
    private LocalDate listedDate;
    @Field(name = "main_picture_url", type = FieldType.Keyword, index = false)
    private String mainPictureUrl = "";
    @Field(name = "major_options", type = FieldType.Keyword)
    private List<String> majorOptions = new ArrayList<>();
    @Field(name = "make_name", type = FieldType.Keyword, index = false)
    private String makeName = "";
    @Field(name = "maximum_seating", type = FieldType.Integer)
    private Integer maximumSeating;
    @Field(name = "mileage", type = FieldType.Integer)
    private Integer mileage;
    @Field(name = "model_name", type = FieldType.Keyword, index = false)
    private String modelName = "";
    @Field(name = "owner_count", type = FieldType.Integer)
    private Integer ownerCount;
    @Field(name = "power", type = FieldType.Keyword, index = false)
    private String power = "";
    @Field(name = "price", type = FieldType.Integer)
    private Integer price;
    @Field(name = "seller_rating", type = FieldType.Double)
    private BigDecimal sellerRating;
    @Field(name = "torque", type = FieldType.Keyword, index = false)
    private String torque = "";
    @Field(name = "transmission", type = FieldType.Keyword, index = false)
    private String transmission = "";
    @Field(name = "transmission_display", type = FieldType.Keyword, index = false)
    private String transmissionDisplay = "";
    @Field(name = "trim_name", type = FieldType.Keyword, index = false)
    private String trimName = "";
    @Field(name = "vin", type = FieldType.Keyword, index = false)
    private String vin = "";
    @Field(name = "wheel_system", type = FieldType.Keyword, index = false)
    private String wheelSystem = "";
    @Field(name = "wheel_system_display", type = FieldType.Keyword, index = false)
    private String wheelSystemDisplay = "";
    @Field(name = "wheelbase", type = FieldType.Double)
    private BigDecimal wheelBase;
    @Field(name = "width", type = FieldType.Double)
    private BigDecimal width;
    @Field(name = "year", type = FieldType.Integer)
    private Integer year;
    @Field(name = "zip", type = FieldType.Keyword, index = false)
    private String zip = "";
    @Field(name = "is_depreciated", type = FieldType.Boolean)
    private Boolean isDepreciated = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBackLegroom() {
        return backLegroom;
    }

    public void setBackLegroom(BigDecimal backLegroom) {
        this.backLegroom = backLegroom;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCityFuelEconomy() {
        return cityFuelEconomy;
    }

    public void setCityFuelEconomy(Integer cityFuelEconomy) {
        this.cityFuelEconomy = cityFuelEconomy;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getEngineDisplacement() {
        return engineDisplacement;
    }

    public void setEngineDisplacement(Integer engineDisplacement) {
        this.engineDisplacement = engineDisplacement;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String getExteriorColor() {
        return exteriorColor;
    }

    public void setExteriorColor(String exteriorColor) {
        this.exteriorColor = exteriorColor;
    }

    public BigDecimal getFrontLegroom() {
        return frontLegroom;
    }

    public void setFrontLegroom(BigDecimal frontLegroom) {
        this.frontLegroom = frontLegroom;
    }

    public BigDecimal getFuelTankVolume() {
        return fuelTankVolume;
    }

    public void setFuelTankVolume(BigDecimal fuelTankVolume) {
        this.fuelTankVolume = fuelTankVolume;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public Integer getHighwayFuelEconomy() {
        return highwayFuelEconomy;
    }

    public void setHighwayFuelEconomy(Integer highwayFuelEconomy) {
        this.highwayFuelEconomy = highwayFuelEconomy;
    }

    public String getInteriorColor() {
        return interiorColor;
    }

    public void setInteriorColor(String interiorColor) {
        this.interiorColor = interiorColor;
    }

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public LocalDate getListedDate() {
        return listedDate;
    }

    public void setListedDate(LocalDate listedDate) {
        this.listedDate = listedDate;
    }

    public String getMainPictureUrl() {
        return mainPictureUrl;
    }

    public void setMainPictureUrl(String mainPictureUrl) {
        this.mainPictureUrl = mainPictureUrl;
    }

    public List<String> getMajorOptions() {
        return majorOptions;
    }

    public void setMajorOptions(List<String> majorOptions) {
        this.majorOptions = majorOptions;
    }

    public String getMakeName() {
        return makeName;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }

    public Integer getMaximumSeating() {
        return maximumSeating;
    }

    public void setMaximumSeating(Integer maximumSeating) {
        this.maximumSeating = maximumSeating;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getOwnerCount() {
        return ownerCount;
    }

    public void setOwnerCount(Integer ownerCount) {
        this.ownerCount = ownerCount;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public BigDecimal getSellerRating() {
        return sellerRating;
    }

    public void setSellerRating(BigDecimal sellerRating) {
        this.sellerRating = sellerRating;
    }

    public String getTorque() {
        return torque;
    }

    public void setTorque(String torque) {
        this.torque = torque;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getTransmissionDisplay() {
        return transmissionDisplay;
    }

    public void setTransmissionDisplay(String transmissionDisplay) {
        this.transmissionDisplay = transmissionDisplay;
    }

    public String getTrimName() {
        return trimName;
    }

    public void setTrimName(String trimName) {
        this.trimName = trimName;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getWheelSystem() {
        return wheelSystem;
    }

    public void setWheelSystem(String wheelSystem) {
        this.wheelSystem = wheelSystem;
    }

    public String getWheelSystemDisplay() {
        return wheelSystemDisplay;
    }

    public void setWheelSystemDisplay(String wheelSystemDisplay) {
        this.wheelSystemDisplay = wheelSystemDisplay;
    }

    public BigDecimal getWheelBase() {
        return wheelBase;
    }

    public void setWheelBase(BigDecimal wheelBase) {
        this.wheelBase = wheelBase;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Boolean getDepreciated() {
        return isDepreciated;
    }

    public void setDepreciated(Boolean depreciated) {
        isDepreciated = depreciated;
    }
}
