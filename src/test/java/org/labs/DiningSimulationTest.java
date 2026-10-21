package org.labs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class DiningSimulationTest {

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("Base test 100_000 stock size, 7 programmers, 5 sec timeout")
    void testBasicSimulation() {
        final int programmerCount = 7;
        final int foodStock = 100_000;
        final int waiters = 2;
        final int plateSize = 3;

        int[] eaten = DiningSimulation.simulate(programmerCount, waiters, foodStock, plateSize);

        int totalEaten = Arrays.stream(eaten).sum();
        assertEquals(foodStock, totalEaten, "Stock size and total eaten sum are not equal");

        for (int i = 0; i < programmerCount; i++) {
            assertTrue(eaten[i] > 0, "Programmer " + i + " ate 0");
        }
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("Edge case 2 programmers")
    void testMinimalTwoProgrammers() {
        final int foodStock = 10_000;
        final int programmerCount = 2;
        final int waiters = 2;
        final int plateSize = 3;
        int[] eaten = DiningSimulation.simulate(programmerCount, waiters, foodStock, plateSize);

        int totalEaten = eaten[0] + eaten[1];
        assertEquals(foodStock, totalEaten);
        assertTrue(Math.abs(eaten[0] - eaten[1]) <=plateSize,
                "Difference in eaten should be no more than plate size for 2 programmers");
    }

    @RepeatedTest(10)
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    @DisplayName("food stock = 0")
    void testTinyFoodRaceCondition() {
        final int programmerCount = 7;
        final int foodStock = 0;
        final int waiters = 2;
        final int plateSize = 3;
        int[] eaten = DiningSimulation.simulate(programmerCount, waiters, foodStock, plateSize);

        int totalEaten = 0;
        for (int count : eaten) totalEaten += count;
        assertEquals(0, totalEaten);
    }

    @ParameterizedTest(name = "Parameters test")
    @CsvSource({
            "-7,  2, 1000,  3",   // -7 programmes
            " 7,  0, 1000,  3",   // 0 waiters
            " 7,  2,   -1,  3",   // -1 stockFoodSize
            " 7,  2, 1000,  0",   // plateSize 0
            " 1,  1,    1,  1",   // 1 programmer only
            " 0,  0,    0,  0"    // all zeroes
    })
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testVariousInvalidInputsDoNotCrash(int programmerCount, int waiters, int food, int plate) {
        int[] eaten = DiningSimulation.simulate(programmerCount, waiters, food, plate);
        assertNotNull(eaten);
        assertTrue(eaten.length >= 2, "At least 2 programmers");
    }
}