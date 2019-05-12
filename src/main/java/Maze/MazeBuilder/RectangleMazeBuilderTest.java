package Maze.MazeBuilder;

import Helpers.Coordinate;
import Maze.Cell;
import Maze.Maze;
import Maze.MazeBuilder.Exceptions.MazeBuilderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangleMazeBuilderTest {

    @Test
    void buildMazeSkeleton() {
        RectangleMazeBuilder rectangleMazeBuilder = new RectangleMazeBuilder();

        assertNotNull(rectangleMazeBuilder);
        int height = 5;
        int width = 5;
        assertEquals(rectangleMazeBuilder, rectangleMazeBuilder.buildMazeSkeleton(height, width));

        assertNotNull(rectangleMazeBuilder);

        assertThrows(MazeBuilderException.class, rectangleMazeBuilder::getMaze);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                assertNull(rectangleMazeBuilder.getCellAtPosition(new Coordinate(i, j)));
            }
        }
    }

    @Test
    void buildAllCellsAsEmpty() {
        RectangleMazeBuilder rectangleMazeBuilder = new RectangleMazeBuilder();

        assertNotNull(rectangleMazeBuilder);
        int height = 5;
        int width = 5;
        rectangleMazeBuilder
                .buildMazeSkeleton(height, width)
                .buildAllCellsAsEmpty();

        assertDoesNotThrow(rectangleMazeBuilder::getMaze);

        Maze maze = null;
        try {
            maze = rectangleMazeBuilder.getMaze();
        } catch (MazeBuilderException e) {
            e.printStackTrace();
        }

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

}