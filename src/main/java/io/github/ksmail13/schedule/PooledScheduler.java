package io.github.ksmail13.schedule;

import java.util.LinkedList;

class PooledScheduler implements Scheduler {
    private LinkedList<Worker> workers;
    private int curr;

    public PooledScheduler() {
        this(Runtime.getRuntime().availableProcessors());
    }

    public PooledScheduler(int cnt) {
        this("Pooled", cnt);
    }

    public PooledScheduler(String name, int parallelism) {
        WorkerFactory factory = new WorkerFactory(name);
        workers = new LinkedList<>();

        while(parallelism-- > 0) {
            Worker worker = factory.newWorker();
            workers.add(worker);
            worker.start();
        }
    }

    @Override
    public Worker worker() {
        Worker worker = workers.get(curr++);
        curr %= workers.size();
        System.out.printf("return worker(%s)\n", worker.getName());
        return worker;
    }

    @Override
    public void work(Runnable runnable) {
        workers.get(curr++).push(runnable);
        curr %= workers.size();
    }
}
