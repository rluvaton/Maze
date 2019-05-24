package Helpers;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsSame;
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


    @ParameterizedTest(name = "{index}: Utils.<{0}>cloneMatrix({1})")
    @MethodSource("provideDataForCloneMatrix")
    void cloneMatrix(String type, Object[][] mat, boolean isImmutable, cloneMatrixHelperFns fn) {
        Object[][] clonedMatrix = Utils.cloneMatrix(mat);
        if (mat != null) {
            assertNotNull(clonedMatrix);
        } else {
            assertNull(clonedMatrix);
            return;
        }

        assertEquals(mat.length, clonedMatrix.length);
        assertNotSame(mat, clonedMatrix);

        if (isImmutable) {
            assertArrayEquals(mat, clonedMatrix);
        }

        for (int i = 0; i < mat.length; i++) {
            Object[] row = mat[i];

            assertArrayEquals(row, clonedMatrix[i]);
            assertNotSame(row, clonedMatrix[i]);
        }

        int rowIndex = mat.length - 1;
        int columnIndex = mat[rowIndex].length - 1;

        fn.changeValue(mat, clonedMatrix, rowIndex, columnIndex);

        assertNotEquals(mat, clonedMatrix);

        for (int i = 0; i < mat.length; i++) {
            Object[] row = mat[i];

            assertNotSame(row, clonedMatrix[i]);

            if (!isImmutable) {
                for (int matColumnIndex = 0, rowLength = row.length; matColumnIndex < rowLength; matColumnIndex++) {
                    Object matItem = row[matColumnIndex];

                    assertNotSame(matItem, clonedMatrix[i][matColumnIndex]);
                }
            }
        }
    }

    private static Stream<Arguments> provideDataForCloneMatrix() {
        return Stream.of(
                Arguments.of(
                        "Integer",
                        new Integer[][]{
                                {5, 6, 7}
                        },
                        true,
                        (cloneMatrixHelperFns<Integer>) (mat, clonedMat, row, column) -> mat[row][column]++
                ),
                Arguments.of(
                        "String",
                        new String[][]{
                                {"s1", "s2", "s3"}
                        },
                        true,
                        (cloneMatrixHelperFns<String>) (mat, clonedMat, row, column) -> mat[row][column] = "s4"
                ),
                Arguments.of(
                        "CustomObject",
                        new CustomObjectForTestingClone[][]{
                                {
                                        new CustomObjectForTestingClone(1),
                                        new CustomObjectForTestingClone(2),
                                        new CustomObjectForTestingClone(3)
                                }
                        },
                        false,
                        (cloneMatrixHelperFns<CustomObjectForTestingClone>) (mat, clonedMat, row, column) -> mat[row][column] = new CustomObjectForTestingClone(4)
                )
        );
    }

    /**
     * For this test, the cloneable parameter must implement equals method
     */
    @ParameterizedTest(name = "{index}: Utils.<{0}>clone({1})")
    @MethodSource("provideDataForClone")
    void clone(SuccessCloneable cloneable, CallbackFns.ArgsVoidCallbackFunction changeValueFn) {
        SuccessCloneable cloned = Utils.clone(cloneable);
        assertEquals(cloned, cloneable);

        if (cloneable == null) {
            assertNull(cloneable);
            assertNull(cloned);

            return;
        }

        assertNotNull(cloneable);
        assertNotNull(cloned);
        assertNotSame(cloneable, cloned);

        assertNotNull(changeValueFn);
        changeValueFn.run(cloneable);

        assertNotEquals(cloneable, cloned);

        assertNotNull(cloneable);
        assertNotNull(cloned);
        assertNotSame(cloneable, cloned);
    }

    private static <T> void assertNotEquals(T unexpected, T actual) {
        MatcherAssert.assertThat(unexpected, IsNot.not(IsEqual.equalTo(actual)));
    }

    private static <T> void assertNotSame(T unexpected, T actual) {
        MatcherAssert.assertThat(unexpected, IsNot.not(IsSame.sameInstance(actual)));
    }

    private static Stream<Arguments> provideDataForClone() {
        return Stream.of(
                Arguments.of(
                        null,
                        null
                ),
                Arguments.of(
                        new CustomObjectForTestingClone(1),
                        (CallbackFns.ArgsVoidCallbackFunction<CustomObjectForTestingClone>) (CustomObjectForTestingClone obj) -> obj.num = 4
                )
        );
    }

    @FunctionalInterface
    private interface cloneMatrixHelperFns<T> {
        void changeValue(T[][] mat, T[][] clonedMat, int row, int column);
    }

    private static class CustomObjectForTestingClone implements SuccessCloneable<CustomObjectForTestingClone> {
        public int num;

        public CustomObjectForTestingClone(int num) {
            this.num = num;
        }

        @SuppressWarnings("MethodDoesntCallSuperMethod")
        @Override
        public CustomObjectForTestingClone clone() {
            return new CustomObjectForTestingClone(num);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CustomObjectForTestingClone that = (CustomObjectForTestingClone) o;
            return num == that.num;
        }
    }
}