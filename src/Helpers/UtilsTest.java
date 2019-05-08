package Helpers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void moveCoordinatesToDirectionWithCoordinates() {
        testAllDirectionOfMovement(Utils.Instance::moveCoordinatesToDirection);
    }

    @Test
    void moveCoordinatesToDirectionWithRowAndColumn() {
        testAllDirectionOfMovement(
                (curr, dir) -> Utils.Instance.moveCoordinatesToDirection(curr.getRow(), curr.getColumn(), dir)
        );
    }

    private void testAllDirectionOfMovement(MoveCoordinatesToDirectionFn moveCoordinatesFn) {
        assertMoveCoordinatesWorks(
                new Coordinate(7, 3),
                new Coordinate(6, 3),
                Direction.UP,
                moveCoordinatesFn
        );

        assertMoveCoordinatesWorks(
                new Coordinate(7, 3),
                new Coordinate(8, 3),
                Direction.DOWN,
                moveCoordinatesFn
        );

        assertMoveCoordinatesWorks(
                new Coordinate(7, 3),
                new Coordinate(7, 4),
                Direction.RIGHT,
                moveCoordinatesFn
        );

        assertMoveCoordinatesWorks(
                new Coordinate(7, 3),
                new Coordinate(7, 2),
                Direction.LEFT,
                moveCoordinatesFn
        );

        // TODO - Test With nulls
    }

    private void assertMoveCoordinatesWorks(Coordinate currentLocation,
                                            Coordinate expectedCoordinatesForDirection,
                                            Direction moveDirection,
                                            MoveCoordinatesToDirectionFn moveCoordinatesFn) {
        Coordinate result = moveCoordinatesFn.moveCoordinatesToDirection(currentLocation, moveDirection);

        assertNotNull(result);
        assertNotEquals(currentLocation, result);

        assertCoordinatesEqual(expectedCoordinatesForDirection, result);
    }

    private void assertCoordinatesEqual(Coordinate expectedCoordinates, Coordinate resultCoordinates) {
        assertNotNull(expectedCoordinates);
        assertNotNull(resultCoordinates);

        assertEquals(expectedCoordinates.getRow(), resultCoordinates.getRow());
        assertEquals(expectedCoordinates.getColumn(), resultCoordinates.getColumn());
    }

    private interface MoveCoordinatesToDirectionFn {
        Coordinate moveCoordinatesToDirection(Coordinate curr, Direction dir);
    }

    @Test
    void getDirectionOfMove() {
        assertDirectionOfMoveWorks(
                new Coordinate(7, 3),
                new Coordinate(6, 3),
                Direction.UP
        );

        assertDirectionOfMoveWorks(
                new Coordinate(7, 3),
                new Coordinate(8, 3),
                Direction.DOWN
        );

        assertDirectionOfMoveWorks(
                new Coordinate(7, 3),
                new Coordinate(7, 4),
                Direction.RIGHT
        );

        assertDirectionOfMoveWorks(
                new Coordinate(7, 3),
                new Coordinate(7, 2),
                Direction.LEFT
        );
    }

    private void assertDirectionOfMoveWorks(Coordinate currentLocation,
                                            Coordinate afterMoveCoordinates,
                                            Direction moveDirection) {
        Direction result = Utils.Instance.getDirectionOfMove(currentLocation, afterMoveCoordinates);

        assertNotNull(result);
        assertEquals(moveDirection, result);
    }
}