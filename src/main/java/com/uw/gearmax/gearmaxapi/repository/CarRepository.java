package com.uw.gearmax.gearmaxapi.repository;

import com.uw.gearmax.gearmaxapi.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {

}
