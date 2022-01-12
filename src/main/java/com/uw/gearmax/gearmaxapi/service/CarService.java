package com.uw.gearmax.gearmaxapi.service;

import com.uw.gearmax.gearmaxapi.domain.Car;
import com.uw.gearmax.gearmaxapi.error.BusinessException;

import java.util.List;
import java.util.Optional;

public interface CarService {

    Car saveCar(Car car) throws BusinessException;

    Optional<Car> getCarById(Long id);

    List<Car> listCars();

    void removeCar(Long id) throws BusinessException;
}
