package com.uw.gearmax.gearmaxapi.controller;

import com.uw.gearmax.gearmaxapi.controller.viewobject.CarVO;
import com.uw.gearmax.gearmaxapi.domain.Car;
import com.uw.gearmax.gearmaxapi.error.BusinessException;
import com.uw.gearmax.gearmaxapi.response.CommonReturnType;
import com.uw.gearmax.gearmaxapi.service.CarService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
        CarVO carVO = convertVOFromEntity(returnedCar);

        return CommonReturnType.create(carVO);
    }

    @DeleteMapping(value = "/delete/{id}")
    @ResponseBody
    public CommonReturnType removeCar(@PathVariable(value = "id") Long id) throws BusinessException {
        carService.removeCar(id);
        return CommonReturnType.create(null);
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Hello world";
    }

    private CarVO convertVOFromEntity(Car car) {
        CarVO carVO = new CarVO();
        BeanUtils.copyProperties(car, carVO);
        return carVO;
    }
}
