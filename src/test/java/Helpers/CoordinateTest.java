package Helpers;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {

    @Test
    void getRow() {
        int row;
        int column;

        Random random = new Random();

        int totalChecks = random.nextInt(30) + 10;

        for (int i = 0; i < totalChecks; i++) {
            row = random.nextInt(1000);
            column = random.nextInt(1000);

            Coordinate coordinate = new Coordinate(row, column);

            assertEquals(row, coordinate.getRow());
        }
    }

    @Test
    void setRow() {
        Random random = new Random();

        int row = random.nextInt(1000);
        int column = random.nextInt(1000);

        Coordinate coordinate = new Coordinate(row, column);
        assertEquals(row, coordinate.getRow());

        int totalChecks = random.nextInt(30) + 10;

        for (int i = 0; i < totalChecks; i++) {
            row = random.nextInt(1000);
            coordinate.setRow(row);
            assertEquals(row, coordinate.getRow());
        }
    }

    @Test
    void getColumn() {
        int row;
        int column;

        Random random = new Random();

        int totalChecks = random.nextInt(30) + 10;

        for (int i = 0; i < totalChecks; i++) {
            row = random.nextInt(1000);
            column = random.nextInt(1000);

            Coordinate coordinate = new Coordinate(row, column);

            assertEquals(column, coordinate.getColumn());
        }
    }

    @Test
    void setColumn() {
        Random random = new Random();

        int row = random.nextInt(1000);
        int column = random.nextInt(1000);

        Coordinate coordinate = new Coordinate(row, column);
        assertEquals(column, coordinate.getColumn());

        int totalChecks = random.nextInt(30) + 10;

        for (int i = 0; i < totalChecks; i++) {
            column = random.nextInt(1000);
            coordinate.setColumn(column);
            assertEquals(column, coordinate.getColumn());
        }
    }

    @Test
    void equals() {
        equalCheck(Object::equals);
    }

    @Test
    void staticEquals() {
        equalCheck((coord1, coord2) -> Coordinate.equals(coord1, coord2));
    }

    private void equalCheck(EqualFunction equalFn) {
        Random random = new Random();

        int row = random.nextInt(1000);
        int column = random.nextInt(1000);
        int otherRow;
        int otherColumn;

        Coordinate coordinate = new Coordinate(row, column);
        assertCoordinatesEquals(row, column, coordinate.getRow(), coordinate.getColumn());
        assertFalse(equalFn.equal(coordinate, null));

        int totalChecks = random.nextInt(30) + 10;

        for (int i = 0; i < totalChecks; i++) {
            otherRow = random.nextInt(1000) + 1000;
            otherColumn = random.nextInt(1000) + 1000;
            Coordinate otherCoordinates = new Coordinate(otherRow, otherColumn);

            assertCoordinatesEquals(row, column, coordinate.getRow(), coordinate.getColumn());

            assertCoordinatesEquals(otherRow, otherColumn, otherCoordinates.getRow(), otherCoordinates.getColumn());

            Coordinate otherEqualCoordinates = new Coordinate(otherRow, otherColumn);
            assertTrue(equalFn.equal(otherCoordinates, otherEqualCoordinates));
            assertFalse(equalFn.equal(otherCoordinates, null));
            assertFalse(equalFn.equal(coordinate, otherCoordinates));
            assertFalse(equalFn.equal(coordinate, null));
        }
    }

    @FunctionalInterface
    private interface EqualFunction {
        boolean equal(Coordinate a, Coordinate b);
    }

    private void assertCoordinatesEquals(int row, int column, int row2, int column2) {
        assertEquals(row, row2);
        assertEquals(column, column2);
    }
}