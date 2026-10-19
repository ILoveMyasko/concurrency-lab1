package org.labs;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {

        final int pCount = 7;
        final int waiterCount = 2;
        final int foodStockSize = 1_000_000;
        final int plateSize = 3;

        Restaurant restaurant = new Restaurant(foodStockSize, waiterCount);
        Table table = new Table();
        List<Programmer> programmers = IntStream.range(0,pCount)
                .mapToObj(id -> new Programmer(id
                        , Programmer.getInitialTokens(id,pCount)
                        , restaurant
                        , table
                        , plateSize))
                .toList();

        for (int i = 0; i < pCount; i++) {
            Programmer left = programmers.get((i - 1 + pCount) % pCount);
            Programmer right = programmers.get((i + 1) % pCount);
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
            System.out.println("Programmer" + p.getId() + " " + p.getEaten()+ " " +  p.getPlate());
        }

        System.out.println(restaurant.getStockLeft());
        System.out.println((endTime - startTime)/ 1_000_000  + "ms");

    }
}