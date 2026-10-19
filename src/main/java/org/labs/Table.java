package org.labs;

import java.util.concurrent.atomic.AtomicBoolean;

public class Table {

    private final AtomicBoolean finished = new AtomicBoolean(false);

    public void finish(){
        finished.compareAndSet(false, true);
    }

    public boolean isFinished(){
        return finished.get();
    }
}
