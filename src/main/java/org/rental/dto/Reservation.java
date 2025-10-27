package org.rental.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Reservation {
    private final LocalDateTime start;
    private final LocalDateTime end;

    public Reservation(LocalDateTime start, int days) {
        this.start = Objects.requireNonNull(start, "start date is missing");

        if (days < 0) {
            throw new IllegalArgumentException("Reservation duration cannot be a negative number");
        } else if (days == 0) {
            //return same day at midnight
            this.end = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 23, 59);
        } else {
            this.end = start.plusDays(days);
        }
    }

    boolean isBefore(Reservation other) {
        return this.end.isBefore(other.start);
    }

    boolean isAfter(Reservation other) {
        return this.start.isAfter(other.end);
    }
}
