package ru.geekbrains;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Car implements Runnable {
    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;
    }
    private final Race race;
    private final int speed;
    private final String name;
    private int count;
    private final CyclicBarrier cb;
    private final CountDownLatch cdl;
    private static boolean winnerFound;
    private static Lock win = new ReentrantLock();

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed, CyclicBarrier cb, CountDownLatch cdl) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        this.cb = cb;
        this.cdl = cdl;

    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            cb.await();
            for (int i = 0; i < race.getStages().size(); i++) {
                race.getStages().get(i).go(this);
            }
//            checkWinner(this);
            synchronized (win) {
                cdl.countDown();
                if (cdl.getCount() == 1)
                    System.out.println("Победил " + this.name);
            }
            cb.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
