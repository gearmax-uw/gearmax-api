package com.uw.gearmax.gearmaxapi.controller;

import com.uw.gearmax.gearmaxapi.controller.viewobject.CarVO;
import com.uw.gearmax.gearmaxapi.controller.viewobject.DepreciatedCarVO;
import com.uw.gearmax.gearmaxapi.controller.viewobject.PickupTruckVO;
import com.uw.gearmax.gearmaxapi.domain.Car;
import com.uw.gearmax.gearmaxapi.domain.DepreciatedCar;
import com.uw.gearmax.gearmaxapi.domain.PickupTruck;
import com.uw.gearmax.gearmaxapi.error.BusinessException;
import com.uw.gearmax.gearmaxapi.error.EmBusinessError;
import com.uw.gearmax.gearmaxapi.query.CarSpecificationBuilder;
import com.uw.gearmax.gearmaxapi.query.SearchOperation;
import com.uw.gearmax.gearmaxapi.response.CommonReturnType;
import com.uw.gearmax.gearmaxapi.service.CarService;
import com.uw.gearmax.gearmaxapi.service.DepreciatedCarService;
import com.uw.gearmax.gearmaxapi.service.PickupTruckService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/car")
public class CarController {

    private static final String PRICE_FIELD_IN_CAR_SQL = "price";
    private static final String PRICE_PARAM_IN_URL = "price";
    private static final String YEAR_PARAM_IN_URL = "year";
    private static final String PICKUP_TRUCK_IN_CAR_SQL = "pickup-truck";

    @Autowired
    private CarService carService;
    @Autowired
    private PickupTruckService pickupTruckService;
    @Autowired
    private DepreciatedCarService depreciatedCarService;

    // car/add?vin=abcd&&...&&country=USA&&bedHeight=1
    // car/add?vin=abcd&&...&&country=USA
    // car/add?vin=abcd&&...&&country=USA&&bedHeight=1&&isFrameDamaged=true
    @Autowired
    private RedisTemplate redisTemplate;

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
                                   @RequestParam(name = "country") String country,
                                   @RequestParam(name = "bedLength", required = false) BigDecimal bedLength,
                                   @RequestParam(name = "bed", required = false) String bed,
                                   @RequestParam(name = "cabin", required = false) String cabin,
                                   @RequestParam(name = "isFrameDamaged", required = false, defaultValue = "false") boolean isFrameDamaged) throws BusinessException {
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

        // determine if the car to be saved is a pickup truck
        if (StringUtils.equals(returnedCar.getBodyType(), PICKUP_TRUCK_IN_CAR_SQL)) {
            PickupTruck truck = new PickupTruck(); // create a pickup truck object
            truck.setId(returnedCar.getId());
            truck.setBedLength(bedLength);
            truck.setBed(bed);
            truck.setCabin(cabin);

            // store pickup truck object to repo
            PickupTruck returnedTruck = pickupTruckService.savePickupTruck(truck);
            PickupTruckVO pickupTruckVO = convertPickupTruckVOFromEntity(returnedCar, returnedTruck);
            return CommonReturnType.create(pickupTruckVO);
        }

        // determine if the car to be saved is a depreciated car
        if (Boolean.TRUE.equals(returnedCar.getDepreciated())) {
            DepreciatedCar depreciatedCar = new DepreciatedCar();
            depreciatedCar.setId(returnedCar.getId());

            // todo: get user inputs from parameters
            depreciatedCar.setFrameDamaged(isFrameDamaged);
            depreciatedCar.setHasAccidents(true);
            depreciatedCar.setSalvaged(true);
            depreciatedCar.setCab(true);
            depreciatedCar.setTheftTitle(true);

            DepreciatedCar returnedDepreciatedCar = depreciatedCarService.saveDepreciatedCar(depreciatedCar);
            DepreciatedCarVO depreciatedCarVO = convertDepreciatedCarVOFromEntity(car, returnedDepreciatedCar);
            return CommonReturnType.create(depreciatedCarVO);
        }

        // wrap Car to CarVO
        CarVO carVO = convertCarVOFromEntity(returnedCar);

        return CommonReturnType.create(carVO);
    }

    // car/delete/1 => delete the car with id 1
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public CommonReturnType removeCar(@PathVariable(value = "id") Long id) throws BusinessException {
        Car returnedCar = carService.removeCar(id);
        // determine if the car to be removed is depreciated
        if (Boolean.TRUE.equals(returnedCar.getDepreciated())) {
            // it is depreciated, so remove it from depreciatedRepo
            depreciatedCarService.removeDepreciatedCar(id);
        }
        // it is depreciated, so remove it from pickupRepo
        if (StringUtils.equals(returnedCar.getBodyType(), PICKUP_TRUCK_IN_CAR_SQL)) {
            pickupTruckService.removePickupTruck(id);
        }
        return CommonReturnType.create(null);
    }

    // car/1 => get the detail of car with id 1
    @GetMapping("/{id}")
    @ResponseBody
    public CommonReturnType getCar(@PathVariable(value = "id") Long id) throws BusinessException {
        // try to get the car object from redis
        Car car = (Car) redisTemplate.opsForValue().get("car_" + id);
        if (car == null) { // if the car is not found from redis
            // get car from database
            Optional<Car> optionalCar = carService.getCarById(id);
            if (optionalCar.isPresent()) {
                car = optionalCar.get();
                // save car to redis
                redisTemplate.opsForValue().set("car_" + id, car);
                // the key/value will be expired after 10 minutes
                redisTemplate.expire("car_" + id, 10, TimeUnit.MINUTES);
            } else {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                        "Car does not exist");
            }
        }

        if (Boolean.TRUE.equals(car.getDepreciated())
                && StringUtils.equals(car.getBodyType(), PICKUP_TRUCK_IN_CAR_SQL)) {
            // todo: ...
        }

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
     * - listing_color
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
                                            @RequestParam(name = "listingColor", required = false) String listingColor,
                                            @RequestParam(name = "year", required = false) String yearRange,
                                            @RequestParam(name = "mileage", required = false, defaultValue = "-1") int mileage,
                                            @RequestParam(name = "maximumSeating", required = false, defaultValue = "-1") int maximumSeating,
                                            @RequestParam(name = "transmissionDisplay", required = false) String transmissionDisplay,
                                            @RequestParam(name = "features", required = false) String options,
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

        Specification<Car> spec = getCarSpec(priceRange, bodyType, city, makeName, modelName, listingColor, yearRange,
                mileage, maximumSeating, transmissionDisplay);

        Page<Car> page = carService.listCarsWithSpecificationAndPagination(spec, pageable);

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

    private PickupTruckVO convertPickupTruckVOFromEntity(Car car, PickupTruck truck) {
        PickupTruckVO pickupTruckVO = new PickupTruckVO();
        BeanUtils.copyProperties(car, pickupTruckVO);
        BeanUtils.copyProperties(truck, pickupTruckVO);
        return pickupTruckVO;
    }

    private Specification<Car> getCarSpec(String priceRange, String bodyType, String city, String makeName, String modelName,
                                          String listingColor, String yearRange, int mileage, int maximumSeating, String transmissionDisplay) {
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
            builder.with("modelName", SearchOperation.EQUALITY, modelName);
        }
        if (StringUtils.isNotEmpty(listingColor)) {
            builder.with("listingColor", SearchOperation.EQUALITY, listingColor);
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
