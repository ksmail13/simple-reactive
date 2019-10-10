package io.github.ksmail13.schedule;

public interface Scheduler {

    Worker worker();

    void work(Runnable runnable);

}
