package com.uw.gearmax.gearmaxapi.service;

import com.uw.gearmax.gearmaxapi.domain.DepreciatedCar;
import com.uw.gearmax.gearmaxapi.error.BusinessException;

import java.util.Optional;

public interface DepreciatedCarService {

    DepreciatedCar saveDepreciatedCar(DepreciatedCar car);

    DepreciatedCar getDepreciatedCarById(Long id) throws BusinessException;

    Optional<DepreciatedCar> getOptionalDepreciatedCarById(Long id);

    DepreciatedCar removeDepreciatedCar(Long id) throws BusinessException;
}
