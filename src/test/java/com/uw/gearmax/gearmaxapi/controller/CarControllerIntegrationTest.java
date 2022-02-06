package com.uw.gearmax.gearmaxapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uw.gearmax.gearmaxapi.controller.viewobject.CarVO;
import com.uw.gearmax.gearmaxapi.controller.viewobject.DepreciatedCarVO;
import com.uw.gearmax.gearmaxapi.controller.viewobject.DepreciatedPickupTruckVO;
import com.uw.gearmax.gearmaxapi.controller.viewobject.PickupTruckVO;
import com.uw.gearmax.gearmaxapi.domain.Car;
import com.uw.gearmax.gearmaxapi.domain.DepreciatedCar;
import com.uw.gearmax.gearmaxapi.domain.PickupTruck;
import com.uw.gearmax.gearmaxapi.domain.es.EsCar;
import com.uw.gearmax.gearmaxapi.response.CommonReturnType;
import com.uw.gearmax.gearmaxapi.service.CarService;
import com.uw.gearmax.gearmaxapi.service.DepreciatedCarService;
import com.uw.gearmax.gearmaxapi.service.EsCarService;
import com.uw.gearmax.gearmaxapi.service.PickupTruckService;
import com.uw.gearmax.gearmaxapi.util.FieldVal;
import com.uw.gearmax.gearmaxapi.util.UrlParameter;
import com.uw.gearmax.gearmaxapi.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
class CarControllerIntegrationTest {

    ValidatorImpl validator;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private DepreciatedCarService depreciatedCarService;
    @MockBean
    private PickupTruckService pickupTruckService;
    @MockBean
    private CarService carService;
    @MockBean
    private EsCarService esCarService;

//    @Test
//    void addCarShouldReturnCarVO() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .post("/car/add")
//                .accept(MediaType.APPLICATION_JSON)
//                .param("vin", "abcdefg")
//                .param("sellerId", "2")
//                .param("price", "99999")
//                .param("year", "2020")
//                .param("mileage", "10")
//                .param("bodyType", "SUV")
//                .param("zip", "000111")
//                .param("city", "Seattle")
//                .param("country", "USA")
//                .param("listedDate", "2020-01-01");
//
//        Car car = createCar();
//        when(carService.saveCar(any(Car.class))).thenReturn(car);
//
//        MvcResult mvcResult = mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andReturn();
//
//        CarVO carVO = new CarVO();
//        BeanUtils.copyProperties(car, carVO);
//        CommonReturnType expectedResponseBody = CommonReturnType.create(carVO);
//        String actualResponseBody = mvcResult.getResponse().getContentAsString();
//
//        assertEquals(StringUtils.deleteWhitespace(objectMapper.writeValueAsString(expectedResponseBody)),
//                StringUtils.deleteWhitespace(actualResponseBody));
//    }
//
//    @Test
//    void removeCarShouldReturnSuccessCode() throws Exception {
//
//        RequestBuilder request = MockMvcRequestBuilders
//                .delete("/car/delete/1")
//                .contentType(MediaType.APPLICATION_JSON);
//
//        when(carService.getCarById(1L)).thenReturn(Optional.of(createCar()));
//
//        mockMvc.perform(request)
//                .andExpect(status().isOk());
//    }

    @Test
    void listNormalCarsShouldReturnSatisfiedCars() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/car/eslist")
                .accept(MediaType.APPLICATION_JSON)
                .param(UrlParameter.PAGE_SIZE.val(), "1")
                .param(UrlParameter.PRICE.val(), "10000-100000")
                .param(UrlParameter.BODY_TYPE.val(), "coupe")
                .param(UrlParameter.MAKE_NAME.val(), "land-rover")
                .param(UrlParameter.LISTING_COLOR.val(), "black")
                .param(UrlParameter.YEAR.val(), "2010-2020")
                .param(UrlParameter.MILEAGE.val(), "1500")
                .param(UrlParameter.MAXIMUM_SEATING.val(), "10");

        // expect a normal car
        EsCar expectedEsCar = new EsCar();
        expectedEsCar.setId(1L);
        expectedEsCar.setPrice(10000);
        expectedEsCar.setBodyType("Coupe");
        expectedEsCar.setMakeName("Land Rover");
        expectedEsCar.setListingColor("Black");
        expectedEsCar.setYear(2012);
        expectedEsCar.setMileage(1000);
        expectedEsCar.setMaximumSeating(5);

        List<EsCar> returnedCars = Arrays.asList(expectedEsCar);

        when(esCarService.listCarsWithDynamicQuery(any(Map.class))).thenReturn(returnedCars);

        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        CarVO carVO = new CarVO();
        BeanUtils.copyProperties(expectedEsCar, carVO);
        CommonReturnType expectedResponseBody = CommonReturnType.create(Arrays.asList(carVO));

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(StringUtils.deleteWhitespace(objectMapper.writeValueAsString(expectedResponseBody)),
                StringUtils.deleteWhitespace(actualResponseBody));
    }

    @Test
    void listDepreciatedPickupCarsShouldReturnSatisfiedCars() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/car/eslist")
                .accept(MediaType.APPLICATION_JSON)
                .param(UrlParameter.PAGE_SIZE.val(), "1")
                .param(UrlParameter.PRICE.val(), "10000-100000")
                .param(UrlParameter.BODY_TYPE.val(), "pickup-truck")
                .param(UrlParameter.MAKE_NAME.val(), "land-rover")
                .param(UrlParameter.LISTING_COLOR.val(), "black")
                .param(UrlParameter.YEAR.val(), "2010-2020")
                .param(UrlParameter.MILEAGE.val(), "1500")
                .param(UrlParameter.MAXIMUM_SEATING.val(), "10");

        // expect a normal car
        EsCar expectedEsCar = new EsCar();
        expectedEsCar.setId(1L);
        expectedEsCar.setPrice(10000);
        expectedEsCar.setBodyType("Pickup Truck");
        expectedEsCar.setDepreciated(true);
        expectedEsCar.setMakeName("Land Rover");
        expectedEsCar.setListingColor("Black");
        expectedEsCar.setYear(2012);
        expectedEsCar.setMileage(1000);
        expectedEsCar.setMaximumSeating(5);

        // expect a car which is depreciated and pickup truck
        expectedEsCar.setDepreciated(true);
        expectedEsCar.setBodyType(FieldVal.PICKUP_TRUCK.val());

        List<EsCar> returnedCars = Arrays.asList(expectedEsCar);

        DepreciatedCar expectedDepreciatedCar = new DepreciatedCar();
        expectedDepreciatedCar.setId(expectedEsCar.getId());
        expectedDepreciatedCar.setTheftTitle(true);

        PickupTruck expectedPickupTruck = new PickupTruck();
        expectedPickupTruck.setId(expectedEsCar.getId());
        expectedPickupTruck.setBedLength(BigDecimal.TEN);

        when(esCarService.listCarsWithDynamicQuery(any(Map.class))).thenReturn(returnedCars);
        when(depreciatedCarService.getDepreciatedCarById(anyLong())).thenReturn(Optional.of(expectedDepreciatedCar));
        when(pickupTruckService.getPickupTruckById(anyLong())).thenReturn(Optional.of(expectedPickupTruck));

        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        DepreciatedPickupTruckVO depreciatedPickupTruckVO = new DepreciatedPickupTruckVO();
        BeanUtils.copyProperties(expectedEsCar, depreciatedPickupTruckVO);
        BeanUtils.copyProperties(expectedDepreciatedCar, depreciatedPickupTruckVO);
        BeanUtils.copyProperties(expectedPickupTruck, depreciatedPickupTruckVO);

        CommonReturnType expectedResponseBody = CommonReturnType.create(Arrays.asList(depreciatedPickupTruckVO));

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(StringUtils.deleteWhitespace(objectMapper.writeValueAsString(expectedResponseBody)),
                StringUtils.deleteWhitespace(actualResponseBody));
    }

    @Test
    void listPickupCarsShouldReturnSatisfiedCars() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/car/eslist")
                .accept(MediaType.APPLICATION_JSON)
                .param(UrlParameter.PAGE_SIZE.val(), "1")
                .param(UrlParameter.PRICE.val(), "10000-100000")
                .param(UrlParameter.BODY_TYPE.val(), "pickup-truck")
                .param(UrlParameter.MAKE_NAME.val(), "land-rover")
                .param(UrlParameter.LISTING_COLOR.val(), "black")
                .param(UrlParameter.YEAR.val(), "2010-2020")
                .param(UrlParameter.MILEAGE.val(), "1500")
                .param(UrlParameter.MAXIMUM_SEATING.val(), "10");

        // expect a pickup truck
        EsCar expectedEsCar = new EsCar();
        expectedEsCar.setId(1L);
        expectedEsCar.setPrice(10000);
        expectedEsCar.setBodyType(FieldVal.PICKUP_TRUCK.val());
        expectedEsCar.setMakeName("Land Rover");
        expectedEsCar.setListingColor("Black");
        expectedEsCar.setYear(2012);
        expectedEsCar.setMileage(1000);
        expectedEsCar.setMaximumSeating(5);

        List<EsCar> returnedCars = Arrays.asList(expectedEsCar);

        PickupTruck expectedPickupTruck = new PickupTruck();
        expectedPickupTruck.setId(expectedEsCar.getId());
        expectedPickupTruck.setBedLength(BigDecimal.TEN);

        when(esCarService.listCarsWithDynamicQuery(any(Map.class))).thenReturn(returnedCars);
        when(pickupTruckService.getPickupTruckById(anyLong())).thenReturn(Optional.of(expectedPickupTruck));

        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        PickupTruckVO pickupTruckVO = new PickupTruckVO();
        BeanUtils.copyProperties(expectedEsCar, pickupTruckVO);
        BeanUtils.copyProperties(expectedPickupTruck, pickupTruckVO);

        CommonReturnType expectedResponseBody = CommonReturnType.create(Arrays.asList(pickupTruckVO));

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(StringUtils.deleteWhitespace(objectMapper.writeValueAsString(expectedResponseBody)),
                StringUtils.deleteWhitespace(actualResponseBody));
    }

    @Test
    void listDepreciatedCarsShouldReturnSatisfiedCars() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/car/eslist")
                .accept(MediaType.APPLICATION_JSON)
                .param(UrlParameter.PAGE_SIZE.val(), "1")
                .param(UrlParameter.PRICE.val(), "10000-100000")
                .param(UrlParameter.BODY_TYPE.val(), "pickup-truck")
                .param(UrlParameter.MAKE_NAME.val(), "land-rover")
                .param(UrlParameter.LISTING_COLOR.val(), "black")
                .param(UrlParameter.YEAR.val(), "2010-2020")
                .param(UrlParameter.MILEAGE.val(), "1500")
                .param(UrlParameter.MAXIMUM_SEATING.val(), "10");

        // expect a depreciated car
        EsCar expectedEsCar = new EsCar();
        expectedEsCar.setId(1L);
        expectedEsCar.setPrice(10000);
        expectedEsCar.setBodyType("Coupe");
        expectedEsCar.setMakeName("Land Rover");
        expectedEsCar.setListingColor("Black");
        expectedEsCar.setYear(2012);
        expectedEsCar.setMileage(1000);
        expectedEsCar.setMaximumSeating(5);
        expectedEsCar.setDepreciated(true);

        List<EsCar> returnedCars = Arrays.asList(expectedEsCar);

        DepreciatedCar expectedDepreciatedCar = new DepreciatedCar();
        expectedDepreciatedCar.setId(expectedEsCar.getId());
        expectedDepreciatedCar.setTheftTitle(true);

        when(esCarService.listCarsWithDynamicQuery(any(Map.class))).thenReturn(returnedCars);
        when(depreciatedCarService.getDepreciatedCarById(anyLong())).thenReturn(Optional.of(expectedDepreciatedCar));

        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        DepreciatedCarVO depreciatedCarVO = new DepreciatedCarVO();
        BeanUtils.copyProperties(expectedEsCar, depreciatedCarVO);
        BeanUtils.copyProperties(expectedDepreciatedCar, depreciatedCarVO);

        CommonReturnType expectedResponseBody = CommonReturnType.create(Arrays.asList(depreciatedCarVO));

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(StringUtils.deleteWhitespace(objectMapper.writeValueAsString(expectedResponseBody)),
                StringUtils.deleteWhitespace(actualResponseBody));
    }

    private Car createCar() {
        Car car = new Car();
        car.setId(1L);
        car.setVin("abcdefg");
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
