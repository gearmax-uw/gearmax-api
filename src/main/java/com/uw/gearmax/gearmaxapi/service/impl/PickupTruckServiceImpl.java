package com.uw.gearmax.gearmaxapi.service.impl;

import com.uw.gearmax.gearmaxapi.domain.PickupTruck;
import com.uw.gearmax.gearmaxapi.error.BusinessException;
import com.uw.gearmax.gearmaxapi.error.EmBusinessError;
import com.uw.gearmax.gearmaxapi.repository.PickupTruckRepository;
import com.uw.gearmax.gearmaxapi.service.PickupTruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class PickupTruckServiceImpl implements PickupTruckService {

    @Autowired
    private PickupTruckRepository pickupTruckRepository;

    @Override
    @Transactional
    public PickupTruck savePickupTruck(PickupTruck truck) {
        return pickupTruckRepository.save(truck);
    }

    @Override
    public PickupTruck removePickupTruck(Long id) throws BusinessException {
        // delete from pick_truck where id = 'id'
        Optional<PickupTruck> returnedTruck = getPickupTruckById(id);
        if (!returnedTruck.isPresent()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,
                    "Car to be removed does not exist");
        }

        pickupTruckRepository.deleteById(id);
        return returnedTruck.get();
    }

    @Override
    public Optional<PickupTruck> getPickupTruckById(Long id) {
        return pickupTruckRepository.findById(id);
    }
}