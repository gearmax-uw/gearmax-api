package com.uw.gearmax.gearmaxapi.controller;

import com.uw.gearmax.gearmaxapi.controller.viewobject.CarVO;
import com.uw.gearmax.gearmaxapi.domain.Car;
import com.uw.gearmax.gearmaxapi.error.BusinessException;
import com.uw.gearmax.gearmaxapi.query.CarSpecificationBuilder;
import com.uw.gearmax.gearmaxapi.query.SearchOperation;
import com.uw.gearmax.gearmaxapi.response.CommonReturnType;
import com.uw.gearmax.gearmaxapi.service.CarService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/car")
public class CarController extends BaseController {

    @Autowired
    private CarService carService;

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
        // wrap Car to CarVO
        CarVO carVO = convertVOFromEntity(returnedCar);

        return CommonReturnType.create(carVO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public CommonReturnType removeCar(@PathVariable(value = "id") Long id) throws BusinessException {
        carService.removeCar(id);
        return CommonReturnType.create(null);
    }

    /**
     * Requested parameters:
     * - bodyType
     * - city
     * - make_name
     * - year (range, e.g., 2010-2022)
     * - mileage (less than)
     * - options (not implemented yet)
     * - exterior_color (has problem now)
     * - interior_color (has problem now)
     * - maximum_seating
     * - transmission: A (Automatic), M (Manual), CVT, Dual Clutch (may not provide to users)
     * - transmission_display (called 'transmission' to users)
     * ...
     */
    @GetMapping("/list")
    @ResponseBody
    public CommonReturnType listCarsByOrder(@RequestParam(name = "bodyType", required = false) String bodyType,
                                            @RequestParam(name = "city", required = false) String city,
                                            @RequestParam(name = "makeName", required = false) String makeName,
                                            @RequestParam(name = "year", required = false) String yearRange,
                                            @RequestParam(name = "mileage", required = false, defaultValue = "-1") int mileage,
                                            @RequestParam(name = "maximumSeating", required = false, defaultValue = "-1") int maximumSeating,
                                            @RequestParam(name = "transmissionDisplay", required = false) String transmissionDisplay,
                                            @RequestParam(name = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        // Page<Car> page =  carService.listCarsByBodyType(bodyType, pageable);

        Specification<Car> spec = getCarSpec(bodyType, city, makeName, yearRange, mileage, maximumSeating, transmissionDisplay);

        // return page 0 with 10 records if pageIndex = 0 and pageSize = 10
        Page<Car> page = carService.listCarsWithSpecification(spec, pageable);

        List<Car> cars = page.getContent();
        List<CarVO> carVOs = cars.stream().map(car -> {
            CarVO carVO = this.convertVOFromEntity(car);
            return carVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(carVOs);
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Hello world";
    }

    private CarVO convertVOFromEntity(Car car) {
        CarVO carVO = new CarVO();
        // copy properties of car to carVO
        // make sure Car and CarVO have same attributes
        BeanUtils.copyProperties(car, carVO);
        return carVO;
    }

    private Specification<Car> getCarSpec(String bodyType, String city, String makeName, String yearRange,
                                          int mileage, int maximumSeating, String transmissionDisplay) {
        CarSpecificationBuilder builder = new CarSpecificationBuilder();
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
        if (StringUtils.isNotEmpty(yearRange)) {
            // the given parameter in url will be year = xxxx-yyyy, then the sql condition will be year >= xxxx and year <= yyyy
            int minYear = Integer.valueOf(yearRange.substring(0, yearRange.indexOf("-")));
            int maxYear = Integer.valueOf(yearRange.substring(yearRange.indexOf("-") + 1));
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
        Specification<Car> spec = builder.build();
        return spec;
    }
}
