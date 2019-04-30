package Maze.MazeBuilder;

import Helpers.Coordinate;
import Maze.Cell;
import Maze.Maze;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangleMazeTest {

    @Test
    void buildMazeSkeleton() {
        RectangleMaze rectangleMaze = new RectangleMaze();

        assertNotNull(rectangleMaze);
        int height = 5;
        int width = 5;
        assertEquals(rectangleMaze, rectangleMaze.buildMazeSkeleton(height, width));

        Maze maze = rectangleMaze.getMaze();
        assertNotNull(maze);

        assertEquals(height, maze.getHeight());
        assertEquals(width, maze.getWidth());

        Cell[][] mazeData = maze.getMazeData();

        assertEquals(height, mazeData.length);

        for (Cell[] row : mazeData) {
            assertEquals(width, row.length);

            for (Cell cell : row) {
                assertNull(cell);
            }
        }
    }

    @Test
    void buildAllCellsAsEmpty() {
        RectangleMaze rectangleMaze = new RectangleMaze();

        assertNotNull(rectangleMaze);
        int height = 5;
        int width = 5;
        rectangleMaze
                .buildMazeSkeleton(height, width)
                .buildAllCellsAsEmpty();

        Maze maze = rectangleMaze.getMaze();
        assertNotNull(maze);

        assertEquals(height, maze.getHeight());
        assertEquals(width, maze.getWidth());

        Cell[][] mazeData = maze.getMazeData();

        assertEquals(height, mazeData.length);

        for (int i = 0; i < mazeData.length; i++) {
            Cell[] row = mazeData[i];
            assertEquals(width, row.length);

            for (int j = 0; j < row.length; j++) {
                Cell cell = row[j];
                assertNotNull(cell);
                this.assertEqualLocations(cell.getLocation(), i, j);
            }
        }
    }

    private void assertEqualLocations(Coordinate location, int row, int column) {
        assertNotNull(location);
        assertEquals(row, location.getRow());
        assertEquals(column, location.getColumn());
    }

    @Test
    void buildCell() {
    }

    @Test
    void buildDoor() {
    }

    @Test
    void buildDoor1() {
    }

    @Test
    void buildEntrance() {
    }

    @Test
    void buildExit() {
    }

    @Test
    void getMaze() {
    }
}