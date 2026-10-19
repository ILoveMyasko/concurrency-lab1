package org.labs;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class DiningSimulation {

    public static void simulate(int programmerCount, int waiterCount, int foodStockSize, int plateSize) {
        boolean invalidArgs = programmerCount < 2 || waiterCount < 1 || plateSize < 1 || foodStockSize < 1;
        if (invalidArgs) {
            System.out.println("Invalid parameters detected. Rolling back to default settings.");
        }
        final int finalProgrammerCount = programmerCount < 2 ? 7 : programmerCount;
        final int finalWaiterCount = waiterCount < 1 ? 2 : waiterCount;
        final int finalFoodStockSize = foodStockSize < 0 ? 1_000_000 : foodStockSize;
        final int finalPlateSize = plateSize < 1 ? 3 : plateSize;

        System.out.println("Programmers count: " + finalProgrammerCount);
        System.out.println("Waiters count: " + finalWaiterCount);
        System.out.println("Food Stock size : " + finalFoodStockSize);
        System.out.println("Plate size: " + finalPlateSize);

        Restaurant restaurant = new Restaurant(finalFoodStockSize, finalWaiterCount);
        Table table = new Table(restaurant);
        List<Programmer> programmers = IntStream.range(0,finalProgrammerCount)
                .mapToObj(id -> new Programmer(id
                        , Programmer.getInitialTokens(id,finalProgrammerCount)
                        , table
                        , finalPlateSize))
                .toList();

        for (int i = 0; i < finalProgrammerCount; i++) {
            Programmer left = programmers.get((i - 1 + finalProgrammerCount) % finalProgrammerCount);
            Programmer right = programmers.get((i + 1) % finalProgrammerCount);
            programmers.get(i).setNeighbors(left, right);
        }

        long startTime = System.nanoTime();
        try (ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofPlatform().factory())) {
            for (var p : programmers) {
                executor.submit(p);
            }
        }
        long endTime = System.nanoTime();

        for (var p: programmers) {
            System.out.println("Programmer" + p.getId() + " " + p.getEaten());
        }
        System.out.println("Execution time " +(endTime - startTime)/ 1_000_000  + "ms");
    }

}
