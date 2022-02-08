package com.uw.gearmax.gearmaxapi.service.impl;

import com.uw.gearmax.gearmaxapi.domain.DepreciatedCar;
import com.uw.gearmax.gearmaxapi.error.BusinessException;
import com.uw.gearmax.gearmaxapi.error.EmBusinessError;
import com.uw.gearmax.gearmaxapi.repository.DepreciatedCarRepository;
import com.uw.gearmax.gearmaxapi.service.DepreciatedCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DepreciatedCarServiceImpl implements DepreciatedCarService {

    @Autowired
    private DepreciatedCarRepository depreciatedCarRepository;

    @Override
    public DepreciatedCar saveDepreciatedCar(DepreciatedCar car) {
        return depreciatedCarRepository.save(car);
    }

    @Override
    public DepreciatedCar getDepreciatedCarById(Long id) throws BusinessException {
        Optional<DepreciatedCar> optionalDepreciatedCar = getOptionalDepreciatedCarById(id);
        if (!optionalDepreciatedCar.isPresent()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                    "Depreciated car to be obtained does not exist");
        }
        return optionalDepreciatedCar.get();
    }

    @Override
    public Optional<DepreciatedCar> getOptionalDepreciatedCarById(Long id) {
        return depreciatedCarRepository.findById(id);
    }

    @Override
    public DepreciatedCar removeDepreciatedCar(Long id) throws BusinessException {
        DepreciatedCar depreciatedCar = getDepreciatedCarById(id);
        depreciatedCarRepository.deleteById(id);
        return depreciatedCar;
    }
}
