package org.rental.dto;

import lombok.Getter;

import java.util.*;

@Getter
public abstract class Car {
    private final TreeSet<Reservation> reservations;

    public Car() {
        this.reservations = new TreeSet<>();
    }

    public boolean makeReservation(Reservation newReservation) {
        return reservations.add(newReservation);
    }
}
