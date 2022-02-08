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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepreciatedCarServiceImplIntegrationTest {

    @Mock
    private DepreciatedCarRepository depreciatedCarRepository;

    @Autowired
    @InjectMocks
    private DepreciatedCarServiceImpl depreciatedCarService;

    private DepreciatedCar depreciatedCar;

    @BeforeEach
    public void setUp() {
        depreciatedCar = createDepreciatedCar(1L);
    }

    @AfterEach
    public void tearDown() {
        depreciatedCar = null;
    }

    @Test
    void getDepreciatedCarByIdShouldReturnExpectedDepreciatedCar() throws Exception {
        when(depreciatedCarRepository.findById(anyLong())).thenReturn(Optional.of(depreciatedCar));
        DepreciatedCar returnedDepreciatedCar = depreciatedCarService.getDepreciatedCarById(1L);
        assertThat(returnedDepreciatedCar).isEqualTo(depreciatedCar);
    }

    @Test
    void getEmptyDepreciatedCarByIdShouldThrowException() {
        when(depreciatedCarRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> depreciatedCarService.getDepreciatedCarById(anyLong()));
    }

    @Test
    void givenDepreciatedCarToSaveShouldReturnSavedCar() {
        when(depreciatedCarRepository.save(any(DepreciatedCar.class))).thenReturn(depreciatedCar);

        depreciatedCarService.saveDepreciatedCar(depreciatedCar);

        verify(depreciatedCarRepository, times(1)).save(any(DepreciatedCar.class));
    }

    @Test
    void givenIdToRemoveThenShouldRemoveDepreciatedCar() throws Exception {
        when(depreciatedCarService.getOptionalDepreciatedCarById(anyLong())).thenReturn(Optional.of(depreciatedCar));
        depreciatedCarService.removeDepreciatedCar(depreciatedCar.getId());

        verify(depreciatedCarRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void givenNotExistDepreciatedCarToRemoveThenShouldThrowException() {
        Optional<DepreciatedCar> notExistDepreciatedCar = Optional.empty();
        when(depreciatedCarService.getOptionalDepreciatedCarById(anyLong())).thenReturn(notExistDepreciatedCar);

        assertThrows(BusinessException.class, () -> depreciatedCarService.removeDepreciatedCar(anyLong()));
        // assertEquals("Depreciated car to be obtained does not exist", e.getErrMsg());
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
