package org.rental.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.rental.dto.Car;
import org.rental.dto.Sedan;
import org.rental.dto.Suv;
import org.rental.dto.Van;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CarFactoryTest {

    @ParameterizedTest
    @MethodSource("provideCarTypes")
    void create_shouldCreateCorrectTypeOfCar(CarType type, Class<?> clazz) {
        //given
        //when
        Car result = CarFactory.create(type);

        //then
        assertThat(result).isInstanceOf(clazz);
    }

    private static Stream<Arguments> provideCarTypes() {
        return Stream.of(
                Arguments.of(CarType.SEDAN, Sedan.class),
                Arguments.of(CarType.SUV, Suv.class),
                Arguments.of(CarType.VAN, Van.class)
        );
    }

}