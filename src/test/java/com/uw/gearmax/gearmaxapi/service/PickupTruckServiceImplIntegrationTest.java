package com.uw.gearmax.gearmaxapi.service;

import com.uw.gearmax.gearmaxapi.domain.PickupTruck;
import com.uw.gearmax.gearmaxapi.error.BusinessException;
import com.uw.gearmax.gearmaxapi.repository.PickupTruckRepository;
import com.uw.gearmax.gearmaxapi.service.impl.PickupTruckServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PickupTruckServiceImplIntegrationTest {

    @Mock
    private PickupTruckRepository pickupTruckRepository;

    @Autowired
    @InjectMocks
    private PickupTruckServiceImpl pickupTruckService;

    private PickupTruck pickupTruck;

    @BeforeEach
    public void setUp() {
        pickupTruck = createPickupTruck(1L);
    }

    @AfterEach
    public void tearDown() {
        pickupTruck = null;
    }

    @Test
    void getPickupTruckByIdShouldReturnExpectedPickupTruck() throws Exception {
        when(pickupTruckRepository.findById(anyLong())).thenReturn(Optional.of(pickupTruck));
        PickupTruck returnedPickupTruck = pickupTruckService.getPickupTruckById(1L);
        assertThat(returnedPickupTruck).isEqualTo(pickupTruck);
    }

    @Test
    void getEmptyPickupTruckByIdShouldThrowException() {
        when(pickupTruckRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> pickupTruckService.getPickupTruckById(anyLong()));
    }

    private PickupTruck createPickupTruck(Long id) {
        PickupTruck pickupTruck = new PickupTruck();
        pickupTruck.setBedLength(BigDecimal.TEN);
        pickupTruck.setCabin("abc");
        pickupTruck.setBed("def");
        return pickupTruck;
    }
}
