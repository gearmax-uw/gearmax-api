package com.uw.gearmax.gearmaxapi.service.impl;

import com.uw.gearmax.gearmaxapi.domain.Car;
import com.uw.gearmax.gearmaxapi.error.BusinessException;
import com.uw.gearmax.gearmaxapi.error.EmBusinessError;
import com.uw.gearmax.gearmaxapi.repository.CarRepository;
import com.uw.gearmax.gearmaxapi.service.CarService;
import com.uw.gearmax.gearmaxapi.validator.ValidationResult;
import com.uw.gearmax.gearmaxapi.validator.ValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private CarRepository carRepository;

    /**
     * Validate the car properties by constraints. If no constraint is violated, then save car to repo.
     * For defined constraints,
     * @see com.uw.gearmax.gearmaxapi.domain.Car
     */
    @Override
    @Transactional
    public Car saveCar(Car car) throws BusinessException {
        ValidationResult result = validator.validate(car);
        if (result.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }

        Car returnedCar = carRepository.save(car);

        return getCarById(returnedCar.getId()).get();
    }

    @Override
    public List<Car> listCars() {
        return carRepository.findAll();
    }

    @Override
    @Transactional
    public void removeCar(Long id) throws BusinessException {
        Optional<Car> optionalCar = getCarById(id);
        // if remove a car does not exist, throw an exception
        if (!optionalCar.isPresent()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                    "Car to be removed does not exist");
        }
        carRepository.deleteById(id);
    }

    @Override
    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }
}
