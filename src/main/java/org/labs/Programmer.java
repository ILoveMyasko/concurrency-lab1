package org.labs;

import java.util.concurrent.Semaphore;

public class Programmer implements Runnable {

    private final int id;
    private Programmer leftNeighbor;
    private Programmer rightNeighbor;
    private final Semaphore spoonTokens;
    private final Table table;
    private final int plateSize;
    private int plate;
    private int eaten;

    public Programmer(int id, int tokens, Table table, int plateSize) {
        this.id = id;
        this.table = table;
        this.plateSize = plateSize;
        this.eaten = 0;
        this.spoonTokens = new Semaphore(tokens);
        this.plate = 0;
    }

    public void setNeighbors(Programmer left, Programmer right){
        this.leftNeighbor = left;
        this.rightNeighbor = right;
    }

    public void eat(){
        spoonTokens.acquireUninterruptibly(2);
        try {
            if (plate == 0){
                int refillSize = table.requestRefill(plateSize);
                if (refillSize == 0){
                    table.finish();
                    return;
                }
                plate = refillSize;
            }
            plate--;
            eaten++;

            if (table.isFinished()) {
                eaten += plate;
                plate = 0;
           }
        }
        finally {
            leftNeighbor.giveSpoonToken();
            rightNeighbor.giveSpoonToken();
        }

    }

    public void think() {
        Thread.onSpinWait();
    }

    @Override
    public void run() {
        while (!table.isFinished() || plate != 0)
        {
            eat();
            think();
        }
    }

    public void giveSpoonToken(){
        spoonTokens.release();
    }

    public static int getInitialTokens(final int id, final int n) {
        if (id == 0) {
            return 2;
        } else if (id == n / 2) {
            return 0;
        } else {
            return 1;
        }
    }

    public int getEaten() {
        return eaten;
    }

    public int getId(){
        return id;
    }

    public int getPlate(){
        return plate;
    }


}
