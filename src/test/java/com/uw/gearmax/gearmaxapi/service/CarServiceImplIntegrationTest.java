package com.uw.gearmax.gearmaxapi.service;

import com.uw.gearmax.gearmaxapi.domain.Car;
import com.uw.gearmax.gearmaxapi.error.BusinessException;
import com.uw.gearmax.gearmaxapi.repository.CarRepository;
import com.uw.gearmax.gearmaxapi.service.impl.CarServiceImpl;
import com.uw.gearmax.gearmaxapi.util.UrlParameter;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
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
    void listCarWithGivenId() throws BusinessException {
        Car expectedCar = car1;

        when(carRepository.findById(anyLong())).thenReturn(Optional.of(car1));

        Car returnedCar = carService.getCarById(1L);

        assertThat(returnedCar).isEqualTo(expectedCar);
    }

    @Test
    void listCarsWithDynamicQueryShouldReturnSatisfiedCars() {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put(UrlParameter.PAGE_SIZE.val(), "1");
        queryMap.put(UrlParameter.PRICE.val(), "10000-100000");
        queryMap.put(UrlParameter.BODY_TYPE.val(), "coupe");
        queryMap.put(UrlParameter.MAKE_NAME.val(), "land-rover");
        queryMap.put(UrlParameter.LISTING_COLOR.val(), "black");
        queryMap.put(UrlParameter.YEAR.val(), "2010-2020");
        queryMap.put(UrlParameter.MILEAGE.val(), "1500");
        queryMap.put(UrlParameter.MAXIMUM_SEATING.val(), "10");

        Car expectedCar = new Car();
        expectedCar.setPrice(10000);
        expectedCar.setBodyType("Coupe");
        expectedCar.setMakeName("Land Rover");
        expectedCar.setListingColor("Black");
        expectedCar.setYear(2012);
        expectedCar.setMileage(1000);
        expectedCar.setMaximumSeating(5);

        Page<Car> returnedCar = new PageImpl<>(Arrays.asList(expectedCar));

        when(carRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(returnedCar);

        List<Car> returnedCars = carService.listCarsWithDynamicQuery(queryMap);

        assertThat(returnedCars.get(0)).isEqualTo(expectedCar);
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
        when(carService.getOptionalCarById(anyLong())).thenReturn(Optional.of(car1));

        carService.removeCar(car1.getId());

        verify(carRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void givenNotExistCarToRemoveThenShouldThrowException() {
        Optional<Car> notExistCar = Optional.empty();
        when(carService.getOptionalCarById(anyLong())).thenReturn(notExistCar);
        assertThrows(BusinessException.class, () -> carService.removeCar(anyLong()));
    }

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
