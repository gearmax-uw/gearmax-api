package com.uw.gearmax.gearmaxapi.service;

import com.uw.gearmax.gearmaxapi.domain.DepreciatedCar;
import com.uw.gearmax.gearmaxapi.error.BusinessException;
import com.uw.gearmax.gearmaxapi.repository.DepreciatedCarRepository;
import com.uw.gearmax.gearmaxapi.service.impl.DepreciatedCarServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepreciatedCarServiceImplIntegrationTest {

    @Mock
    private DepreciatedCarRepository depreciatedCarRepository;

    @Autowired
    @InjectMocks
    private DepreciatedCarServiceImpl depreciatedCarService;

    private DepreciatedCar depreciatedCar1;

    @BeforeEach
    public void setUp() {
        depreciatedCar1 = createDepreciatedCar(1L);
    }

    @AfterEach
    public void tearDown() {
        depreciatedCar1 = null;
    }

    @Test
    public void givenDepreciatedCarToSaveShouldReturnSavedCar() throws Exception {
        when(depreciatedCarRepository.save(any(DepreciatedCar.class))).thenReturn(depreciatedCar1);

        depreciatedCarService.saveDepreciatedCar(depreciatedCar1);

        verify(depreciatedCarRepository, times(1)).save(any(DepreciatedCar.class));
    }

    @Test
    public void givenIdToRemoveThenShouldRemoveDepreciatedCar() throws Exception {
        when(depreciatedCarService.getDepreciatedCarById(anyLong())).thenReturn(Optional.of(depreciatedCar1));

        depreciatedCarService.removeDepreciatedCar(depreciatedCar1.getId());

        verify(depreciatedCarRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void givenNotExistDepreciatedCarToRemoveThenShouldThrowException() {
        Optional<DepreciatedCar> notExistDepreciatedCar = Optional.empty();
        when(depreciatedCarService.getDepreciatedCarById(anyLong())).thenReturn(notExistDepreciatedCar);

        BusinessException e = assertThrows(BusinessException.class, () -> depreciatedCarService.removeDepreciatedCar(anyLong()));
        assertEquals(e.getErrMsg(), "Depreciated car to be removed does not exist");
    }

    private DepreciatedCar createDepreciatedCar(Long id) {
        DepreciatedCar depreciatedCar = new DepreciatedCar();
        depreciatedCar.setId(id);
        depreciatedCar.setFrameDamaged(true);
        depreciatedCar.setHasAccidents(true);
        depreciatedCar.setSalvaged(true);
        depreciatedCar.setCab(true);
        depreciatedCar.setTheftTitle(true);
        return depreciatedCar;
    }
}
