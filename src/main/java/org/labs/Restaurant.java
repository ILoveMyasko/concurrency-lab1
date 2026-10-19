package org.labs;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Restaurant {

    private final AtomicInteger foodStock;
    private final Semaphore waiters;

    public Restaurant(int foodStockSize, int waiterCount) {
        this.foodStock = new AtomicInteger(foodStockSize);
        this.waiters = new Semaphore(waiterCount);  //TODO: make true?
    }

    public int requestRefill(int plateSize){
        waiters.acquireUninterruptibly();
        try {
            while (true) {
                int currentStock = foodStock.get();
                if (currentStock <= 0) return 0;
                int toTake = Math.min(plateSize, currentStock);
                if (foodStock.compareAndSet(currentStock, currentStock - toTake)) {
                    return toTake;
                }
            }
        }
        finally {
            waiters.release();
        }
    }

    public int getStockLeft(){
        return foodStock.get();
    }

}
