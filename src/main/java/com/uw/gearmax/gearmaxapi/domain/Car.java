package com.uw.gearmax.gearmaxapi.domain;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vin", nullable = false)
    @NotEmpty(message = "VIN cannot be empty")
    private String vin = "";

    @Column(name = "seller_id", nullable = false)
    private Integer sellerId;

    @Column(name = "make_name", nullable = false)
    private String makeName = "";

    @Column(name = "model_name", nullable = false)
    private String modelName = "";

    @Column(name = "price", nullable = false)
    @NotNull(message = "Price cannot be empty")
    @Max(value = 999999999, message = "Price must be between 0-999999999")
    private Integer price;

    @Column(name = "year", columnDefinition = "year", nullable = false)
    @NotNull(message = "Year cannot be empty")
    @Min(value = 1884, message = "Year must be between 1884-2022")
    @Max(value = 2022, message = "Year must be between 1884-2022")
    private Integer year;

    @Column(name = "mileage", nullable = false)
    @NotNull(message = "Mileage cannot be empty")
    private Integer mileage;

    @Column(name = "body_type", nullable = false)
    @NotEmpty(message = "Body type cannot be empty")
    private String bodyType = ""; // Convertible/Coupe/Hatchback/Minivan/Pickup_truck/Sedan/SUV/Van/Wagon

    @Column(name = "listing_color")
    @NotEmpty(message = "Exterior color cannot be empty")
    private String listingColor = "";

    @Column(name = "exterior_color", nullable = false)
    private String exteriorColor = "";

    @Column(name = "interior_color", nullable = false)
    private String interiorColor = "";

    @Column(name = "engine_type", nullable = false)
    private String engineType = "";

    @Column(name = "engine_displacement", nullable = false)
    private Integer engineDisplacement = 0;

    @Column(name = "pound_foot", nullable = false)
    private Integer poundFoot = 0;

    @Column(name = "torque_rpm", nullable = false)
    private Integer torqueRpm = 0;

    @Column(name = "horsepower", nullable = false)
    private Integer horsePower = 0;

    @Column(name = "power_rpm", nullable = false)
    private Integer powerRpm = 0;

    @Column(name = "transmission", nullable = false)
    private String transmission = "";

    @Column(name = "transmission_display", nullable = false)
    private String transmissionDisplay = "";

    @Column(name = "wheel_system", nullable = false)
    private String wheelSystem = "";

    @Column(name = "wheel_system_display", nullable = false)
    private String wheelSystemDisplay = "";

    @Column(name = "wheelbase", nullable = false)
    @Digits(integer = 3, fraction = 1)
    private BigDecimal wheelbase = BigDecimal.valueOf(0.0);

    @Column(name = "city_fuel_economy", nullable = false)
    private Integer cityFuelEconomy = 0;

    @Column(name = "highway_fuel_economy", nullable = false)
    private Integer highwayFuelEconomy = 0;

    @Column(name = "fuel_tank_volume", nullable = false)
    @Digits(integer = 2, fraction = 1)
    private BigDecimal fuelTankVolume = BigDecimal.valueOf(0.0);

    @Column(name = "fuel_type", nullable = false)
    private String fuelType = ""; // Gasoline/Diesel/Biodiesel/Flex Fuel Vehicle/Electric/Hybrid

    @Column(name = "seller_rating", nullable = false)
    @Digits(integer = 1, fraction = 1, message = "Rating must be between 0-5")
    private BigDecimal sellerRating = BigDecimal.valueOf(0.0);

    @Column(name = "owner_count", nullable = false)
    private Integer ownerCount = 0;

    @Column(name = "trim_name", nullable = false)
    private String trimName = "";

    @Column(name = "back_legroom", nullable = false)
    @Digits(integer = 2, fraction = 1)
    private BigDecimal backLegroom = BigDecimal.valueOf(0.0);

    @Column(name = "front_legroom", nullable = false)
    @Digits(integer = 2, fraction = 1)
    private BigDecimal frontLegroom = BigDecimal.valueOf(0.0);

    @Column(name = "height", nullable = false)
    @Digits(integer = 2, fraction = 1)
    private BigDecimal height = BigDecimal.valueOf(0.0);

    @Column(name = "length", nullable = false)
    @Digits(integer = 2, fraction = 1)
    private BigDecimal length = BigDecimal.valueOf(0.0);

    @Column(name = "width", nullable = false)
    @Digits(integer = 2, fraction = 1)
    private BigDecimal width = BigDecimal.valueOf(0.0);

    @Column(name = "maximum_seating", nullable = false)
    private Integer maximumSeating = 0;

    @Column(name = "is_depreciated", nullable = false)
    private Boolean isDepreciated = false;

    @Column(name = "listed_date", columnDefinition = "date", nullable = false)
    @NotNull
    private LocalDate listedDate;

    @Column(name = "main_picture_url", columnDefinition = "text", nullable = false)
    private String mainPictureUrl = "";

    @Column(name = "is_new", nullable = false)
    private Boolean isNew = false;

    @Column(name = "zip", nullable = false)
    @NotEmpty(message = "Address ZIP cannot be empty")
    private String zip = "";

    @Column(name = "city", nullable = false)
    @NotEmpty(message = "City where the car is listed cannot be empty")
    private String city = "";

    @Column(name = "country", nullable = false)
    private String country = "";

//    @JsonIgnore
//    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
//    private List<CarOption> carOptions;

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

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getMakeName() {
        return makeName;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
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

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
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

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public Integer getEngineDisplacement() {
        return engineDisplacement;
    }

    public void setEngineDisplacement(Integer engineDisplacement) {
        this.engineDisplacement = engineDisplacement;
    }

    public Integer getPoundFoot() {
        return poundFoot;
    }

    public void setPoundFoot(Integer poundFoot) {
        this.poundFoot = poundFoot;
    }

    public Integer getTorqueRpm() {
        return torqueRpm;
    }

    public void setTorqueRpm(Integer torqueRpm) {
        this.torqueRpm = torqueRpm;
    }

    public Integer getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(Integer horsePower) {
        this.horsePower = horsePower;
    }

    public Integer getPowerRpm() {
        return powerRpm;
    }

    public void setPowerRpm(Integer powerRpm) {
        this.powerRpm = powerRpm;
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

    public BigDecimal getWheelbase() {
        return wheelbase;
    }

    public void setWheelbase(BigDecimal wheelbase) {
        this.wheelbase = wheelbase;
    }

    public Integer getCityFuelEconomy() {
        return cityFuelEconomy;
    }

    public void setCityFuelEconomy(Integer cityFuelEconomy) {
        this.cityFuelEconomy = cityFuelEconomy;
    }

    public Integer getHighwayFuelEconomy() {
        return highwayFuelEconomy;
    }

    public void setHighwayFuelEconomy(Integer highwayFuelEconomy) {
        this.highwayFuelEconomy = highwayFuelEconomy;
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

    public BigDecimal getSellerRating() {
        return sellerRating;
    }

    public void setSellerRating(BigDecimal sellerRating) {
        this.sellerRating = sellerRating;
    }

    public Integer getOwnerCount() {
        return ownerCount;
    }

    public void setOwnerCount(Integer ownerCount) {
        this.ownerCount = ownerCount;
    }

    public String getTrimName() {
        return trimName;
    }

    public void setTrimName(String trimName) {
        this.trimName = trimName;
    }

    public BigDecimal getBackLegroom() {
        return backLegroom;
    }

    public void setBackLegroom(BigDecimal backLegroom) {
        this.backLegroom = backLegroom;
    }

    public BigDecimal getFrontLegroom() {
        return frontLegroom;
    }

    public void setFrontLegroom(BigDecimal frontLegroom) {
        this.frontLegroom = frontLegroom;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public Integer getMaximumSeating() {
        return maximumSeating;
    }

    public void setMaximumSeating(Integer maximumSeating) {
        this.maximumSeating = maximumSeating;
    }

    public Boolean getDepreciated() {
        return isDepreciated;
    }

    public void setDepreciated(Boolean depreciated) {
        isDepreciated = depreciated;
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

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean isNew) {
        this.isNew = isNew;
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

    public String getListingColor() {
        return listingColor;
    }

    public void setListingColor(String listingColor) {
        this.listingColor = listingColor;
    }
}
