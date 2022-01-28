package com.uw.gearmax.gearmaxapi.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity(name = "car_option")
public class CarOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "option_id", nullable = false)
    private Long optionId;

    @Column(name = "car_id", nullable = false)
    private Long carId;

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

//    @ManyToOne
//    @JoinColumn(name = "option_id", nullable = false)
//    private Option option;
//
//    @ManyToOne
//    @JoinColumn(name = "car_id", nullable = false)
//    private Car car;

//    public Option getOption() {
//        return option;
//    }
//
//    public void setOption(Option option) {
//        this.option = option;
//    }
//
//    public Car getCar() {
//        return car;
//    }
//
//    public void setCar(Car car) {
//        this.car = car;
//    }
}
