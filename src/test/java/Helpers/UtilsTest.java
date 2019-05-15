package Helpers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @ParameterizedTest(name = "{index}: moveCoordinatesToDirection({0}, Direction.{1}) = {2}")
    @MethodSource("provideDataForMoveCoordinatesToDirectionWithCoordinates")
    void moveCoordinatesToDirectionWithCoordinates(Coordinate from, Direction directionOfMovement, Coordinate expected) {
        Coordinate result = Utils.Instance.moveCoordinatesToDirection(from, directionOfMovement);

        assertNotNull(result);
        assertNotEquals(from, result);

        assertCoordinatesEqual(expected, result);
    }

    private static Stream<Arguments> provideDataForMoveCoordinatesToDirectionWithCoordinates() {
        return Stream.of(
                Arguments.of(
                        new Coordinate(7, 3),
                        Direction.UP,
                        new Coordinate(6, 3)
                ),
                Arguments.of(
                        new Coordinate(7, 3),
                        Direction.DOWN,
                        new Coordinate(8, 3)
                ),
                Arguments.of(
                        new Coordinate(7, 3),
                        Direction.RIGHT,
                        new Coordinate(7, 4)
                ),
                Arguments.of(
                        new Coordinate(7, 3),
                        Direction.LEFT,
                        new Coordinate(7, 2)
                )
        );
    }


    @ParameterizedTest(name = "{index}: moveCoordinatesToDirection({0}, {1}, Direction.{2}) = {3}")
    @MethodSource("provideDataForMoveCoordinatesToDirectionWithRowAndColumn")
    void moveCoordinatesToDirectionWithRowAndColumn(int fromRow, int fromCol, Direction directionOfMovement, Coordinate expected) {

        Coordinate result = Utils.Instance.moveCoordinatesToDirection(fromRow, fromCol, directionOfMovement);

        assertNotNull(result);

        if (fromRow == expected.getRow()) {
            assertEquals(fromRow, result.getRow());
        } else {
            assertNotEquals(fromRow, result.getRow());
        }

        if (fromCol == expected.getColumn()) {
            assertEquals(fromCol, result.getColumn());
        } else {
            assertNotEquals(fromCol, result.getColumn());
        }

        assertCoordinatesEqual(expected, result);
    }

    private static Stream<Arguments> provideDataForMoveCoordinatesToDirectionWithRowAndColumn() {
        return Stream.of(
                Arguments.of(
                        7, 3,
                        Direction.UP,
                        new Coordinate(6, 3)
                ),
                Arguments.of(
                        7, 3,
                        Direction.DOWN,
                        new Coordinate(8, 3)
                ),
                Arguments.of(
                        7, 3,
                        Direction.RIGHT,
                        new Coordinate(7, 4)
                ),
                Arguments.of(
                        7, 3,
                        Direction.LEFT,
                        new Coordinate(7, 2)
                )
        );
    }


    private void assertCoordinatesEqual(Coordinate expectedCoordinates, Coordinate resultCoordinates) {
        assertNotNull(expectedCoordinates);
        assertNotNull(resultCoordinates);

        assertEquals(expectedCoordinates.getRow(), resultCoordinates.getRow());
        assertEquals(expectedCoordinates.getColumn(), resultCoordinates.getColumn());
    }


    @ParameterizedTest(name = "{index}: getDirectionOfMove(from: {0}, to: {1}) = Direction.{2}")
    @MethodSource("provideDataForGetDirectionOfMove")
    void getDirectionOfMove(Coordinate currentLocation,
                            Coordinate afterMoveCoordinates,
                            Direction moveDirection) {
        Direction result = Utils.Instance.getDirectionOfMove(currentLocation, afterMoveCoordinates);

        assertNotNull(result);
        assertEquals(moveDirection, result);
    }


    private static Stream<Arguments> provideDataForGetDirectionOfMove() {
        return Stream.of(
                Arguments.of(new Coordinate(7, 3), new Coordinate(6, 3), Direction.UP),
                Arguments.of(new Coordinate(7, 3), new Coordinate(8, 3), Direction.DOWN),
                Arguments.of(new Coordinate(7, 3), new Coordinate(7, 4), Direction.RIGHT),
                Arguments.of(new Coordinate(7, 3), new Coordinate(7, 2), Direction.LEFT)
        );
    }
}