package org.rental.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.rental.dto.Result;
import org.rental.util.CarType;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.rental.util.CarType.*;

class RentServiceTest {
    private RentService tested;

    private static final Instant FIXED_INSTANT = Instant.parse("2024-01-01T00:00:00Z");
    private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_INSTANT, ZoneId.systemDefault());

    private static MockedStatic<Clock> clockMock;

    @BeforeEach
    void setUp() {
        clockMock = mockStatic(Clock.class);
        clockMock.when(Clock::systemDefaultZone).thenReturn(FIXED_CLOCK);
    }

    @AfterEach
    void tearDown() {
        clockMock.close();
    }

    @Test
    void rentService_CarLimitsPerTypeTest() {
        //given
        Map<CarType, Integer> carLimits = Map.of(SUV, 1, SEDAN, 2, VAN, 4);

        //when
        tested = new RentService(carLimits);

        //then
        assertThat(tested.getFleet().get(SUV).size()).isEqualTo(1);
        assertThat(tested.getFleet().get(SEDAN).size()).isEqualTo(2);
        assertThat(tested.getFleet().get(VAN).size()).isEqualTo(4);
    }

    @Test
    void rentService_negativeLimit_shouldNotCreateFleetForInvalidInput() {
        //given
        Map<CarType, Integer> carLimits = Map.of(SUV, -1, SEDAN, 2, VAN, 4);

        //when
        tested = new RentService(carLimits);

        //then
        assertThat(tested.getFleet().get(SUV)).isEmpty();
        assertThat(tested.getFleet().get(SEDAN).size()).isEqualTo(2);
        assertThat(tested.getFleet().get(VAN).size()).isEqualTo(4);
    }

    @Test
    void rentService_zeroLimit_shouldNotCreateFleetForInvalidInput() {
        //given
        Map<CarType, Integer> carLimits = Map.of(SUV, 0, SEDAN, 2, VAN, 4);

        //when
        tested = new RentService(carLimits);

        //then
        assertThat(tested.getFleet().get(SUV)).isEmpty();
        assertThat(tested.getFleet().get(SEDAN).size()).isEqualTo(2);
        assertThat(tested.getFleet().get(VAN).size()).isEqualTo(4);
    }

    @Test
    void rentService_missingCarType_shouldNotCreateFleetForMissingType() {
        //given
        Map<CarType, Integer> carLimits = Map.of(SEDAN, 2, VAN, 4);

        //when
        tested = new RentService(carLimits);

        //then
        assertThat(tested.getFleet().get(SUV)).isNull();
        assertThat(tested.getFleet().get(SEDAN).size()).isEqualTo(2);
        assertThat(tested.getFleet().get(VAN).size()).isEqualTo(4);
    }

    @Test
    void rentCar_allSlotsFree_shouldReturnTrue() {
        //given
        Map<CarType, Integer> carLimits = Map.of(SUV, 1, SEDAN, 2, VAN, 4);
        tested = new RentService(carLimits);

        LocalDateTime start = LocalDateTime.of(2025, 2, 2, 12, 0);

        //when
        Result actual = tested.rentCar(SEDAN, start, 18);

        //then
        assertTrue(actual.isSuccessful());
    }

    @Test
    void rentCar_reservationStartBeforeNow_shouldReturnTrue() {
        //given
        Map<CarType, Integer> carLimits = Map.of(SUV, 1, SEDAN, 2, VAN, 4);
        tested = new RentService(carLimits);

        LocalDateTime start = LocalDateTime.of(2020, 2, 2, 12, 0);

        //when
        Result actual = tested.rentCar(SEDAN, start, 18);

        //then
        assertFalse(actual.isSuccessful());
    }

    @Test
    void rentCar_startDayIsNotSpecified_shouldReturnTrue() {
        //given
        Map<CarType, Integer> carLimits = Map.of(SUV, 1, SEDAN, 2, VAN, 4);
        tested = new RentService(carLimits);

        //when
        Result actual = tested.rentCar(SEDAN, null, 18);

        //then
        assertFalse(actual.isSuccessful());
    }

    @Test
    void rentCar_freeSlotForOneCar_shouldReturnTrue() {
        //given
        Map<CarType, Integer> carLimits = Map.of(SUV, 1, SEDAN, 3, VAN, 4);
        tested = new RentService(carLimits);

        LocalDateTime start1 = LocalDateTime.of(2025, 2, 2, 12, 0);
        tested.rentCar(SEDAN, start1, 18);
        LocalDateTime start2 = LocalDateTime.of(2025, 2, 2, 12, 0);
        tested.rentCar(SEDAN, start2, 18);

        LocalDateTime newStart = LocalDateTime.of(2025, 2, 2, 12, 0);

        //when
        Result actual = tested.rentCar(SEDAN, newStart, 18);

        //then
        assertTrue(actual.isSuccessful());
    }

    @Test
    void rentCar_noFreeSlots_shouldReturnFalse() {
        //given
        Map<CarType, Integer> carLimits = Map.of(SUV, 1, SEDAN, 2, VAN, 4);
        tested = new RentService(carLimits);

        LocalDateTime start1 = LocalDateTime.of(2025, 2, 2, 12, 0);
        tested.rentCar(SEDAN, start1, 18);
        LocalDateTime start2 = LocalDateTime.of(2025, 2, 2, 12, 0);
        tested.rentCar(SEDAN, start2, 18);

        LocalDateTime newStart = LocalDateTime.of(2025, 2, 2, 12, 0);

        //when
        Result actual = tested.rentCar(SEDAN, newStart, 18);

        //then
        assertFalse(actual.isSuccessful());
    }

    @Test
    void rentCar_typeNotAvailable_shouldReturnFalse() {
        //given
        Map<CarType, Integer> carLimits = Map.of(SUV, 1, SEDAN, 0, VAN, 4);
        tested = new RentService(carLimits);

        LocalDateTime newStart = LocalDateTime.of(2025, 2, 2, 12, 0);

        //when
        Result actual = tested.rentCar(SEDAN, newStart, 18);

        //then
        assertFalse(actual.isSuccessful());
    }

}