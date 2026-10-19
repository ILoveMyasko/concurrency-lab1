package org.labs;

public class Main {
    public static void main(String[] args) {

        final int programmerCount = 7;
        final int waiterCount = 2;
        final int foodStockSize = 1_000_000;
        final int plateSize = 3;

        DiningSimulation.simulate(programmerCount, waiterCount, foodStockSize, plateSize);
    }
}