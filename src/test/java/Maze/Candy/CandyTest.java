package Maze.Candy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CandyTest {
    private Candy candy;

    private int candyStrength = 94;
    private int timeToLive = 37;
    private CandyPowerType type = CandyPowerType.Points;

    @BeforeEach
    void setUp() {
        candy = new Candy(type, candyStrength, timeToLive) {

        };
        assertNotNull(candy);
    }

    @Test
    void getCandyStrength() {
        assertNotNull(candy);

        assertDoesNotThrow(() -> {
            candy.getCandyStrength();
        });

        assertEquals(candyStrength, candy.getCandyStrength());

        int[] unequalCandyStrengths = new int[]{
                13,
                0,
                -4234
        };

        for (int candyStrength : unequalCandyStrengths) {
            assertNotEquals(candyStrength, candy.getCandyStrength());
        }

        assertEquals(candyStrength, candy.getCandyStrength());
    }

    @Test
    void setCandyStrength() {
        assertNotNull(candy);

        assertEquals(candyStrength, candy.getCandyStrength());

        int[] paramsTests = new int[]{
                candyStrength - 7,
                0,
                -19
        };

        for (int param : paramsTests) {
            candy.setCandyStrength(param);
            assertEquals(param, candy.getCandyStrength());
            assertNotEquals(candyStrength, candy.getCandyStrength());
        }

        assertDoesNotThrow(() -> {
            candy.setCandyStrength(candyStrength * 3);
            candy.setCandyStrength(candyStrength);
        });

        assertEquals(candyStrength, candy.getCandyStrength());
    }

    @Test
    void getType() {
        assertNotNull(candy);

        assertDoesNotThrow(() -> {
            candy.getType();
        });

        assertEquals(type, candy.getType());

        int[] unequalTypes = new int[]{
                13,
                0,
                -4234
        };

        for (int option : unequalTypes) {
            assertNotEquals(option, candy.getType());
        }

        assertEquals(type, candy.getType());
    }

    @Test
    void getTimeToLive() {
        assertNotNull(candy);

        assertDoesNotThrow(() -> {
            candy.getTimeToLive();
        });

        assertEquals(timeToLive, candy.getTimeToLive());

        int[] unequalsTimeToLive = new int[]{
                13,
                0,
                -4234
        };

        for (int option : unequalsTimeToLive) {
            assertNotEquals(option, candy.getTimeToLive());
        }

        assertEquals(timeToLive, candy.getTimeToLive());
    }

    @Test
    void setTimeToLive() {
        assertNotNull(candy);

        assertEquals(timeToLive, candy.getTimeToLive());

        int[] unequalTimeToLive = new int[]{
                timeToLive - 7,
                0,
                -19
        };

        for (int timeToLiveOption : unequalTimeToLive) {
            candy.setTimeToLive(timeToLiveOption);
            assertEquals(timeToLiveOption, candy.getTimeToLive());
            assertNotEquals(timeToLive, candy.getTimeToLive());
        }

        assertDoesNotThrow(() -> {
            candy.setTimeToLive(timeToLive * 3);
            candy.setTimeToLive(timeToLive);
        });

        assertEquals(candyStrength, candy.getCandyStrength());
    }

    @Test
    void equals() {
        assertTrue(candy.equals(candy));

        assertCandyEqual(type, candyStrength, timeToLive, true);
        assertCandyEqual(null, false);
        assertCandyEqual(CandyPowerType.Time, candyStrength - 5, timeToLive * 3, false);
        assertCandyEqual(type, candyStrength, timeToLive * 3, false);
        assertCandyEqual(type, candyStrength + 4, timeToLive, false);
        assertCandyEqual(CandyPowerType.Location, candyStrength, timeToLive, false);
        assertCandyEqual(CandyPowerType.Location, candyStrength + 19, timeToLive, false);
        assertCandyEqual(CandyPowerType.Location, candyStrength, timeToLive * 15, false);
    }

    private void assertCandyEqual(CandyPowerType otherType, int otherCandyStrength, int otherTimeToLive, boolean equals) {
        assertCandyEqual(new Candy(otherType, otherCandyStrength, otherTimeToLive) {
        }, equals);
    }

    private void assertCandyEqual(Candy otherCandy, boolean equals) {
        assertNotNull(candy);
        assertNotSame(candy, otherCandy);

        if (equals) {
            assertEquals(candy, otherCandy);
            assertTrue(candy.equals(otherCandy));
        } else {
            assertNotEquals(candy, otherCandy);
            assertFalse(candy.equals(otherCandy));
        }
    }
}