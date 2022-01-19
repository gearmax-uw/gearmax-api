package com.uw.gearmax.gearmaxapi.validator;

import com.uw.gearmax.gearmaxapi.domain.Car;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CarValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeClass
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void addCarShouldHaveNoViolations() {
        Car car = createNormalCar();
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(car);
        assertTrue(constraintViolationSet.isEmpty());
    }

    @Test
    public void addCarShouldHaveViolations() {
        Car car = createNormalCar();
        // make car attribute have violations
        car.setVin(null);
        Set<ConstraintViolation<Car>> constraintViolationSet = validator.validate(car);
        ConstraintViolation<Car> violation = constraintViolationSet.iterator().next();
        assertEquals("VIN cannot be empty", violation.getMessage());

        car.setVin("xxxxx"); // make it follow the constraint again
        car.setPrice(null);
        assertEquals("Price cannot be empty", validator.validate(car).iterator().next().getMessage());

        car.setPrice(1000000000);
        assertEquals("Price must be between 0-999999999", validator.validate(car).iterator().next().getMessage());

        car.setPrice(999999);
        car.setYear(null);
        assertEquals("Year cannot be empty", validator.validate(car).iterator().next().getMessage());

        car.setYear(2025);
        assertEquals("Year must be between 1884-2022", validator.validate(car).iterator().next().getMessage());

        car.setYear(1883);
        assertEquals("Year must be between 1884-2022", validator.validate(car).iterator().next().getMessage());

        car.setYear(2010);
        car.setMileage(null);
        assertEquals("Mileage cannot be empty", validator.validate(car).iterator().next().getMessage());

        car.setMileage(10);
        car.setBodyType(null);
        assertEquals("Body type cannot be empty", validator.validate(car).iterator().next().getMessage());

        car.setBodyType("pickup_truck");
        car.setListedDate(null);
        assertEquals(1, validator.validate(car).size());

        car.setListedDate(LocalDate.now());
        car.setZip(null);
        assertEquals("Address ZIP cannot be empty", validator.validate(car).iterator().next().getMessage());

        car.setZip("123456");
        car.setCity(null);
        assertEquals("City where the car is listed cannot be empty", validator.validate(car).iterator().next().getMessage());
    }

    private Car createNormalCar() {
        Car car = new Car();
        car.setVin("xxxxx");
        car.setSellerId(1);
        car.setPrice(1000);
        car.setYear(2002);
        car.setMileage(10);
        car.setBodyType("pickup_truck");
        car.setListedDate(LocalDate.now());
        car.setZip("123456");
        car.setCity("Huston");
        return car;
    }
}
