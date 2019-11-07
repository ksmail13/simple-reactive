package io.github.ksmail13.schedule;

/**
 * Scheduler for Simple reactive
 */
public interface Scheduler {

    /**
     * get worker
     * @return worker
     */
    Worker worker();

    /**
     * run job for
     * @param runnable
     */
    void work(Runnable runnable);
}
