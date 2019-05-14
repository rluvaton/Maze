package Helpers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomHelperTest {

    @Test
    void getRandomState() {
        int totalChecks = 50;
        int totalTrue = 0;
        int totalFalse = 0;

        for (int i = 0; i < totalChecks; i++) {
            if(RandomHelper.getRandomState()) {
                totalTrue++;
            } else {
                totalFalse++;
            }
        }

        assertNotEquals(totalChecks, totalFalse);
        assertNotEquals(totalChecks, totalTrue);

        assertDoesNotThrow(RandomHelper::getRandomState);
    }
}