package Helpers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    @ParameterizedTest(name = "{index}: getOppositeDirection(Direction.{0}) = {1}")
    @MethodSource("provideDirectionsForgetOppositeDirection")
    void getOppositeDirection(Direction direction, Direction actualOppositeDirection) {
        assertNotEquals(direction, actualOppositeDirection);
        assertEquals(actualOppositeDirection, Direction.getOppositeDirection(direction));
    }

    private static Stream<Arguments> provideDirectionsForgetOppositeDirection() {
        return Stream.of(
                Arguments.of(Direction.UP, Direction.DOWN),
                Arguments.of(Direction.DOWN, Direction.UP),
                Arguments.of(Direction.RIGHT, Direction.LEFT),
                Arguments.of(Direction.LEFT, Direction.RIGHT)
        );
    }
}