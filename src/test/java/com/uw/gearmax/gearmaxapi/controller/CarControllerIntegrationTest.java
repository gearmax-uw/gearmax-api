package com.uw.gearmax.gearmaxapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uw.gearmax.gearmaxapi.controller.viewobject.CarVO;
import com.uw.gearmax.gearmaxapi.domain.Car;
import com.uw.gearmax.gearmaxapi.response.CommonReturnType;
import com.uw.gearmax.gearmaxapi.service.CarService;
import com.uw.gearmax.gearmaxapi.service.DepreciatedCarService;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
    private CarService carService;

    @Test
    void addCarShouldReturnCarVO() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/car/add")
                .accept(MediaType.APPLICATION_JSON)
                .param("vin", "abcdefg")
                .param("sellerId", "2")
                .param("price", "99999")
                .param("year", "2020")
                .param("mileage", "10")
                .param("bodyType", "SUV")
                .param("zip", "000111")
                .param("city", "Seattle")
                .param("country", "USA")
                .param("listedDate", "2020-01-01");

        Car car = createCar();
        when(carService.saveCar(any(Car.class))).thenReturn(car);

        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        CarVO carVO = new CarVO();
        BeanUtils.copyProperties(car, carVO);
        CommonReturnType expectedResponseBody = CommonReturnType.create(carVO);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(StringUtils.deleteWhitespace(objectMapper.writeValueAsString(expectedResponseBody)),
                StringUtils.deleteWhitespace(actualResponseBody));
    }

    @Test
    void removeCarShouldReturnSuccessCode() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .delete("/car/delete/1")
                .contentType(MediaType.APPLICATION_JSON);

        when(carService.getCarById(1L)).thenReturn(Optional.of(createCar()));

        mockMvc.perform(request)
                .andExpect(status().isOk());
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
