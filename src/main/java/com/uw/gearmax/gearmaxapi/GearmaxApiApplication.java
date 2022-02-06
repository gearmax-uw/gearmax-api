package com.uw.gearmax.gearmaxapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.uw.gearmax.gearmaxapi.config"})
@SpringBootApplication
public class GearmaxApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GearmaxApiApplication.class, args);
    }

}
