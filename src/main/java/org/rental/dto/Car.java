package org.rental.dto;

import lombok.Getter;

import java.util.*;

@Getter
public abstract class Car {
    private final List<Reservation> reservations;

    public Car() {
        this.reservations = new ArrayList<>();
    }

    public boolean makeReservation(Reservation newReservation) {
        if (reservations.isEmpty()) {
            reservations.add(newReservation);
            return true;
        } else if (reservations.get(0).isAfter(newReservation)) {
            reservations.add(0, newReservation);
            return true;
        } else {
            for (int i = 0; i < reservations.size(); i++) {
                if (reservations.get(i).isBefore(newReservation)) {
                    if (reservations.size() == i + 1) {
                        reservations.add(newReservation);
                        return true;
                    } else if (reservations.get(i + 1).isAfter(newReservation)) {
                        reservations.add(i + 1, newReservation);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
