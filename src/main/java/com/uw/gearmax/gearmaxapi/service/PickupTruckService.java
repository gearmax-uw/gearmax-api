package com.uw.gearmax.gearmaxapi.service;
// add, find delete, update <_ function of service
import com.uw.gearmax.gearmaxapi.domain.PickupTruck;
import com.uw.gearmax.gearmaxapi.error.BusinessException;

import java.util.List;
import java.util.Optional;

public interface PickupTruckService { //save objects into back_end
    PickupTruck savePickupTruck(PickupTruck truck);

    PickupTruck removePickupTruck(Long id) throws BusinessException;

    Optional<PickupTruck> getPickupTruckById(long id); //前端调API处断后端function操作数据库在返回数值给前端
}
