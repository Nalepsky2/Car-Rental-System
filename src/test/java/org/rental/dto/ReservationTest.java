package org.rental.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    void newReservation_startTimeIsMissing_shouldThrowNullPointerException() {
        //given
        //when
        Exception exception = assertThrows(NullPointerException.class, () -> new Reservation(null, 1));

        //then
        String expectedMessage = "start date is missing";
        assertThat(exception).hasMessage(expectedMessage);
    }

    @Test
    void newReservation_durationIsLessThanZero_shouldThrowIllegalArgumentException() {
        //given
        LocalDateTime start = LocalDateTime.of(2025, 2, 2, 12, 0);

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Reservation(start, -1));

        //then
        String expectedMessage = "Reservation duration cannot be a negative number";
        assertThat(exception).hasMessage(expectedMessage);
    }

    @Test
    void newReservation_durationIsZero_shouldCreateReservationThatEndsJustBeforeMidnight() {
        //given
        LocalDateTime start = LocalDateTime.of(2025, 2, 2, 12, 0);

        //when
        Reservation actual = new Reservation(start, 0);

        //then
        LocalDateTime expected = LocalDateTime.of(2025, 2, 2, 23, 59);
        assertThat(actual.getEnd()).isEqualTo(expected);
        assertThat(actual.getStart()).isEqualTo(start);
    }

    @Test
    void newReservation_durationIsPositive_shouldCreateReservationThatEndsAfterDurationDays() {
        //given
        LocalDateTime start = LocalDateTime.of(2025, 2, 2, 12, 0);

        //when
        Reservation actual = new Reservation(start, 10);

        //then
        LocalDateTime expected = LocalDateTime.of(2025, 2, 12, 12, 0);
        assertThat(actual.getEnd()).isEqualTo(expected);
        assertThat(actual.getStart()).isEqualTo(start);
    }

    @Test
    void isBefore_isBefore_shouldReturnTrue() {
        //given
        Reservation currentReservation = getCurrentReservation();

        LocalDateTime start = LocalDateTime.of(2025, 2, 2, 12, 0);
        Reservation newReservation = new Reservation(start, 18);

        //then
        boolean result = currentReservation.isBefore(newReservation);

        //then
        assertTrue(result);
    }

    @Test
    void isBefore_isAfter_shouldReturnFalse() {
        //given
        Reservation currentReservation = getCurrentReservation();

        LocalDateTime start = LocalDateTime.of(2024, 12, 1, 12, 0);
        Reservation newReservation = new Reservation(start, 30);

        //then
        boolean result = currentReservation.isBefore(newReservation);

        //then
        assertFalse(result);
    }

    @Test
    void isBefore_overlap_shouldReturnFalse() {
        //given
        Reservation currentReservation = getCurrentReservation();

        LocalDateTime start = LocalDateTime.of(2025, 1, 15, 12, 0);
        Reservation newReservation = new Reservation(start, 40);

        //then
        boolean result = currentReservation.isBefore(newReservation);

        //then
        assertFalse(result);
    }

    @Test
    void isAfter_isAfter_shouldReturnTrue() {
        //given
        Reservation currentReservation = getCurrentReservation();

        LocalDateTime start = LocalDateTime.of(2024, 12, 1, 12, 0);
        Reservation newReservation = new Reservation(start, 30);

        //then
        boolean result = currentReservation.isAfter(newReservation);

        //then
        assertTrue(result);
    }

    @Test
    void isAfter_isBefore_shouldReturnTrue() {
        //given
        Reservation currentReservation = getCurrentReservation();

        LocalDateTime start = LocalDateTime.of(2025, 2, 2, 12, 0);
        Reservation newReservation = new Reservation(start, 18);

        //then
        boolean result = currentReservation.isAfter(newReservation);

        //then
        assertFalse(result);
    }

    @Test
    void isAfter_overlap_shouldReturnTrue() {
        //given
        Reservation currentReservation = getCurrentReservation();

        LocalDateTime start = LocalDateTime.of(2025, 1, 15, 12, 0);
        Reservation newReservation = new Reservation(start, 40);

        //then
        boolean result = currentReservation.isAfter(newReservation);

        //then
        assertFalse(result);
    }

    private Reservation getCurrentReservation() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 12, 0);
        return new Reservation(start, 31);
    }

}