package com.uw.gearmax.gearmaxapi.repository;

import com.uw.gearmax.gearmaxapi.domain.DepreciatedCar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepreciatedCarRepository extends JpaRepository<DepreciatedCar, Long> {
}
