package io.github.ksmail13.schedule;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Worker extends Thread {

    private Queue<Runnable> works = new LinkedBlockingQueue<>();
    private volatile AtomicBoolean working = new AtomicBoolean(false);

    Worker(String s) {
        super(s);
        setDaemon(true);
    }

    @Override
    public void run() {
        for(;;){
            Runnable poll = works.poll();
            if (poll != null) {
                working.set(true);
                poll.run();
                working.set(false);
            }
        }
    }

    public void push(Runnable r) {
        works.add(r);
    }

    public boolean isWorking() {
        return working.get();
    }
}
