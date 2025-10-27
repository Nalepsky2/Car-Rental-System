package org.rental.service;

import lombok.Getter;
import org.rental.dto.Car;
import org.rental.dto.Reservation;
import org.rental.dto.Result;
import org.rental.util.CarFactory;
import org.rental.util.CarType;

import java.time.LocalDateTime;
import java.util.*;

@Getter
public class RentService {
    private final Map<CarType, List<Car>> fleet;

    public RentService(Map<CarType, Integer> carLimits) {
        fleet = new HashMap<>();

        for (Map.Entry<CarType, Integer> carLimit : carLimits.entrySet()) {
            List<Car> cars = new ArrayList<>();
            if (carLimit.getValue() > 0) {
                for (int i = 0; i < carLimit.getValue(); i++) {
                    cars.add(CarFactory.create(carLimit.getKey()));
                }
            }
            fleet.put(carLimit.getKey(), cars);
        }
    }

    public Result rentCar(CarType type, LocalDateTime start, int days) {
        if (start != null && start.isAfter(LocalDateTime.now())) {
            if (fleet.containsKey(type)) {
                Reservation reservation = new Reservation(start, days);
                for (Car car : fleet.get(type)) {
                    if (car.makeReservation(reservation)) {
                        return new Result(true);
                    }
                }
            }
        }
        return new Result(false);
    }
}
