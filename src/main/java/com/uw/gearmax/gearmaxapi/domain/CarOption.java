package com.uw.gearmax.gearmaxapi.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "car_option")
@IdClass(CompositeKey.class)
public class CarOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "option_id", nullable = false)
    private Long optionId;

    @Id
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
}

@Embeddable
class CompositeKey implements Serializable {
    private Long optionId;
    private Long carId;
}
