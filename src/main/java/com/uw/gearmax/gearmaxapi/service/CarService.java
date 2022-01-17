package com.uw.gearmax.gearmaxapi.service;

import com.uw.gearmax.gearmaxapi.domain.Car;
import com.uw.gearmax.gearmaxapi.error.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface CarService {

    Car saveCar(Car car) throws BusinessException;

    Optional<Car> getCarById(Long id);

    Car removeCar(Long id) throws BusinessException;

    Page<Car> listCarsByBodyType(String bodyType, Pageable pageable);

    Page<Car> listCarsWithSpecification(Specification<Car> spec, Pageable pageable);

}
