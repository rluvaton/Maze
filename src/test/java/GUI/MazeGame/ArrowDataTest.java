package GUI.MazeGame;

import Helpers.Direction;
import Helpers.RandomHelper;
import Maze.ELocationType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrowDataTest {

    @Test
    void constructorWith2Vars() {
        ELocationType[] eLocationTypes = ELocationType.values();
        Direction[] directions = Direction.values();

        for (Direction direction : directions) {
            for (ELocationType type : eLocationTypes) {
                ArrowData arrowData = new ArrowData(type, direction);

                assertNotNull(arrowData);

                assertEquals(0, arrowData.x);
                assertEquals(0, arrowData.y);
                assertEquals(type, arrowData.type);
                assertEquals(direction, arrowData.direction);
            }
        }
    }

    @Test
    void constructorWithAllVars() {
        ELocationType[] eLocationTypes = ELocationType.values();
        Direction[] directions = Direction.values();

        int x;
        int y;

        for (Direction direction : directions) {
            for (ELocationType type : eLocationTypes) {
                x = RandomHelper.getRandomNumber(-100000, 100000);
                y = RandomHelper.getRandomNumber(-100000, 100000);

                ArrowData arrowData = new ArrowData(x, y, type, direction);

                assertNotNull(arrowData);
                assertEquals(x, arrowData.x);
                assertEquals(y, arrowData.y);
                assertEquals(type, arrowData.type);
                assertEquals(direction, arrowData.direction);
            }
        }
    }

    @Test
    void setX() {
        ELocationType[] eLocationTypes = ELocationType.values();
        Direction[] directions = Direction.values();

        int x;

        for (int i = 0; i < 5; i++) {
            ArrowData arrowData = new ArrowData(RandomHelper.generateItemFromArray(eLocationTypes), RandomHelper.generateItemFromArray(directions));


            assertNotNull(arrowData);

            assertEquals(0, arrowData.x);
            assertEquals(0, arrowData.y);

            x = RandomHelper.getRandomNumber(-100000, 100000);
            ArrowData setResult = arrowData.setX(x);

            assertNotNull(setResult);
            assertSame(arrowData, setResult);
            assertEquals(arrowData, setResult);

            assertEquals(x, arrowData.x);
        }
    }

    @Test
    void setY() {
        ELocationType[] eLocationTypes = ELocationType.values();
        Direction[] directions = Direction.values();

        int y;

        for (int i = 0; i < 5; i++) {
            ArrowData arrowData = new ArrowData(RandomHelper.generateItemFromArray(eLocationTypes), RandomHelper.generateItemFromArray(directions));

            assertNotNull(arrowData);
            assertEquals(0, arrowData.x);
            assertEquals(0, arrowData.y);

            y = RandomHelper.getRandomNumber(-100000, 100000);
            ArrowData setResult = arrowData.setY(y);

            assertNotNull(setResult);
            assertSame(arrowData, setResult);
            assertEquals(arrowData, setResult);

            assertEquals(y, arrowData.y);
        }
    }

    @Test
    void setPoint() {
        ELocationType[] eLocationTypes = ELocationType.values();
        Direction[] directions = Direction.values();

        int x;
        int y;

        for (int i = 0; i < 5; i++) {
            ArrowData arrowData = new ArrowData(RandomHelper.generateItemFromArray(eLocationTypes), RandomHelper.generateItemFromArray(directions));

            assertNotNull(arrowData);
            assertEquals(0, arrowData.x);
            assertEquals(0, arrowData.y);

            x = RandomHelper.getRandomNumber(-100000, 100000);
            y = RandomHelper.getRandomNumber(-100000, 100000);

            ArrowData setResult = arrowData.setPoint(x, y);
            assertNotNull(setResult);
            assertSame(arrowData, setResult);
            assertEquals(arrowData, setResult);

            assertEquals(x, arrowData.x);
            assertEquals(y, arrowData.y);
        }
    }

    @Test
    void setType() {
        ELocationType[] eLocationTypes = ELocationType.values();
        Direction[] directions = Direction.values();

        for (ELocationType type : eLocationTypes) {
            ArrowData arrowData = new ArrowData(RandomHelper.generateItemFromArray(eLocationTypes), RandomHelper.generateItemFromArray(directions));

            assertNotNull(type);
            assertNotNull(arrowData);
            assertEquals(0, arrowData.x);
            assertEquals(0, arrowData.y);

            ArrowData setResult = arrowData.setType(type);

            assertNotNull(setResult);
            assertEquals(arrowData, setResult);
            assertSame(arrowData, setResult);

            assertEquals(type, arrowData.type);
        }
    }

    @Test
    void setDirection() {
        ELocationType[] eLocationTypes = ELocationType.values();
        Direction[] directions = Direction.values();

        for (Direction direction : directions) {
            ArrowData arrowData = new ArrowData(RandomHelper.generateItemFromArray(eLocationTypes), RandomHelper.generateItemFromArray(directions));

            assertNotNull(arrowData);
            assertNotNull(direction);
            assertEquals(0, arrowData.x);
            assertEquals(0, arrowData.y);

            ArrowData setResult = arrowData.setDirection(direction);

            assertNotNull(setResult);
            assertEquals(arrowData, setResult);
            assertSame(arrowData, setResult);

            assertEquals(direction, arrowData.direction);
        }
    }

    @Test
    void getDirectionBasedOnELocationTypeTestEntrances() {
        ArrowData arrowData;

        arrowData = new ArrowData(ELocationType.Entrance, Direction.UP);
        assertNotNull(arrowData);
        assertEquals(Direction.DOWN, arrowData.getDirectionBasedOnELocationType());

        arrowData = new ArrowData(ELocationType.Entrance, Direction.DOWN);
        assertNotNull(arrowData);
        assertEquals(Direction.UP, arrowData.getDirectionBasedOnELocationType());

        arrowData = new ArrowData(ELocationType.Entrance, Direction.RIGHT);
        assertNotNull(arrowData);
        assertEquals(Direction.LEFT, arrowData.getDirectionBasedOnELocationType());

        arrowData = new ArrowData(ELocationType.Entrance, Direction.LEFT);
        assertNotNull(arrowData);
        assertEquals(Direction.RIGHT, arrowData.getDirectionBasedOnELocationType());

    }

    @Test
    void getDirectionBasedOnELocationTypeTestExits() {
        ArrowData arrowData;

        arrowData = new ArrowData(ELocationType.Exit, Direction.UP);
        assertNotNull(arrowData);
        assertEquals(Direction.UP, arrowData.getDirectionBasedOnELocationType());

        arrowData = new ArrowData(ELocationType.Exit, Direction.DOWN);
        assertNotNull(arrowData);
        assertEquals(Direction.DOWN, arrowData.getDirectionBasedOnELocationType());

        arrowData = new ArrowData(ELocationType.Exit, Direction.RIGHT);
        assertNotNull(arrowData);
        assertEquals(Direction.RIGHT, arrowData.getDirectionBasedOnELocationType());

        arrowData = new ArrowData(ELocationType.Exit, Direction.LEFT);
        assertNotNull(arrowData);
        assertEquals(Direction.LEFT, arrowData.getDirectionBasedOnELocationType());

    }

}