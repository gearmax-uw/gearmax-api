package com.uw.gearmax.gearmaxapi.controller;

import com.uw.gearmax.gearmaxapi.controller.viewobject.CarVO;
import com.uw.gearmax.gearmaxapi.controller.viewobject.DepreciatedCarVO;
import com.uw.gearmax.gearmaxapi.domain.Car;
import com.uw.gearmax.gearmaxapi.domain.DepreciatedCar;
import com.uw.gearmax.gearmaxapi.error.BusinessException;
import com.uw.gearmax.gearmaxapi.error.EmBusinessError;
import com.uw.gearmax.gearmaxapi.query.CarSpecificationBuilder;
import com.uw.gearmax.gearmaxapi.query.SearchOperation;
import com.uw.gearmax.gearmaxapi.response.CommonReturnType;
import com.uw.gearmax.gearmaxapi.service.CarService;
import com.uw.gearmax.gearmaxapi.service.DepreciatedCarService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/car")
public class CarController {

    private static final String PRICE_FIELD_IN_CAR_SQL = "price";
    private static final String PRICE_PARAM_IN_URL = "price";
    private static final String YEAR_PARAM_IN_URL = "year";

    @Autowired
    private CarService carService;

    @Autowired
    private DepreciatedCarService depreciatedCarService;

    @PostMapping("/add")
    @ResponseBody
    public CommonReturnType addCar(@RequestParam(name = "vin") String vin,
                                   @RequestParam(name = "sellerId") int sellerId,
                                   @RequestParam(name = "price") int price,
                                   @RequestParam(name = "year") int year,
                                   @RequestParam(name = "mileage") int mileage,
                                   @RequestParam(name = "bodyType") String bodyType,
                                   @RequestParam(name = "zip") String zip,
                                   @RequestParam(name = "city") String city,
                                   @RequestParam(name = "country") String country) throws BusinessException {
        Car car = new Car();
        car.setVin(vin);
        car.setSellerId(sellerId);
        car.setPrice(price);
        car.setYear(year);
        car.setMileage(mileage);
        car.setBodyType(bodyType);
        car.setZip(zip);
        car.setCity(city);
        car.setCountry(country);

        car.setListedDate(LocalDate.now());

        Car returnedCar = carService.saveCar(car);

        // TODO: when returnedCar is determined as DepreciatedCar/PickupTruck, you need to create DepreciatedCar/PickupTruck and save them in repo

        // wrap Car to CarVO
        CarVO carVO = convertCarVOFromEntity(returnedCar);

        return CommonReturnType.create(carVO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public CommonReturnType removeCar(@PathVariable(value = "id") Long id) throws BusinessException {
        // TODO: first determine if the id corresponds a DepreciatedCar/PickupTruck. If it is, remove it from repo (CarRepo and DepreciatedCarRepo)
        carService.removeCar(id);
        return CommonReturnType.create(null);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public CommonReturnType getCar(@PathVariable(value = "id") Long id) throws BusinessException {
        Optional<Car> optionalCar = carService.getCarById(id);
        Car car;
        if (optionalCar.isPresent()) {
            car = optionalCar.get();
        } else {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                    "Car does not exist");
        }

        // TODO: check if car is determined as both depreciated and pickup truck; if it is, get it and return vo

        // check if car is determined as depreciated
        if (Boolean.TRUE.equals(car.getDepreciated())) {
            // car is determined as depreciated
            DepreciatedCar depreciatedCar = depreciatedCarService.getDepreciatedCarById(car.getId()).get();
            DepreciatedCarVO depreciatedCarVO = convertDepreciatedCarVOFromEntity(car, depreciatedCar);
            return CommonReturnType.create(depreciatedCarVO);
        }

        // TODO: check if car is determined as a pickup truck; if it is, get it and return vo

        // the car must be a normal car
        CarVO carVO = convertCarVOFromEntity(car);
        return CommonReturnType.create(carVO);
    }

    /**
     * Parameters that can be used to search:
     * - price (range, e.g., 1000-2000)
     * - body_type
     * - city
     * - make_name
     * - model_name
     * - year (range, e.g., 2010-2022)
     * - mileage (less than)
     * - options (not implemented yet)
     * - exterior_color (has problem now)
     * - interior_color (has problem now)
     * - maximum_seating
     * - transmission_display (called 'transmission' to users)
     * ...
     * transmission: A (Automatic), M (Manual), CVT, Dual Clutch (only for note, may not provide to users)
     * <p>
     * This method searches items by given parameters. Pagination and sorting are applied.
     */
    @GetMapping("/list")
    @ResponseBody
    public CommonReturnType listCarsByOrder(@RequestParam(name = "price", required = false) String priceRange,
                                            @RequestParam(name = "bodyType", required = false) String bodyType,
                                            @RequestParam(name = "city", required = false) String city,
                                            @RequestParam(name = "makeName", required = false) String makeName,
                                            @RequestParam(name = "modelName", required = false) String modelName,
                                            @RequestParam(name = "year", required = false) String yearRange,
                                            @RequestParam(name = "mileage", required = false, defaultValue = "-1") int mileage,
                                            @RequestParam(name = "maximumSeating", required = false, defaultValue = "-1") int maximumSeating,
                                            @RequestParam(name = "transmissionDisplay", required = false) String transmissionDisplay,
                                            @RequestParam(name = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
                                            @RequestParam(name = "sort", required = false, defaultValue = "") String sortField,
                                            @RequestParam(name = "sortOrder", required = false, defaultValue = "asc") String sortOrder) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        if (isSortFieldAvailable(sortField)) {
            if (StringUtils.equals(sortOrder, "asc")) {
                pageable = PageRequest.of(pageIndex, pageSize, Sort.by(sortField).ascending());
            } else if (StringUtils.equals(sortOrder, "desc")) {
                pageable = PageRequest.of(pageIndex, pageSize, Sort.by(sortField).descending());
            }
        }

        Specification<Car> spec = getCarSpec(priceRange, bodyType, city, makeName, modelName, yearRange, mileage, maximumSeating, transmissionDisplay);

        // return page 0 with 10 records if pageIndex = 0 and pageSize = 10
        Page<Car> page = carService.listCarsWithSpecification(spec, pageable);

        List<Car> cars = page.getContent();
        List<CarVO> carVOs = cars.stream().map(car -> {
            // check if car is depreciated
            if (Boolean.TRUE.equals(car.getDepreciated())) {
                // if it is, get the depreciated car from repo
                DepreciatedCar depreciatedCar = depreciatedCarService.getDepreciatedCarById(car.getId()).get();
                // copy both car's fields and depreciated car's fields to depreciated car's vo as DepreciatedCar extends Car
                return convertDepreciatedCarVOFromEntity(car, depreciatedCar);
            }
            return this.convertCarVOFromEntity(car);
        }).collect(Collectors.toList());
        return CommonReturnType.create(carVOs);
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Hello world";
    }

    @GetMapping("/test/depreciatedCar/{id}")
    @ResponseBody
    public CommonReturnType testGetDepreciatedCar(@PathVariable Long id) {
        Optional<DepreciatedCar> optionalCar = depreciatedCarService.getDepreciatedCarById(id);
        if (optionalCar.isPresent()) {
            DepreciatedCar depreciatedCar = optionalCar.get();
            return CommonReturnType.create(depreciatedCar);
        }
        return CommonReturnType.create(null);
    }

    private CarVO convertCarVOFromEntity(Car car) {
        CarVO carVO = new CarVO();
        // copy properties of car to carVO
        // make sure Car and CarVO have same attributes
        BeanUtils.copyProperties(car, carVO);
        return carVO;
    }

    private DepreciatedCarVO convertDepreciatedCarVOFromEntity(Car car, DepreciatedCar depreciatedCar) {
        DepreciatedCarVO depreciatedCarVO = new DepreciatedCarVO();
        BeanUtils.copyProperties(car, depreciatedCarVO);
        BeanUtils.copyProperties(depreciatedCar, depreciatedCarVO);
        return depreciatedCarVO;
    }

    private Specification<Car> getCarSpec(String priceRange, String bodyType, String city, String makeName, String modelName,
                                          String yearRange, int mileage, int maximumSeating, String transmissionDisplay) {
        CarSpecificationBuilder builder = new CarSpecificationBuilder();
        if (StringUtils.isNotEmpty(priceRange)) {
            // the given parameter in url will be year = xxxx-yyyy, then the sql condition will be year >= xxxx and year <= yyyy
            int minPrice = Integer.parseInt(priceRange.substring(0, priceRange.indexOf("-")));
            int maxPrice = Integer.parseInt(priceRange.substring(priceRange.indexOf("-") + 1));
            builder.with(PRICE_FIELD_IN_CAR_SQL, SearchOperation.GREATER_THAN, minPrice);
            builder.with(PRICE_FIELD_IN_CAR_SQL, SearchOperation.LESS_THAN, maxPrice);
        }
        if (StringUtils.isNotEmpty(bodyType)) {
            // if bodyType = "SUV", then the sql condition will be bodyType = "SUV"
            builder.with("bodyType", SearchOperation.EQUALITY, bodyType);
        }
        if (StringUtils.isNotEmpty(city)) {
            builder.with("city", SearchOperation.EQUALITY, city);
        }
        if (StringUtils.isNotEmpty(makeName)) {
            builder.with("makeName", SearchOperation.EQUALITY, makeName);
        }
        if (StringUtils.isNotEmpty(modelName)) {
            builder.with("modelName", SearchOperation.EQUALITY, makeName);
        }
        if (StringUtils.isNotEmpty(yearRange)) {
            // the given parameter in url will be year = xxxx-yyyy, then the sql condition will be year >= xxxx and year <= yyyy
            int minYear = Integer.parseInt(yearRange.substring(0, yearRange.indexOf("-")));
            int maxYear = Integer.parseInt(yearRange.substring(yearRange.indexOf("-") + 1));
            builder.with("year", SearchOperation.GREATER_THAN, minYear);
            builder.with("year", SearchOperation.LESS_THAN, maxYear);
        }
        if (mileage >= 0) {
            // if mileage = 10000, then the sql condition will be mileage <= 10000
            builder.with("mileage", SearchOperation.LESS_THAN, mileage);
        }
        if (maximumSeating > 0) {
            builder.with("maximumSeating", SearchOperation.EQUALITY, maximumSeating);
        }
        if (StringUtils.isNotEmpty(transmissionDisplay)) {
            builder.with("transmissionDisplay", SearchOperation.EQUALITY, transmissionDisplay);
        }
        return builder.build();
    }

    /**
     * The results can be sorted by price and year
     */
    private boolean isSortFieldAvailable(String sortField) {
        return StringUtils.equals(sortField, PRICE_PARAM_IN_URL)
                || StringUtils.equals(sortField, YEAR_PARAM_IN_URL);
    }
}
