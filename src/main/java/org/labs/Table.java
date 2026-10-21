package org.labs;

import java.util.concurrent.atomic.AtomicBoolean;

public class Table {

    private final AtomicBoolean finished;
    private final Restaurant restaurant;

    public Table(Restaurant restaurant) {
        this.finished = new AtomicBoolean(false);
        this.restaurant = restaurant;
    }

    public void finish(){
        finished.compareAndSet(false, true);
    }

    public boolean isFinished(){
        return finished.get();
    }

    public int requestRefill(int plateSize){
        return restaurant.refill(plateSize);
    }
}
