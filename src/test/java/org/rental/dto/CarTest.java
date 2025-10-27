package org.rental.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarTest {
    private Car tested;

    @BeforeEach
    void setUp() {
        tested = new TestCar();
    }

    @Test
    void makeReservation_emptyReservations_shouldAddNewReservation() {
        //given
        tested = new TestCar();
        LocalDateTime start = LocalDateTime.of(2025, 2, 2, 12, 0);
        Reservation newReservation = new Reservation(start, 18);

        //when
        boolean result = tested.makeReservation(newReservation);

        //then
        assertTrue(result);
        assertThat(tested.getReservations()).containsExactly(newReservation);
    }

    @Test
    void makeReservation_oneReservation_shouldAddNewReservationBefore() {
        //given
        LocalDateTime oldStart = LocalDateTime.of(2025, 2, 2, 12, 0);
        Reservation oldReservation = new Reservation(oldStart, 18);
        tested.makeReservation(oldReservation);

        LocalDateTime start = LocalDateTime.of(2025, 1, 2, 12, 0);
        Reservation newReservation = new Reservation(start, 18);

        //when
        boolean result = tested.makeReservation(newReservation);

        //then
        assertTrue(result);
        assertThat(tested.getReservations()).containsExactly(newReservation, oldReservation);
    }

    @Test
    void makeReservation_oneReservation_shouldAddNewReservationAfter() {
        //given
        LocalDateTime oldStart = LocalDateTime.of(2025, 2, 2, 12, 0);
        Reservation oldReservation = new Reservation(oldStart, 18);
        tested.makeReservation(oldReservation);

        LocalDateTime start = LocalDateTime.of(2025, 3, 2, 12, 0);
        Reservation newReservation = new Reservation(start, 18);

        //when
        boolean result = tested.makeReservation(newReservation);

        //then
        assertTrue(result);
        assertThat(tested.getReservations()).containsExactly(oldReservation, newReservation);
    }

    @Test
    void makeReservation_multipleReservations_shouldAddNewReservationBetween() {
        //given
        LocalDateTime oldStart1 = LocalDateTime.of(2025, 2, 2, 12, 0);
        Reservation oldReservation1 = new Reservation(oldStart1, 18);
        tested.makeReservation(oldReservation1);

        LocalDateTime oldStart2 = LocalDateTime.of(2025, 4, 2, 12, 0);
        Reservation oldReservation2 = new Reservation(oldStart2, 18);
        tested.makeReservation(oldReservation2);

        LocalDateTime start = LocalDateTime.of(2025, 3, 2, 12, 0);
        Reservation newReservation = new Reservation(start, 18);

        //when
        boolean result = tested.makeReservation(newReservation);

        //then
        assertTrue(result);
        assertThat(tested.getReservations()).containsExactly(oldReservation1, newReservation, oldReservation2);
    }

    @Test
    void makeReservation_noFreeSpaceForReservation_shouldNotAddNewReservation() {
        //given
        LocalDateTime oldStart1 = LocalDateTime.of(2025, 2, 2, 12, 0);
        Reservation oldReservation1 = new Reservation(oldStart1, 18);
        tested.makeReservation(oldReservation1);

        LocalDateTime oldStart2 = LocalDateTime.of(2025, 4, 2, 12, 0);
        Reservation oldReservation2 = new Reservation(oldStart2, 18);
        tested.makeReservation(oldReservation2);

        LocalDateTime oldStart3 = LocalDateTime.of(2025, 5, 2, 12, 0);
        Reservation oldReservation3 = new Reservation(oldStart3, 18);
        tested.makeReservation(oldReservation3);

        LocalDateTime start = LocalDateTime.of(2025, 4, 2, 12, 0);
        Reservation newReservation = new Reservation(start, 18);

        //when
        boolean result = tested.makeReservation(newReservation);

        //then
        assertFalse(result);
        assertThat(tested.getReservations()).containsExactly(oldReservation1, oldReservation2, oldReservation3);
    }

    @Test
    void makeReservation_newReservationOverlapsWithMultipleOther_shouldNotAddNewReservation() {
        //given
        LocalDateTime oldStart1 = LocalDateTime.of(2025, 2, 2, 12, 0);
        Reservation oldReservation1 = new Reservation(oldStart1, 18);
        tested.makeReservation(oldReservation1);

        LocalDateTime oldStart2 = LocalDateTime.of(2025, 4, 2, 12, 0);
        Reservation oldReservation2 = new Reservation(oldStart2, 18);
        tested.makeReservation(oldReservation2);

        LocalDateTime oldStart3 = LocalDateTime.of(2025, 5, 2, 12, 0);
        Reservation oldReservation3 = new Reservation(oldStart3, 18);
        tested.makeReservation(oldReservation3);

        LocalDateTime start = LocalDateTime.of(2025, 2, 1, 12, 0);
        Reservation newReservation = new Reservation(start, 18);

        //when
        boolean result = tested.makeReservation(newReservation);

        //then
        assertFalse(result);
        assertThat(tested.getReservations()).containsExactly(oldReservation1, oldReservation2, oldReservation3);
    }

    @Test
    void makeReservation_newReservationOverlapsWithAllOther_shouldNotAddNewReservation() {
        //given
        LocalDateTime oldStart1 = LocalDateTime.of(2025, 2, 2, 12, 0);
        Reservation oldReservation1 = new Reservation(oldStart1, 18);
        tested.makeReservation(oldReservation1);

        LocalDateTime oldStart2 = LocalDateTime.of(2025, 4, 2, 12, 0);
        Reservation oldReservation2 = new Reservation(oldStart2, 18);
        tested.makeReservation(oldReservation2);

        LocalDateTime oldStart3 = LocalDateTime.of(2025, 5, 2, 12, 0);
        Reservation oldReservation3 = new Reservation(oldStart3, 18);
        tested.makeReservation(oldReservation3);

        LocalDateTime start = LocalDateTime.of(2025, 2, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2025, 5, 21, 12, 0);
        Reservation newReservation = new Reservation(start, 99);

        //when
        boolean result = tested.makeReservation(newReservation);

        //then
        assertFalse(result);
        assertThat(tested.getReservations()).containsExactly(oldReservation1, oldReservation2, oldReservation3);
    }

    private static class TestCar extends Car {}
}