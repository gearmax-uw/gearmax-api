package com.uw.gearmax.gearmaxapi.controller;

import com.uw.gearmax.gearmaxapi.controller.viewobject.CarVO;
import com.uw.gearmax.gearmaxapi.controller.viewobject.DepreciatedCarVO;
import com.uw.gearmax.gearmaxapi.controller.viewobject.DepreciatedPickupTruckVO;
import com.uw.gearmax.gearmaxapi.controller.viewobject.PickupTruckVO;
import com.uw.gearmax.gearmaxapi.domain.Car;
import com.uw.gearmax.gearmaxapi.domain.DepreciatedCar;
import com.uw.gearmax.gearmaxapi.domain.PickupTruck;
import com.uw.gearmax.gearmaxapi.domain.Vehicle;
import com.uw.gearmax.gearmaxapi.domain.es.EsCar;
import com.uw.gearmax.gearmaxapi.error.BusinessException;
import com.uw.gearmax.gearmaxapi.error.EmBusinessError;
import com.uw.gearmax.gearmaxapi.response.CommonReturnType;
import com.uw.gearmax.gearmaxapi.service.CarService;
import com.uw.gearmax.gearmaxapi.service.DepreciatedCarService;
import com.uw.gearmax.gearmaxapi.service.EsCarService;
import com.uw.gearmax.gearmaxapi.service.PickupTruckService;
import com.uw.gearmax.gearmaxapi.util.FieldVal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/car")
public class CarController {

    private static final String CAR_PREFIX_IN_REDIS = "car_";
    @Autowired
    private CarService carService;
    @Autowired
    private EsCarService esCarService;
    @Autowired
    private PickupTruckService pickupTruckService;
    @Autowired
    private DepreciatedCarService depreciatedCarService;
    @Autowired
    private RedisTemplate redisTemplate;

    // car/add?vin=abcd&&...&&country=USA&&bedHeight=1
    // car/add?vin=abcd&&...&&country=USA
    // car/add?vin=abcd&&...&&country=USA&&bedHeight=1&&isFrameDamaged=true
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
                                   @RequestParam(name = "isFrameDamaged", required = false, defaultValue = "false") boolean isFrameDamaged,
                                   @RequestParam(name = "hasAccidents", required = false, defaultValue = "false") boolean hasAccidents,
                                   @RequestParam(name = "isSalvaged", required = false, defaultValue = "false") boolean isSalvaged,
                                   @RequestParam(name = "isCab", required = false, defaultValue = "false") boolean isCab,
                                   @RequestParam(name = "isTheftTitle", required = false, defaultValue = "false") boolean isTheftTitle) throws BusinessException {
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

        // as user inputs the bed length, the car is a pickup truck (only pickup truck has the bed attributes)
        boolean isPickupTruck = bedLength != null || StringUtils.isNotBlank(bed) || StringUtils.isNotBlank(cabin);
        boolean isDepreciated = isFrameDamaged || hasAccidents || isSalvaged || isCab || isTheftTitle;

        // is pickup truck or depreciated
        if (isPickupTruck || isDepreciated) {
            PickupTruck returnedTruck = null;
            DepreciatedCar returnedDepreciatedCar = null;
            if (isPickupTruck) {
                PickupTruck truck = new PickupTruck();
                truck.setId(returnedCar.getId());
                truck.setBedLength(bedLength);
                truck.setBed(bed);
                truck.setCabin(cabin);
                // save the pickup truck to repo
                returnedTruck = pickupTruckService.savePickupTruck(truck);
            }
            if (isDepreciated) {
                DepreciatedCar depreciatedCar = new DepreciatedCar();
                depreciatedCar.setId(returnedCar.getId());
                depreciatedCar.setFrameDamaged(isFrameDamaged);
                depreciatedCar.setHasAccidents(hasAccidents);
                depreciatedCar.setSalvaged(isSalvaged);
                depreciatedCar.setCab(isCab);
                depreciatedCar.setTheftTitle(isTheftTitle);
                // save the depreciated car to repo
                returnedDepreciatedCar = depreciatedCarService.saveDepreciatedCar(depreciatedCar);
            }
            // the car is a depreciated pickup truck
            if (isPickupTruck && isDepreciated) {
                DepreciatedPickupTruckVO depreciatedPickupTruckVO = convertDepreciatedPickupTruckVOFromEntity(car, returnedDepreciatedCar, returnedTruck);
                return CommonReturnType.create(depreciatedPickupTruckVO);
            }
            if (isPickupTruck) {
                PickupTruckVO pickupTruckVO = convertPickupTruckVOFromEntity(car, returnedTruck);
                return CommonReturnType.create(pickupTruckVO);
            }
            // here the car must be depreciated
            DepreciatedCarVO depreciatedCarVO = convertDepreciatedCarVOFromEntity(car, returnedDepreciatedCar);
            return CommonReturnType.create(depreciatedCarVO);
        }

        // here the car must be a normal car, so wrap Car to CarVO
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
        if (StringUtils.equals(returnedCar.getBodyType(), FieldVal.PICKUP_TRUCK.val())) {
            pickupTruckService.removePickupTruck(id);
        }
        return CommonReturnType.create(null);
    }

    // car/1 => get the detail of car with id 1
    @GetMapping("/{id}")
    @ResponseBody
    public CommonReturnType getCar(@PathVariable(value = "id") Long id) throws BusinessException {
        // try to get the car object from redis
        Car car = (Car) redisTemplate.opsForValue().get(CAR_PREFIX_IN_REDIS + id);
        if (car == null) { // if the car is not found from redis
            // get car from database
            Optional<Car> optionalCar = carService.getCarById(id);
            if (optionalCar.isPresent()) {
                car = optionalCar.get();
                // save car to redis
                redisTemplate.opsForValue().set(CAR_PREFIX_IN_REDIS + id, car);
                // the key/value will be expired after 10 minutes
                redisTemplate.expire(CAR_PREFIX_IN_REDIS + id, 10, TimeUnit.MINUTES);
            } else {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                        "Car does not exist");
            }
        }

        // check if the car is both depreciated and a pickup truck
        if (Boolean.TRUE.equals(car.getDepreciated())
                && StringUtils.equals(car.getBodyType(), FieldVal.PICKUP_TRUCK.val())) {
            DepreciatedCar depreciatedCar = depreciatedCarService.getDepreciatedCarById(car.getId()).get();
            PickupTruck truck = pickupTruckService.getPickupTruckById(car.getId()).get();
            DepreciatedPickupTruckVO depreciatedPickupTruckVO = convertDepreciatedPickupTruckVOFromEntity(car, depreciatedCar, truck);
            return CommonReturnType.create(depreciatedPickupTruckVO);
        }

        // check if the car is determined as depreciated
        if (Boolean.TRUE.equals(car.getDepreciated())) {
            // car is determined as depreciated
            DepreciatedCar depreciatedCar = depreciatedCarService.getDepreciatedCarById(car.getId()).get();
            DepreciatedCarVO depreciatedCarVO = convertDepreciatedCarVOFromEntity(car, depreciatedCar);
            return CommonReturnType.create(depreciatedCarVO);
        }

        // check if the car is a pickup truck
        if (StringUtils.equals(car.getBodyType(), FieldVal.PICKUP_TRUCK.val())) {
            PickupTruck truck = pickupTruckService.getPickupTruckById(car.getId()).get();
            PickupTruckVO pickupTruckVO = convertPickupTruckVOFromEntity(car, truck);
            return CommonReturnType.create(pickupTruckVO);
        }

        // the car must be a normal car
        CarVO carVO = convertCarVOFromEntity(car);
        return CommonReturnType.create(carVO);
    }

    /**
     * Since too many @RequestParams, so we use one map which contains all the requested params
     */
    @GetMapping("/list")
    @ResponseBody
    public CommonReturnType listCars(@RequestParam Map<String, String> queryMap) {
        List<Car> cars = carService.listCarsWithDynamicQuery(queryMap);
        List<CarVO> carVOs = cars.stream().map(car -> {
            // check if the car is both depreciated and a pickup truck
            if (Boolean.TRUE.equals(car.getDepreciated())
                    && StringUtils.equals(car.getBodyType(), FieldVal.PICKUP_TRUCK.val())) {
                DepreciatedCar depreciatedCar = depreciatedCarService.getDepreciatedCarById(car.getId()).get();
                PickupTruck truck = pickupTruckService.getPickupTruckById(car.getId()).get();
                return convertDepreciatedPickupTruckVOFromEntity(car, depreciatedCar, truck);
            }
            // check if the car is determined as depreciated
            if (Boolean.TRUE.equals(car.getDepreciated())) {
                // car is determined as depreciated
                DepreciatedCar depreciatedCar = depreciatedCarService.getDepreciatedCarById(car.getId()).get();
                return convertDepreciatedCarVOFromEntity(car, depreciatedCar);
            }

            // check if the car is a pickup truck
            if (StringUtils.equals(car.getBodyType(), FieldVal.PICKUP_TRUCK.val())) {
                PickupTruck truck = pickupTruckService.getPickupTruckById(car.getId()).get();
                return convertPickupTruckVOFromEntity(car, truck);
            }
            return this.convertCarVOFromEntity(car);
        }).collect(Collectors.toList());
        return CommonReturnType.create(carVOs);
    }

    @GetMapping("/eslist")
    @ResponseBody
    public CommonReturnType eslistCars(@RequestParam Map<String, String> queryMap) {
        List<EsCar> cars = esCarService.listCarsWithDynamicQuery(queryMap);
        List<CarVO> carVOs = cars.stream().map(car -> {
            // check if the car is both depreciated and a pickup truck
            if (Boolean.TRUE.equals(car.getDepreciated())
                    && StringUtils.equals(car.getBodyType(), FieldVal.PICKUP_TRUCK.val())) {
                DepreciatedCar depreciatedCar = depreciatedCarService.getDepreciatedCarById(car.getId()).get();
                PickupTruck truck = pickupTruckService.getPickupTruckById(car.getId()).get();
                return convertDepreciatedPickupTruckVOFromEntity(car, depreciatedCar, truck);
            }
            // check if the car is determined as depreciated
            if (Boolean.TRUE.equals(car.getDepreciated())) {
                // car is determined as depreciated
                DepreciatedCar depreciatedCar = depreciatedCarService.getDepreciatedCarById(car.getId()).get();
                return convertDepreciatedCarVOFromEntity(car, depreciatedCar);
            }

            // check if the car is a pickup truck
            if (StringUtils.equals(car.getBodyType(), FieldVal.PICKUP_TRUCK.val())) {
                PickupTruck truck = pickupTruckService.getPickupTruckById(car.getId()).get();
                return convertPickupTruckVOFromEntity(car, truck);
            }
            return this.convertCarVOFromEntity(car);
        }).collect(Collectors.toList());
        return CommonReturnType.create(carVOs);
    }

    private CarVO convertCarVOFromEntity(Vehicle car) {
        CarVO carVO = new CarVO();
        // copy properties of car to carVO
        // make sure Car and CarVO have same attributes
        BeanUtils.copyProperties(car, carVO);
        return carVO;
    }

    private DepreciatedCarVO convertDepreciatedCarVOFromEntity(Vehicle car, DepreciatedCar depreciatedCar) {
        DepreciatedCarVO depreciatedCarVO = new DepreciatedCarVO();
        BeanUtils.copyProperties(car, depreciatedCarVO);
        BeanUtils.copyProperties(depreciatedCar, depreciatedCarVO);
        return depreciatedCarVO;
    }

    private DepreciatedPickupTruckVO convertDepreciatedPickupTruckVOFromEntity(Vehicle car, DepreciatedCar depreciatedCar, PickupTruck truck) {
        DepreciatedPickupTruckVO depreciatedPickupTruckVO = new DepreciatedPickupTruckVO();
        BeanUtils.copyProperties(car, depreciatedPickupTruckVO);
        BeanUtils.copyProperties(depreciatedCar, depreciatedPickupTruckVO);
        BeanUtils.copyProperties(truck, depreciatedPickupTruckVO);
        return depreciatedPickupTruckVO;
    }

    private PickupTruckVO convertPickupTruckVOFromEntity(Vehicle car, PickupTruck truck) {
        PickupTruckVO pickupTruckVO = new PickupTruckVO();
        BeanUtils.copyProperties(car, pickupTruckVO);
        BeanUtils.copyProperties(truck, pickupTruckVO);
        return pickupTruckVO;
    }
}
