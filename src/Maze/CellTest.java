package Maze;

import Helpers.Coordinate;
import Helpers.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    void constructorWithCoordinates() {
        Coordinate cellPos = new Coordinate(1, 1);
        Cell cell = new Cell(cellPos);

        assertNotEquals(cellPos, cell.location);
    }

    @Test
    void setCellAtDirection() {
        Coordinate cellPos = new Coordinate(1, 1);
        Cell cell = new Cell(cellPos);

        assertNotEquals(cellPos, cell.location);

        Coordinate nCellPos = new Coordinate(2, 2);
        Cell nCell = new Cell(nCellPos);

        try {
            cell.setCellAtDirection(Direction.TOP, nCell);
            assertNull("shouldn't arrive until here", "");
        } catch (Exception e) {
            assertNotNull(e);
        }
    }
}