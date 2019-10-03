package io.github.ksmail13.schedule;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

class Worker extends Thread {

    private Queue<Runnable> works = new LinkedBlockingQueue<>();

    Worker(String s) {
        super(s);
    }

    @Override
    public void run() {
        for(;;){
            Runnable poll = works.poll();
            if (poll != null) {
                poll.run();
            }
        }
    }

    public void push(Runnable r) {
        works.add(r);
    }
}
