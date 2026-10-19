package org.labs;

import java.util.concurrent.atomic.AtomicInteger;

public class Spoon {

    private final int id;
    private final AtomicInteger pointsTo;

    public Spoon(int id, int programmerId) {
        this.id = id;
        this.pointsTo = new AtomicInteger(programmerId);
    }

    public void pointTo(int programmerId){
        pointsTo.set(programmerId);
    }
}
