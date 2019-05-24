package Maze.MazeGenerator;

import Helpers.Direction;
import Maze.ELocation;
import Maze.Maze;
import Maze.MazeBuilder.Exceptions.MazeBuilderException;
import Maze.MazeBuilder.RectangleMazeBuilder;
import Maze.Solver.BFS.BFSSolverAdapter;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MazeGeneratorTest {

    @Test()
    void createRandomEntrancesAndExists() {
        MazeGenerator mazeGenerator = new MazeGenerator(new RectangleMazeBuilder(), new BFSSolverAdapter());

        assertNotNull(mazeGenerator);

        MazeGenerator mazeGeneratorResult;

        mazeGeneratorResult = mazeGenerator.generateMaze(500, 500);

        assertNotNull(mazeGenerator);
        assertEquals(mazeGenerator, mazeGeneratorResult);
        assertSame(mazeGenerator, mazeGeneratorResult);

        int numOfEntrances = 100;
        int numOfExits = 100;
        mazeGeneratorResult = mazeGenerator.createRandomEntrancesAndExists(numOfEntrances, numOfExits, 0);

        assertNotNull(mazeGenerator);
        assertEquals(mazeGenerator, mazeGeneratorResult);
        assertSame(mazeGenerator, mazeGeneratorResult);

        assertDoesNotThrow(mazeGenerator::create);
        Maze maze = null;

        boolean thrown = false;

        try {
            maze = mazeGenerator.create();
            thrown = false;
        } catch (MazeBuilderException e) {
            e.printStackTrace();
            thrown = true;
        } finally {
            assertFalse(thrown);
        }

        assertNotOnlyOneDirectionAtELocationList(numOfEntrances, maze.getEntrances());
        assertNotOnlyOneDirectionAtELocationList(numOfExits, maze.getExits());

    }

    private void assertNotOnlyOneDirectionAtELocationList(int totalELocations, List<ELocation> eLocations) {
        assertNotNull(eLocations);

        assertEquals(totalELocations, eLocations.size());

        Map<Direction, Integer> totalELocationsAtDirections = new HashMap<>();
        totalELocationsAtDirections.put(Direction.UP, 0);
        totalELocationsAtDirections.put(Direction.DOWN, 0);
        totalELocationsAtDirections.put(Direction.RIGHT, 0);
        totalELocationsAtDirections.put(Direction.LEFT, 0);

        eLocations.forEach(eLocation -> {
            assertNotNull(eLocation);
            Direction direction = eLocation.getDirection();

            totalELocationsAtDirections.put(direction, totalELocationsAtDirections.get(direction) + 1);
        });

        int count;

        count = totalELocationsAtDirections.get(Direction.UP);

        assertNotEquals(0, count);
        assertNotEquals(totalELocations, count);

        count = totalELocationsAtDirections.get(Direction.DOWN);

        assertNotEquals(0, count);
        assertNotEquals(totalELocations, count);

        count = totalELocationsAtDirections.get(Direction.RIGHT);

        assertNotEquals(0, count);
        assertNotEquals(totalELocations, count);

        count = totalELocationsAtDirections.get(Direction.LEFT);

        assertNotEquals(0, count);
        assertNotEquals(totalELocations, count);
    }
}