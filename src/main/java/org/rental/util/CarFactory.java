package org.rental.util;

import lombok.experimental.UtilityClass;
import org.rental.dto.Car;
import org.rental.dto.Sedan;
import org.rental.dto.Suv;
import org.rental.dto.Van;

@UtilityClass
public class CarFactory {

    public Car create(CarType type) {
        return switch (type) {
            case SEDAN -> new Sedan();
            case SUV -> new Suv();
            case VAN -> new Van();
        };
    }

}
