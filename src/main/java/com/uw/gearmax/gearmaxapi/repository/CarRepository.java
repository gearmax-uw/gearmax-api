package com.uw.gearmax.gearmaxapi.repository;

import com.uw.gearmax.gearmaxapi.domain.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CarRepository extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {

    Page<Car> findAllByBodyType(String bodyType, Pageable pageable);
    
}
