package com.uw.gearmax.gearmaxapi.service;

import com.uw.gearmax.gearmaxapi.domain.Car;
import com.uw.gearmax.gearmaxapi.error.BusinessException;
import com.uw.gearmax.gearmaxapi.repository.CarRepository;
import com.uw.gearmax.gearmaxapi.service.impl.CarServiceImpl;
import com.uw.gearmax.gearmaxapi.validator.ValidationResult;
import com.uw.gearmax.gearmaxapi.validator.ValidatorImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplIntegrationTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private ValidatorImpl validator;

    @Autowired
    @InjectMocks
    private CarServiceImpl carService;

    private Car car1;
    private Car car2;
    private List<Car> cars;

    @BeforeEach
    public void setUp() {
        cars = new ArrayList<>();
        car1 = createCar(1L, "abc");
        car2 = createCar(2L, "def");
        cars.add(car1);
        cars.add(car2);
    }

    @AfterEach
    public void tearDown() {
        car1 = car2 = null;
        cars = null;
    }

    @Test
    void givenCarToSaveShouldReturnSavedCar() throws Exception {
        ValidationResult fakeValidationResult = new ValidationResult();
        fakeValidationResult.setHasErrors(false);

        when(validator.validate(any(Car.class))).thenReturn(fakeValidationResult);
        when(carRepository.save(any(Car.class))).thenReturn(car1);

        carService.saveCar(car1);

        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void givenIdToRemoveThenShouldRemoveCar() throws Exception {
        when(carService.getCarById(anyLong())).thenReturn(Optional.of(car1));

        carService.removeCar(car1.getId());

        verify(carRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void givenNotExistCarToRemoveThenShouldThrowException() {
        Optional<Car> notExistCar = Optional.empty();
        when(carService.getCarById(anyLong())).thenReturn(notExistCar);

        BusinessException e = assertThrows(BusinessException.class, () -> carService.removeCar(anyLong()));
        assertEquals("Car to be removed does not exist", e.getErrMsg());
    }

//    @Test
//    public void givenCarToSaveShouldReturnSavedCar() throws Exception {
//        Car car = createCar(1L, "abc");
//        ValidationResult fakeValidationResult = new ValidationResult();
//        fakeValidationResult.setHasErrors(false);
//
//        when(validator.validate(any(Car.class))).thenReturn(fakeValidationResult);
//        when(carRepository.save(any(Car.class))).thenReturn(car);
//
//        Car returnedCar = carService.saveCar(car);
//
//        assertEquals(returnedCar.getVin(), car.getVin());
//    }

    private Car createCar(Long id, String vin) {
        Car car = new Car();
        car.setId(id);
        car.setVin(vin);
        car.setSellerId(100);
        car.setPrice(99999);
        car.setYear(2020);
        car.setMileage(10);
        car.setBodyType("SUV");
        car.setZip("000111");
        car.setCity("Seattle");
        car.setCountry("USA");
        car.setListedDate(LocalDate.parse("2020-01-01"));

        return car;
    }
}
