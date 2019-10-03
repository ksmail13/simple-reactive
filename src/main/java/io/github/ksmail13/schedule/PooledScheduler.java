package io.github.ksmail13.schedule;

import java.util.LinkedList;

class PooledScheduler implements Scheduler {
    private LinkedList<Worker> workers;
    private int curr;

    public PooledScheduler() {
        this(Runtime.getRuntime().availableProcessors());
    }

    public PooledScheduler(int cnt) {
        WorkerFactory factory = new WorkerFactory("Pooled");
        workers = new LinkedList<>();
        while(cnt-- > 0) {
            Worker worker = factory.newWorker();
            workers.add(worker);
            worker.start();
        }
    }


    @Override
    public void work(Runnable runnable) {
        workers.get(curr++).push(runnable);
        curr %= workers.size();
    }
}
